package net.darmo_creations.model;

import java.awt.image.BufferedImage;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.model.graph.Node;
import net.darmo_creations.util.CalendarUtil;

public class FamilyMember extends Node<FamilyMember> {
  private final long id;
  private BufferedImage image;
  private String name;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private Date deathDate;

  /**
   * Une personne a un id interne, une photo, un nom, un prénom, un genre, une date de naissance et
   * une date de décès. Une personne ne connaît pas ses parents.
   */
  public FamilyMember(long id, BufferedImage image, String name, String firstName, Gender gender, Date birthDate, Date deathDate) {
    this.id = id;
    this.image = image;
    this.name = name;
    this.firstName = firstName;
    this.gender = Objects.requireNonNull(gender);
    this.birthDate = birthDate;
    this.deathDate = deathDate;
  }

  public long getId() {
    return this.id;
  }

  public Optional<BufferedImage> getImage() {
    return Optional.ofNullable(this.image);
  }

  void setImage(BufferedImage image) {
    this.image = image;
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
    return Optional.ofNullable(this.birthDate);
  }

  void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
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

  public Optional<Date> getDeathDate() {
    return Optional.ofNullable(this.deathDate);
  }

  void setDeathDate(Date deathDate) {
    this.deathDate = deathDate;
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
      return getName().orElse("?") + " " + getFirstName().orElse("?");
  }

  FamilyMember copy(long newId) {
    // @f0
    return new FamilyMember(
        newId,
        getImage().orElse(null),
        getName().orElse(null),
        getFirstName().orElse(null),
        getGender(),
        getBirthDate().orElse(null),
        getDeathDate().orElse(null));
    // @f1
  }
}
