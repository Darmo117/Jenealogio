package net.darmo_creations.model.family;

import java.awt.image.BufferedImage;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.graph.Node;
import net.darmo_creations.util.CalendarUtil;
import net.darmo_creations.util.ImageUtil;

/**
 * Une personne a un id interne, une photo, un nom, un prénom, un genre, une date de naissance et
 * une date de décès. Une personne ne connaît pas ses parents.
 */
public class FamilyMember extends Node<FamilyMember> implements Comparable<FamilyMember>, Cloneable {
  private final long id;
  private BufferedImage image;
  private String name;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private String birthPlace;
  private Date deathDate;
  private String deathPlace;

  public FamilyMember(BufferedImage image, String name, String firstName, Gender gender, Date birthDate, String birthPlace, Date deathDate, String deathPlace) {
    this(-1, image, name, firstName, gender, birthDate, birthPlace, deathDate, deathPlace);
  }

  public FamilyMember(long id, BufferedImage image, String name, String firstName, Gender gender, Date birthDate, String birthPlace, Date deathDate, String deathPlace) {
    this.id = id;
    this.image = image != null ? ImageUtil.deepCopy(image) : null;
    this.name = name;
    this.firstName = firstName;
    this.gender = Objects.requireNonNull(gender);
    this.birthDate = birthDate != null ? birthDate.clone() : null;
    this.birthPlace = birthPlace;
    this.deathDate = deathDate != null ? deathDate.clone() : null;
    this.deathPlace = deathPlace;
  }

  public long getId() {
    return this.id;
  }

  public Optional<BufferedImage> getImage() {
    return Optional.ofNullable(this.image != null ? ImageUtil.deepCopy(this.image) : null);
  }

  void setImage(BufferedImage image) {
    this.image = image != null ? ImageUtil.deepCopy(image) : null;
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
   * @return l'âge actuel de la personne ou au moment de sa mort (si la date est renseignée)
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

    // Si la date exacte n'est pas encore passée on retranche 1.
    if (birthMonth > currentMonth || birthMonth == currentMonth && birthDay > currentDay)
      years = years == 0 ? 0 : years - 1;

    return Optional.of(Period.ofYears(years));
  }

  public Optional<String> getBirthPlace() {
    return Optional.ofNullable(this.birthPlace);
  }

  void setBirthPlace(String birthPlace) {
    this.birthPlace = birthPlace;
  }

  public Optional<Date> getDeathDate() {
    return Optional.ofNullable(this.deathDate != null ? this.deathDate.clone() : null);
  }

  void setDeathDate(Date deathDate) {
    this.deathDate = deathDate != null ? deathDate.clone() : null;
  }

  public Optional<String> getDeathPlace() {
    return Optional.ofNullable(this.deathPlace);
  }

  void setDeathPlace(String deathPlace) {
    this.deathPlace = deathPlace;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(this.id);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof FamilyMember) {
      return getId() == ((FamilyMember) o).getId();
    }

    return false;
  }

  @Override
  public String toString() {
    if (!getName().isPresent() && !getFirstName().isPresent())
      return "?";
    else
      return getFirstName().orElse("?") + " " + getName().orElse("?");
  }

  FamilyMember copy(long id) {
    // @f0
    return new FamilyMember(
        id,
        getImage().orElse(null),
        getName().orElse(null),
        getFirstName().orElse(null),
        getGender(),
        getBirthDate().orElse(null),
        getBirthPlace().orElse(null),
        getDeathDate().orElse(null),
        getDeathPlace().orElse(null));
    // @f1
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