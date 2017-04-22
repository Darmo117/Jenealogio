package net.darmo_creations.dao;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
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

import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.model.family.Wedding;

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
   * Loads the family from the given file.
   * 
   * @param file the file to load
   * @param locations <i>[output parameter]</i> this map will hold the location of all the cards
   * @return the loaded family object
   * @throws IOException if an I/O error occured
   * @throws ParseException if the file is corrupted/wrongly formatted
   */
  public Family load(String file, Map<Long, Point> locations) throws IOException, ParseException {
    String jsonString = String.join("", Files.readAllLines(Paths.get(file)));

    try {
      JSONParser p = new JSONParser();
      JSONObject obj = (JSONObject) p.parse(jsonString);
      Set<FamilyMember> members = new HashSet<>();
      Set<Wedding> weddings = new HashSet<>();
      Family family = new Family((Long) obj.get("global_id"), (String) obj.get("name"), members, weddings);

      // Members loading
      JSONArray membersObj = (JSONArray) obj.get("members");
      for (Object o : membersObj) {
        JSONObject memberObj = (JSONObject) o;
        long id = (Long) memberObj.get("id");
        BufferedImage image = base64Decode((String) memberObj.get("image"));
        String name = getNullIfEmpty((String) memberObj.get("name"));
        String firstName = getNullIfEmpty((String) memberObj.get("first_name"));
        Gender gender = Gender.fromCode((String) memberObj.get("gender"));
        Date birthDate = getDate((String) memberObj.get("birth_date"));
        String birthLocation = getNullIfEmpty((String) memberObj.get("birth_location"));
        Date deathDate = getDate((String) memberObj.get("death_date"));
        String deathLocation = getNullIfEmpty((String) memberObj.get("death_location"));
        JSONObject positionObj = (JSONObject) memberObj.get("position");
        int x = (int) (long) positionObj.get("x");
        int y = (int) (long) positionObj.get("y");

        locations.put(id, new Point(x, y));
        members.add(new FamilyMember(id, image, name, firstName, gender, birthDate, birthLocation, deathDate, deathLocation));
      }

      // Weddings loading
      JSONArray weddingsObj = (JSONArray) obj.get("weddings");
      for (Object o : weddingsObj) {
        JSONObject weddingObj = (JSONObject) o;
        Date date = getDate((String) weddingObj.get("date"));
        String location = getNullIfEmpty((String) weddingObj.get("location"));
        FamilyMember spouse1 = members.stream().filter(m -> m.getId() == (Long) weddingObj.get("spouse1")).findFirst().get();
        FamilyMember spouse2 = members.stream().filter(m -> m.getId() == (Long) weddingObj.get("spouse2")).findFirst().get();
        JSONArray childrenObj = (JSONArray) weddingObj.get("children");
        FamilyMember[] children = new FamilyMember[childrenObj.size()];

        for (int i = 0; i < children.length; i++) {
          final int j = i;
          children[i] = members.stream().filter(m -> m.getId() == (Long) childrenObj.get((Integer) j)).findFirst().get();
        }

        weddings.add(new Wedding(date, location, spouse1, spouse2, children));
      }

      return family;
    }
    catch (ClassCastException | NoSuchElementException | DateTimeParseException | org.json.simple.parser.ParseException __) {
      throw new ParseException("corrupted file", -1);
    }
  }

  /**
   * Returns nul if the given string is empty ({@code "".equals(s)}).
   * 
   * @param s the string
   * @return null if the string is empty; s otherwise
   */
  private String getNullIfEmpty(String s) {
    return "".equals(s) ? null : s;
  }

  /**
   * The pattern for dates formatted as YYYY-MM-dd
   */
  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

  /**
   * Returns the date object corresponding to the given string (YYYY-MM-dd format).
   * 
   * @param s the string
   * @return the date object or null if the string is empty
   * @throws DateTimeParseException if the date was wrongly formatted
   */
  private Date getDate(String s) throws DateTimeParseException {
    if (!s.isEmpty()) {
      Matcher matcher = DATE_PATTERN.matcher(s);

      if (matcher.matches()) {
        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int date = Integer.parseInt(matcher.group(3));

        return new Date(year, month, date);
      }

      throw new DateTimeParseException("wrong date format", s, -1);
    }

    return null;
  }

  /**
   * Saves the given family to the disk.
   * 
   * @param file the file to save to
   * @param family the family object
   * @param positions the locations for each card panel
   * @throws IOException if an I/O error occured
   */
  @SuppressWarnings("unchecked")
  public void save(String file, Family family, Map<Long, Point> positions) throws IOException {
    JSONObject obj = new JSONObject();

    obj.put("global_id", family.getGlobalId());
    obj.put("name", family.getName());

    JSONArray membersObj = new JSONArray();
    for (FamilyMember m : family.getAllMembers()) {
      JSONObject memberObj = new JSONObject();

      memberObj.put("id", m.getId());
      memberObj.put("name", m.getName().orElse(""));
      memberObj.put("first_name", m.getFirstName().orElse(""));
      memberObj.put("gender", m.getGender().getCode());
      formatDate(memberObj, "birth_date", m.getBirthDate());
      memberObj.put("birth_location", m.getBirthLocation().orElse(""));
      formatDate(memberObj, "death_date", m.getDeathDate());
      memberObj.put("death_location", m.getDeathLocation().orElse(""));
      memberObj.put("image", base64Encode(m.getImage()));
      JSONObject posObj = new JSONObject();
      posObj.put("x", positions.get(m.getId()).x);
      posObj.put("y", positions.get(m.getId()).y);
      memberObj.put("position", posObj);

      membersObj.add(memberObj);
    }
    obj.put("members", membersObj);

    JSONArray weddingsObj = new JSONArray();
    for (Wedding w : family.getAllWeddings()) {
      JSONObject weddingObj = new JSONObject();

      weddingObj.put("spouse1", w.getSpouse1().getId());
      weddingObj.put("spouse2", w.getSpouse2().getId());
      formatDate(weddingObj, "date", w.getDate());
      weddingObj.put("location", w.getLocation().orElse(""));
      JSONArray childrenObj = new JSONArray();
      w.getChildren().forEach(c -> childrenObj.add(c.getId()));
      weddingObj.put("children", childrenObj);

      weddingsObj.add(weddingObj);
    }
    obj.put("weddings", weddingsObj);

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
      DateTimeFormatter f = DateTimeFormatter.ofPattern("YYYY-MM-dd");
      date = f.format(LocalDate.of(d.getYear(), d.getMonth(), d.getDate()));
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
