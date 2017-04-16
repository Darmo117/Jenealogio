package net.darmo_creations.model.family;

import java.awt.image.BufferedImage;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.model.Date;
import net.darmo_creations.util.CalendarUtil;
import net.darmo_creations.util.Images;

/**
 * A family member has an internal ID, a name, first name, gender, birth and death date and
 * location, an image.
 * 
 * @author Damien Vergnet
 */
public class FamilyMember implements Comparable<FamilyMember>, Cloneable {
  private final long id;
  private BufferedImage image;
  private String name;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private String birthLocation;
  private Date deathDate;
  private String deathLocation;

  /**
   * Creates a new member.
   * 
   * @param image [nullable] profile image
   * @param name [nullable]
   * @param firstName [nullable]
   * @param gender
   * @param birthDate [nullable]
   * @param birthPlace [nullable]
   * @param deathDate [nullable]
   * @param deathPlace [nullable]
   */
  public FamilyMember(BufferedImage image, String name, String firstName, Gender gender, Date birthDate, String birthPlace, Date deathDate,
      String deathPlace) {
    this(-1, image, name, firstName, gender, birthDate, birthPlace, deathDate, deathPlace);
  }

  /**
   * Creates a new member.
   * 
   * @param id internal ID
   * @param image [nullable] profile image
   * @param name [nullable]
   * @param firstName [nullable]
   * @param gender
   * @param birthDate [nullable]
   * @param birthPlace [nullable]
   * @param deathDate [nullable]
   * @param deathPlace [nullable]
   */
  public FamilyMember(long id, BufferedImage image, String name, String firstName, Gender gender, Date birthDate, String birthLocation,
      Date deathDate, String deathLocation) {
    this.id = id;
    this.image = image != null ? Images.deepCopy(image) : null;
    this.name = name;
    this.firstName = firstName;
    this.gender = Objects.requireNonNull(gender);
    this.birthDate = birthDate != null ? birthDate.clone() : null;
    this.birthLocation = birthLocation;
    this.deathDate = deathDate != null ? deathDate.clone() : null;
    this.deathLocation = deathLocation;
  }

  /**
   * @return internal ID
   */
  public long getId() {
    return this.id;
  }

  /**
   * @return profile image
   */
  public Optional<BufferedImage> getImage() {
    return Optional.ofNullable(this.image != null ? Images.deepCopy(this.image) : null);
  }

  /**
   * Sets the profile image.
   * 
   * @param image the new profile image
   */
  void setImage(BufferedImage image) {
    this.image = image != null ? Images.deepCopy(image) : null;
  }

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  void setName(String name) {
    this.name = name;
  }

  public Optional<String> getFirstName() {
    return Optional.ofNullable(this.firstName);
  }

  void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public boolean isMan() {
    return this.gender.isMan();
  }

  public boolean isWoman() {
    return this.gender.isWoman();
  }

  public Gender getGender() {
    return this.gender;
  }

  void setGender(Gender gender) {
    this.gender = gender;
  }

  public Optional<Date> getBirthDate() {
    return Optional.ofNullable(this.birthDate != null ? this.birthDate.clone() : null);
  }

  void setBirthDate(Date birthDate) {
    this.birthDate = birthDate != null ? birthDate.clone() : null;
  }

  /**
   * Returns the member's age.<br/>
   * <i>Special cases</i>: if the birthday is not available, the age will be empty; if the death
   * date is not available, age will be calculated based on the current date.
   * 
   * @return the member's current age
   */
  public Optional<Period> getAge() {
    if (!getBirthDate().isPresent())
      return Optional.empty();

    Optional<Date> deathDate = getDeathDate();
    Date currentDate = deathDate.isPresent() ? deathDate.get() : CalendarUtil.getCurrentDate();
    Date birthDate = getBirthDate().get();
    int currentMonth = currentDate.getMonth();
    int birthMonth = birthDate.getMonth();
    int currentDay = currentDate.getDate();
    int birthDay = birthDate.getDate();
    int years = currentDate.getYear() - birthDate.getYear();

    // If the date is not yet behind, remove.
    if (birthMonth > currentMonth || birthMonth == currentMonth && birthDay > currentDay)
      years = years == 0 ? 0 : years - 1;

    return Optional.of(Period.ofYears(years));
  }

  public Optional<String> getBirthLocation() {
    return Optional.ofNullable(this.birthLocation);
  }

  void setBirthPlace(String birthLocation) {
    this.birthLocation = birthLocation;
  }

  public Optional<Date> getDeathDate() {
    return Optional.ofNullable(this.deathDate != null ? this.deathDate.clone() : null);
  }

  void setDeathDate(Date deathDate) {
    this.deathDate = deathDate != null ? deathDate.clone() : null;
  }

  public Optional<String> getDeathLocation() {
    return Optional.ofNullable(this.deathLocation);
  }

  void setDeathLocation(String deathLocation) {
    this.deathLocation = deathLocation;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof FamilyMember)
      return getId() == ((FamilyMember) o).getId();
    return false;
  }

  @Override
  public String toString() {
    if (!getName().isPresent() && !getFirstName().isPresent())
      return "?";
    return getFirstName().orElse("?") + " " + getName().orElse("?");
  }

  FamilyMember copy(long id) {
    return new FamilyMember(id, getImage().orElse(null), getName().orElse(null), getFirstName().orElse(null), getGender(),
        getBirthDate().orElse(null), getBirthLocation().orElse(null), getDeathDate().orElse(null), getDeathLocation().orElse(null));
  }

  @Override
  public FamilyMember clone() {
    try {
      FamilyMember m = (FamilyMember) super.clone();

      getImage().ifPresent(image -> m.setImage(image));
      getBirthDate().ifPresent(date -> m.setBirthDate(date));
      getDeathDate().ifPresent(date -> m.setDeathDate(date));

      return m;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public int compareTo(FamilyMember f) {
    return Long.compare(getId(), f.getId());
  }
}
