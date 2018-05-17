/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.jenealogio.dao;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.darmo_creations.jenealogio.Jenealogio;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasState;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CardState;
import net.darmo_creations.jenealogio.model.FamilyEdit;
import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.date.DateBuilder;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Gender;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.utils.version.Version;
import net.darmo_creations.utils.version.VersionException;

/**
 * This class handles I/O operations for the {@code Family} class.
 * 
 * @author Damien Vergnet
 */
public class FamilyDao {
  private static FamilyDao instance;

  /**
   * @return the instance
   */
  public static FamilyDao instance() {
    if (instance == null)
      instance = new FamilyDao();
    return instance;
  }

  /**
   * Loads the family from the given file with positions for the cards.
   * 
   * @param file the file to load
   * @param ignoreVersion if true, any version mismatch will be ignored
   * @return the loaded family and positions
   * @throws IOException if an I/O error occured
   * @throws ParseException if the file is corrupted/wrongly formatted
   * @throws VersionException if file's version is more recent than the current version
   */
  public FamilyEdit load(String file, boolean ignoreVersion) throws IOException, ParseException, VersionException {
    String jsonString = String.join("", Files.readAllLines(Paths.get(file)));

    try {
      JSONParser p = new JSONParser();
      JSONObject obj = (JSONObject) p.parse(jsonString);
      Set<FamilyMember> members = new HashSet<>();
      Set<Relationship> weddings = new HashSet<>();
      CanvasState state = new CanvasState();

      Long rawVersion = (Long) obj.get("version");
      Version version = new Version(rawVersion != null ? (int) (long) rawVersion : 0);

      if (!ignoreVersion && version.after(Jenealogio.CURRENT_VERSION)) {
        throw new VersionException(Jenealogio.CURRENT_VERSION, version);
      }

      if (version.before(Jenealogio.V1_6D))
        loadOld(version, obj, members, weddings, state);
      else
        load(version, obj, members, weddings, state);

      Family family = new Family((Long) obj.get("global_id"), (String) obj.get("name"), members, weddings);
      return new FamilyEdit(family, state);
    }
    catch (NullPointerException | ClassCastException | NoSuchElementException | DateTimeParseException
        | org.json.simple.parser.ParseException ex) {
      throw new ParseException("Corrupted file", -1);
    }
  }

  /**
   * Loading function prior to v1.6d.
   */
  private void loadOld(final Version version, final JSONObject obj, Set<FamilyMember> members, Set<Relationship> weddings,
      CanvasState state) {
    // Members loading
    JSONArray membersObj = (JSONArray) obj.get("members");
    for (Object o : membersObj) {
      JSONObject memberObj = (JSONObject) o;
      long id = (Long) memberObj.get("id");
      BufferedImage image = base64Decode((String) memberObj.get("image"));
      String familyName = getNullIfEmpty((String) memberObj.get("name"));
      String useName = getNullIfEmpty((String) memberObj.get("use_name"));
      String firstName = getNullIfEmpty((String) memberObj.get("first_name"));
      String otherNames = getNullIfEmpty((String) memberObj.get("other_names"));
      Gender gender = Gender.fromCode((String) memberObj.get("gender"));
      Date birthDate = getDate((String) memberObj.get("birth_date"));
      String birthLocation = getNullIfEmpty((String) memberObj.get("birth_location"));
      Date deathDate = getDate((String) memberObj.get("death_date"));
      String deathLocation = getNullIfEmpty((String) memberObj.get("death_location"));
      Boolean dead = (Boolean) memberObj.get("dead");
      String comment = getNullIfEmpty((String) memberObj.get("comment"));

      JSONObject positionObj = (JSONObject) memberObj.get("position");
      int x = (int) (long) positionObj.get("x");
      int y = (int) (long) positionObj.get("y");
      Point pos = new Point(x, y);

      state.addCardState(id, new CardState(pos, null));

      // #f:0
      FamilyMember member = new FamilyMember(id)
          .setImage(image)
          .setFamilyName(familyName)
          .setUseName(useName)
          .setFirstName(firstName)
          .setOtherNames(otherNames)
          .setGender(gender)
          .setBirthDate(birthDate)
          .setBirthLocation(birthLocation)
          .setDeathDate(deathDate)
          .setDeathLocation(deathLocation)
          .setComment(comment);
      // #f:1
      // This boolean may not be present in older save versions.
      if (dead != null)
        member.setDead(dead);
      members.add(member);
    }

    // Relations loading
    JSONArray relationsObj;
    boolean before1_3d = version.before(Jenealogio.V1_3D);

    if (before1_3d) {
      relationsObj = (JSONArray) obj.get("weddings");
    }
    else {
      relationsObj = (JSONArray) obj.get("relations");
    }

    for (Object o : relationsObj) {
      JSONObject relationObj = (JSONObject) o;
      Date date = getDate((String) relationObj.get("date"));
      String location = getNullIfEmpty((String) relationObj.get("location"));
      boolean isWedding = true;
      boolean hasEnded = false;
      Date endDate = null;

      if (!before1_3d) {
        String type = getNullIfEmpty((String) relationObj.get("type"));
        isWedding = "wedding".equalsIgnoreCase(type);
        String s = (String) relationObj.get("end_date");
        if (s != null)
          endDate = getDate(s);
        if (endDate == null)
          hasEnded = (Boolean) relationObj.get("has_ended");
        else
          hasEnded = true;
      }

      String partnerKey = before1_3d ? "spouse" : "partner";
      long partner1 = (Long) relationObj.get(partnerKey + 1);
      long partner2 = (Long) relationObj.get(partnerKey + 2);

      // #f:0
      Relationship r = new Relationship(partner1, partner2)
          .setDate(date)
          .setLocation(location)
          .setWedding(isWedding)
          .setHasEnded(hasEnded)
          .setEndDate(endDate);
      // #f:1

      JSONArray childrenObj = (JSONArray) relationObj.get("children");

      for (int i = 0; i < childrenObj.size(); i++) {
        r.addChild((Long) childrenObj.get(i));
      }

      Map<Long, Date> adoptions = new HashMap<>();
      JSONObject adoptionsObj = (JSONObject) relationObj.get("adoptions");
      if (adoptionsObj != null) {
        for (Object id : adoptionsObj.keySet()) {
          Optional<String> s = Optional.of((String) adoptionsObj.get(id));
          adoptions.put(Long.parseLong("" + id), getDate(s.orElse("")));
        }
      }

      weddings.add(r);
    }
  }

  /**
   * Loading function for v1.6d+.
   */
  private void load(final Version version, final JSONObject obj, Set<FamilyMember> members, Set<Relationship> weddings, CanvasState state) {
    JSONArray membersObj = (JSONArray) obj.get("members");
    for (Object o : membersObj) {
      JSONObject memberObj = (JSONObject) o;
      long id = (Long) memberObj.get("id");
      BufferedImage image = base64Decode((String) memberObj.get("image"));
      String familyName = getNullIfEmpty((String) memberObj.get("name"));
      String useName = getNullIfEmpty((String) memberObj.get("use_name"));
      String firstName = getNullIfEmpty((String) memberObj.get("first_name"));
      String otherNames = getNullIfEmpty((String) memberObj.get("other_names"));
      Gender gender = Gender.fromCode((String) memberObj.get("gender"));
      Date birthDate = getDate((String) memberObj.get("birth_date"));
      String birthLocation = getNullIfEmpty((String) memberObj.get("birth_location"));
      Date deathDate = getDate((String) memberObj.get("death_date"));
      String deathLocation = getNullIfEmpty((String) memberObj.get("death_location"));
      boolean dead = (Boolean) memberObj.get("dead");
      String comment = getNullIfEmpty((String) memberObj.get("comment"));

      // #f:0
      FamilyMember member = new FamilyMember(id)
          .setImage(image)
          .setFamilyName(familyName)
          .setUseName(useName)
          .setFirstName(firstName)
          .setOtherNames(otherNames)
          .setGender(gender)
          .setBirthDate(birthDate)
          .setBirthLocation(birthLocation)
          .setDeathDate(deathDate)
          .setDeathLocation(deathLocation)
          .setDead(dead)
          .setComment(comment);
      // #f:1
      members.add(member);
    }

    JSONArray relationsObj = (JSONArray) obj.get("relations");
    for (Object o : relationsObj) {
      JSONObject relationObj = (JSONObject) o;
      Date date = getDate((String) relationObj.get("date"));
      String location = getNullIfEmpty((String) relationObj.get("location"));
      boolean isWedding = true;
      boolean hasEnded = false;
      Date endDate = null;

      String type = getNullIfEmpty((String) relationObj.get("type"));
      isWedding = "wedding".equalsIgnoreCase(type);
      String s = (String) relationObj.get("end_date");
      if (s != null)
        endDate = getDate(s);
      if (endDate == null)
        hasEnded = (Boolean) relationObj.get("has_ended");
      else
        hasEnded = true;

      long partner1 = (Long) relationObj.get("partner1");
      long partner2 = (Long) relationObj.get("partner2");

      // #f:0
      Relationship r = new Relationship(partner1, partner2)
          .setDate(date)
          .setLocation(location)
          .setWedding(isWedding)
          .setHasEnded(hasEnded)
          .setEndDate(endDate);
      // #f:1

      JSONArray childrenObj = (JSONArray) relationObj.get("children");

      for (int i = 0; i < childrenObj.size(); i++) {
        r.addChild((Long) childrenObj.get(i));
      }

      JSONObject adoptionsObj = (JSONObject) relationObj.get("adoptions");
      if (adoptionsObj != null) {
        for (Object id : adoptionsObj.keySet()) {
          Optional<String> opt = Optional.of((String) adoptionsObj.get(id));
          r.setAdopted(Long.parseLong("" + id), getDate(opt.orElse("")));
        }
      }

      weddings.add(r);
    }

    JSONObject canvasStateObj = (JSONObject) obj.get("canvas_state");
    JSONArray cardStatesObj = (JSONArray) canvasStateObj.get("cards");

    for (Object o : cardStatesObj) {
      JSONObject item = (JSONObject) o;

      long id = (Long) item.get("id");

      JSONObject positionObj = (JSONObject) item.get("position");
      int x = (int) (long) positionObj.get("x");
      int y = (int) (long) positionObj.get("y");
      Point pos = new Point(x, y);

      JSONObject dimensionObj = (JSONObject) item.get("size");
      int w = (int) (long) dimensionObj.get("w");
      int h = (int) (long) dimensionObj.get("h");
      Dimension size = new Dimension(w, h);

      state.addCardState(id, new CardState(pos, size));
    }
  }

  /**
   * Returns null if the given string is empty ({@code "".equals(s)}).
   * 
   * @param s the string
   * @return null if the string is empty; s otherwise
   */
  private String getNullIfEmpty(String s) {
    return "".equals(s) ? null : s;
  }

  /**
   * The pattern for dates formatted as YYYY-MM-DD
   */
  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}|\\?{4})-(\\d{2}|\\?{2})-(\\d{2}|\\?{2})");

  /**
   * Returns the date object corresponding to the given string (YYYY-MM-DD format).
   * 
   * @param s the string
   * @return the date object or null if the string is empty
   * @throws DateTimeParseException if the date was wrongly formatted
   */
  private Date getDate(String s) throws DateTimeParseException {
    if (!s.isEmpty()) {
      Matcher matcher = DATE_PATTERN.matcher(s);

      if (matcher.matches()) {
        DateBuilder builder = new DateBuilder();
        String year = matcher.group(1);
        String month = matcher.group(2);
        String date = matcher.group(3);

        if (year.matches("\\d{4}"))
          builder.setYear(Integer.parseInt(year));
        if (month.matches("\\d{2}"))
          builder.setMonth(Integer.parseInt(month));
        if (date.matches("\\d{2}"))
          builder.setDate(Integer.parseInt(date));

        return builder.getDate();
      }

      throw new DateTimeParseException("wrong date format", s, -1);
    }

    return null;
  }

  /**
   * Saves the given family to the disk.
   * 
   * @param file the file to save to
   * @param edit the edit to save
   * @throws IOException if an I/O error occured
   */
  @SuppressWarnings("unchecked")
  public void save(String file, final FamilyEdit edit) throws IOException {
    JSONObject obj = new JSONObject();
    Family family = edit.getFamily();
    CanvasState state = edit.getCanvasState();

    String comment = String.format(
        "This is a save file for Jenealogio v%1$s. "
            + "It is strongly not recommended to modify this file without using Jenealogio v%1$s or higher as it may break it.",
        Jenealogio.CURRENT_VERSION);
    obj.put("_comment", comment);
    obj.put("version", Jenealogio.CURRENT_VERSION.getFullValue());
    obj.put("global_id", family.getGlobalId());
    obj.put("name", family.getName());

    // Members
    JSONArray membersObj = new JSONArray();
    for (FamilyMember m : family.getAllMembers()) {
      JSONObject memberObj = new JSONObject();

      memberObj.put("id", m.getId());
      memberObj.put("name", m.getFamilyName().orElse(""));
      memberObj.put("use_name", m.getUseName().orElse(""));
      memberObj.put("first_name", m.getFirstName().orElse(""));
      memberObj.put("other_names", m.getOtherNames().orElse(""));
      memberObj.put("gender", m.getGender().getCode());
      formatDate(memberObj, "birth_date", m.getBirthDate());
      memberObj.put("birth_location", m.getBirthLocation().orElse(""));
      formatDate(memberObj, "death_date", m.getDeathDate());
      memberObj.put("death_location", m.getDeathLocation().orElse(""));
      memberObj.put("dead", m.isDead());
      memberObj.put("comment", m.getComment().orElse(""));
      memberObj.put("image", base64Encode(m.getImage()));

      membersObj.add(memberObj);
    }
    obj.put("members", membersObj);

    // Relations
    JSONArray relationsObj = new JSONArray();
    for (Relationship r : family.getAllRelations()) {
      JSONObject relationObj = new JSONObject();

      relationObj.put("partner1", r.getPartner1());
      relationObj.put("partner2", r.getPartner2());
      relationObj.put("type", r.isWedding() ? "wedding" : "");
      formatDate(relationObj, "date", r.getDate());
      relationObj.put("location", r.getLocation().orElse(""));
      if (r.getEndDate().isPresent()) {
        formatDate(relationObj, "end_date", r.getEndDate());
      }
      else {
        relationObj.put("has_ended", r.hasEnded());
      }
      JSONArray childrenObj = new JSONArray();
      JSONObject adoptionsObj = new JSONObject();
      for (Long c : r.getChildren()) {
        childrenObj.add(c);
        if (r.isAdopted(c))
          formatDate(adoptionsObj, "" + c, r.getAdoptionDate(c));
      }
      relationObj.put("children", childrenObj);
      relationObj.put("adoptions", adoptionsObj);

      relationsObj.add(relationObj);
    }
    obj.put("relations", relationsObj);

    // Canvas state
    JSONObject canvasStateObj = new JSONObject();

    JSONArray cardStatesObj = new JSONArray();
    for (Map.Entry<Long, CardState> entry : state.getCardStates().entrySet()) {
      JSONObject cardObj = new JSONObject();

      cardObj.put("id", entry.getKey());

      JSONObject posObj = new JSONObject();
      Point pos = entry.getValue().getLocation();
      posObj.put("x", pos.x);
      posObj.put("y", pos.y);
      cardObj.put("position", posObj);

      JSONObject dimObj = new JSONObject();
      Dimension size = entry.getValue().getSize();
      dimObj.put("w", size.width);
      dimObj.put("h", size.height);
      cardObj.put("size", dimObj);

      cardStatesObj.add(cardObj);
    }
    canvasStateObj.put("cards", cardStatesObj);

    obj.put("canvas_state", canvasStateObj);

    Files.write(Paths.get(file), Arrays.asList(obj.toJSONString()));
  }

  /**
   * Formats a date object as "YYYY-MM-dd" and puts it in the given JSON object under the given key.
   * 
   * @param obj the JSON object
   * @param key the key
   * @param optDate the date
   */
  @SuppressWarnings("unchecked")
  private void formatDate(JSONObject obj, String key, Optional<Date> optDate) {
    String date = "";

    if (optDate.isPresent()) {
      Date d = optDate.get();

      date += d.isYearSet() ? String.format("%04d", d.getYear()) : "????";
      date += "-" + (d.isMonthSet() ? String.format("%02d", d.getMonth()) : "??");
      date += "-" + (d.isDateSet() ? String.format("%02d", d.getDate()) : "??");
    }
    obj.put(key, date);
  }

  /**
   * Encodes an image to a base64 string.
   * 
   * @param image the image
   * @return the base64 string or an empty string if nothing was given
   */
  private String base64Encode(Optional<BufferedImage> image) {
    try {
      if (image.isPresent()) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
          ImageIO.write(image.get(), "png", baos);
          return DatatypeConverter.printBase64Binary(baos.toByteArray());
        }
      }
      else
        return "";
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Decodes a base64 string and returns the corresponding image.
   * 
   * @param base64 the base64 string
   * @return the corresponding image
   */
  private BufferedImage base64Decode(String base64) {
    try {
      if (base64 != null && !"".equals(base64)) {
        byte[] imageByte = DatatypeConverter.parseBase64Binary(base64);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
          return ImageIO.read(bis);
        }
      }
      else
        return null;
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private FamilyDao() {}
}
