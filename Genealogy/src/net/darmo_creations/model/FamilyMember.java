package net.darmo_creations.model;

import java.awt.image.BufferedImage;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.util.CalendarUtil;

public class FamilyMember {
  private final long id;
  private BufferedImage image;
  private String name;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private Date deathDate;
  private FamilyMember father;
  private FamilyMember mother;

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

  public void setImage(BufferedImage image) {
    this.image = image;
  }

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public void setName(String name) {
    this.name = name;
  }

  public Optional<String> getFirstName() {
    return Optional.ofNullable(this.firstName);
  }

  public void setFirstName(String firstName) {
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

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Optional<Date> getBirthDate() {
    return Optional.ofNullable(this.birthDate);
  }

  public void setBirthDate(Date birthDate) {
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

  public void setDeathDate(Date deathDate) {
    this.deathDate = deathDate;
  }

  public Optional<FamilyMember> getFather() {
    return Optional.ofNullable(this.father);
  }

  public void setFather(FamilyMember father) {
    this.father = father;
  }

  public Optional<FamilyMember> getMother() {
    return Optional.ofNullable(this.mother);
  }

  public void setMother(FamilyMember mother) {
    this.mother = mother;
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
}
