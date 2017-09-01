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
package net.darmo_creations.jenealogio.model.family;

import java.awt.image.BufferedImage;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.util.CalendarUtil;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.Nullable;

/**
 * A family member has an internal ID, a family name, a use name, first name, other names, gender,
 * birth and death date and location, an image and a comment. It can be copied through the
 * <code>copy</code> method.
 * <p>
 * Two members can be compared. The comparison is done on the internal IDs. The <code>equals</code>
 * method is implemented as follow:
 * 
 * <pre>
 * public boolean equals(Object o) {
 *   if (o instanceof FamilyMember)
 *     return getId() == ((FamilyMember) o).getId();
 *   return false;
 * }
 * </pre>
 * </p>
 * 
 * @author Damien Vergnet
 */
public class FamilyMember implements Comparable<FamilyMember>, Cloneable {
  private final long id;
  private BufferedImage image;
  private String familyName;
  private String useName;
  private String firstName;
  private String otherNames;
  private Gender gender;
  private Date birthDate;
  private String birthLocation;
  private Date deathDate;
  private String deathLocation;
  private boolean dead;
  private String comment;

  /**
   * Creates a new member with no ID.
   * 
   * @param image the profile image
   * @param familyName the family name
   * @param useName the use name (e.g.: after marriage)
   * @param firstName the first name
   * @param otherNames other names
   * @param gender the gender (can be <code>Gender.UNKNOWN</code>)
   * @param birthDate the birthday
   * @param birthLocation the birth location
   * @param deathDate the death date
   * @param deathLocation the death location
   * @param dead if both the death date and death location are null, tells that if the person is
   *          dead or not; otherwise, if either the date or location is not null, it is ignored
   * @param comment a comment
   */
  public FamilyMember(@Nullable BufferedImage image, @Nullable String familyName, @Nullable String useName, @Nullable String firstName,
      @Nullable String otherNames, Gender gender, @Nullable Date birthDate, @Nullable String birthLocation, @Nullable Date deathDate,
      @Nullable String deathLocation, boolean dead, @Nullable String comment) {
    this(-1, image, familyName, useName, firstName, otherNames, gender, birthDate, birthLocation, deathDate, deathLocation, dead, comment);
  }

  /**
   * Creates a new member with the given ID.
   * 
   * @param id internal ID
   * @param image the profile image
   * @param familyName the family name
   * @param useName the use name (e.g.: after marriage)
   * @param firstName the first name
   * @param otherNames other names
   * @param gender the gender (can be <code>Gender.UNKNOWN</code>)
   * @param birthDate the birthday
   * @param birthLocation the birth location
   * @param deathDate the death date
   * @param deathLocation the death location
   * @param dead if both the death date and death location are null, tells that if the person is
   *          dead or not; otherwise, if either the date or location is not null, it is ignored
   * @param comment a comment
   */
  public FamilyMember(long id, @Nullable BufferedImage image, @Nullable String familyName, @Nullable String useName,
      @Nullable String firstName, @Nullable String otherNames, Gender gender, @Nullable Date birthDate, @Nullable String birthLocation,
      @Nullable Date deathDate, @Nullable String deathLocation, boolean dead, @Nullable String comment) {
    this.id = id;
    setImage(image);
    setFamilyName(familyName);
    setUseName(useName);
    setFirstName(firstName);
    setOtherNames(otherNames);
    setGender(gender);
    setBirthDate(birthDate);
    setBirthLocation(birthLocation);
    setDeathDate(deathDate);
    setDeathLocation(deathLocation);
    if (!isDead())
      setDead(dead);
    setComment(comment);
  }

  /**
   * @return the internal ID
   */
  public final long getId() {
    return this.id;
  }

  /**
   * @return the profile image
   */
  public Optional<BufferedImage> getImage() {
    return Optional.ofNullable(this.image != null ? Images.deepCopy(this.image) : null);
  }

  /**
   * Sets the profile image. May be null.
   * 
   * @param image the new profile image
   */
  void setImage(@Nullable BufferedImage image) {
    this.image = image != null ? Images.deepCopy(image) : null;
  }

  /**
   * @return the family name
   */
  public Optional<String> getFamilyName() {
    return Optional.ofNullable(this.familyName);
  }

  /**
   * Sets the family name. May be null.
   * 
   * @param familyName the new family name
   */
  void setFamilyName(@Nullable String familyName) {
    this.familyName = familyName;
  }

  /**
   * @return the use name
   */
  public Optional<String> getUseName() {
    return Optional.ofNullable(this.useName);
  }

  /**
   * Sets the use name. May be null.
   * 
   * @param useName the new use name
   */
  public void setUseName(@Nullable String useName) {
    this.useName = useName;
  }

  /**
   * @return the first name
   */
  public Optional<String> getFirstName() {
    return Optional.ofNullable(this.firstName);
  }

  /**
   * Sets the first name. May be null.
   * 
   * @param firstName the new first name
   */
  void setFirstName(@Nullable String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the other names
   */
  public Optional<String> getOtherNames() {
    return Optional.ofNullable(this.otherNames);
  }

  /**
   * Sets the other names. May be null.
   * 
   * @param firstName the new other names
   */
  public void setOtherNames(@Nullable String otherNames) {
    this.otherNames = otherNames;
  }

  /**
   * @return true if and only if this person is a man
   */
  public boolean isMan() {
    return this.gender.isMan();
  }

  /**
   * @return true if and only if this person is a woman
   */
  public boolean isWoman() {
    return this.gender.isWoman();
  }

  /**
   * @return the gender
   */
  public Gender getGender() {
    return this.gender;
  }

  /**
   * Sets the gender. Cannot be null.
   * 
   * @param gender the new gender
   */
  void setGender(Gender gender) {
    this.gender = Objects.requireNonNull(gender);
  }

  /**
   * @return the birth date
   */
  public Optional<Date> getBirthDate() {
    return Optional.ofNullable(this.birthDate != null ? this.birthDate.clone() : null);
  }

  /**
   * Sets the birth date. May be null.
   * 
   * @param birthDate the new birth date
   */
  void setBirthDate(@Nullable Date birthDate) {
    this.birthDate = birthDate != null ? birthDate.clone() : null;
  }

  /**
   * Returns this person's age:
   * <ul>
   * <li>if the birth date is not available or the death date is not avaiblable and the user is
   * dead, the age will be empty;</li>
   * <li>if the user is not dead and their birth date is known, the age will be calculated based on
   * the current date</li>
   * </ul>
   * 
   * @return this person's age
   */
  public Optional<Period> getAge() {
    if (!getBirthDate().isPresent() || !getDeathDate().isPresent() && isDead())
      return Optional.empty();

    Date currentDate = isDead() ? getDeathDate().get() : CalendarUtil.getCurrentDate();
    Date birthDate = getBirthDate().get();

    if (!birthDate.isIncomplete() && !currentDate.isIncomplete()) {
      int currentMonth = currentDate.getMonth();
      int birthMonth = birthDate.getMonth();
      int currentDay = currentDate.getDate();
      int birthDay = birthDate.getDate();
      int years = currentDate.getYear() - birthDate.getYear();
      int months = 0;
      int days = 0;

      if (birthMonth > currentMonth || birthMonth == currentMonth && birthDay > currentDay) {
        years = years == 0 ? 0 : years - 1;
        months = 12 - (birthMonth - currentMonth);
        if (birthDay > currentDay) {
          if (months > 0)
            months--;
          days = birthDate.getDaysNbInMonth() - (birthDay - currentDay);
        }
        else {
          days = currentDay - birthDay;
        }
      }
      else {
        months = currentMonth - birthMonth;
        if (birthDay > currentDay) {
          if (months > 0)
            months--;
          days = birthDate.getDaysNbInMonth() - (birthDay - currentDay);
        }
        else
          days = currentDay - birthDay;
      }

      return Optional.of(Period.of(years, months, days));
    }
    else if (birthDate.isYearSet() && birthDate.isMonthSet() && currentDate.isYearSet() && currentDate.isMonthSet()) {
      int currentMonth = currentDate.getMonth();
      int birthMonth = birthDate.getMonth();
      int years = currentDate.getYear() - birthDate.getYear();
      int months = 0;

      if (birthMonth > currentMonth) {
        years = years == 0 ? 0 : years - 1;
        months = 12 - (birthMonth - currentMonth);
      }
      else
        months = currentMonth - birthMonth;

      return Optional.of(Period.of(years, months, 0));
    }
    else if (birthDate.isYearSet() && currentDate.isYearSet()) {
      return Optional.of(Period.ofYears(currentDate.getYear() - birthDate.getYear()));
    }

    return Optional.empty();
  }

  /**
   * Compares this person against another based on their birthdays.
   * 
   * @param member the other person
   * @return 0 if both have the same birthdate; 1 if this person is younger; -1 if the other person
   *         is younger; nothing if one of the dates is empty
   */
  public Optional<Integer> compareBirthdays(FamilyMember member) {
    Optional<Date> thisBirth = getBirthDate();
    Optional<Date> otherBirth = member.getBirthDate();

    if (thisBirth.isPresent() && otherBirth.isPresent()) {
      return Optional.of(thisBirth.get().compareTo(otherBirth.get()));
    }

    return Optional.empty();
  }

  /**
   * @return the birth location
   */
  public Optional<String> getBirthLocation() {
    return Optional.ofNullable(this.birthLocation);
  }

  /**
   * Sets the birth location. May be null.
   * 
   * @param birthLocation the new birth location
   */
  void setBirthLocation(@Nullable String birthLocation) {
    this.birthLocation = birthLocation;
  }

  /**
   * @return the death date
   */
  public Optional<Date> getDeathDate() {
    return Optional.ofNullable(this.deathDate != null ? this.deathDate.clone() : null);
  }

  /**
   * Sets the death date. May be null. Updates the {@code dead} boolean.
   * 
   * @param deathDate the new death date
   */
  void setDeathDate(@Nullable Date deathDate) {
    this.deathDate = deathDate != null ? deathDate.clone() : null;
    updateDeath();
  }

  /**
   * @return the death location
   */
  public Optional<String> getDeathLocation() {
    return Optional.ofNullable(this.deathLocation);
  }

  /**
   * Sets the death location. May be null. Updates the {@code dead} boolean.
   * 
   * @param deathLocation the new death location
   */
  void setDeathLocation(@Nullable String deathLocation) {
    this.deathLocation = deathLocation;
    updateDeath();
  }

  /**
   * Updates the {@code dead} boolean.
   */
  private void updateDeath() {
    this.dead = getDeathDate().isPresent() || getDeathLocation().isPresent();
  }

  /**
   * @return true if the person is dead
   */
  public boolean isDead() {
    return this.dead;
  }

  /**
   * Sets if the person is dead. If either the death location or date is not null, this method will
   * do nothing.
   * 
   * @param dead true if the person is dead; false otherwise
   */
  void setDead(boolean dead) {
    if (!getDeathDate().isPresent() && !getDeathLocation().isPresent())
      this.dead = dead;
  }

  /**
   * @return the comment
   */
  public Optional<String> getComment() {
    return Optional.ofNullable(this.comment);
  }

  /**
   * Sets the comment. May be null.
   * 
   * @param comment the new comment
   */
  void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((this.birthDate == null) ? 0 : this.birthDate.hashCode());
    result = prime * result + ((this.birthLocation == null) ? 0 : this.birthLocation.hashCode());
    result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
    result = prime * result + (this.dead ? 1231 : 1237);
    result = prime * result + ((this.deathDate == null) ? 0 : this.deathDate.hashCode());
    result = prime * result + ((this.deathLocation == null) ? 0 : this.deathLocation.hashCode());
    result = prime * result + ((this.familyName == null) ? 0 : this.familyName.hashCode());
    result = prime * result + ((this.firstName == null) ? 0 : this.firstName.hashCode());
    result = prime * result + ((this.gender == null) ? 0 : this.gender.hashCode());
    result = prime * result + (int) (this.id ^ (this.id >>> 32));
    result = prime * result + ((this.image == null) ? 0 : Images.hashCode(this.image));
    result = prime * result + ((this.otherNames == null) ? 0 : this.otherNames.hashCode());
    result = prime * result + ((this.useName == null) ? 0 : this.useName.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FamilyMember other = (FamilyMember) obj;
    if (this.birthDate == null) {
      if (other.birthDate != null)
        return false;
    }
    else if (!this.birthDate.equals(other.birthDate))
      return false;
    if (this.birthLocation == null) {
      if (other.birthLocation != null)
        return false;
    }
    else if (!this.birthLocation.equals(other.birthLocation))
      return false;
    if (this.comment == null) {
      if (other.comment != null)
        return false;
    }
    else if (!this.comment.equals(other.comment))
      return false;
    if (this.dead != other.dead)
      return false;
    if (this.deathDate == null) {
      if (other.deathDate != null)
        return false;
    }
    else if (!this.deathDate.equals(other.deathDate))
      return false;
    if (this.deathLocation == null) {
      if (other.deathLocation != null)
        return false;
    }
    else if (!this.deathLocation.equals(other.deathLocation))
      return false;
    if (this.familyName == null) {
      if (other.familyName != null)
        return false;
    }
    else if (!this.familyName.equals(other.familyName))
      return false;
    if (this.firstName == null) {
      if (other.firstName != null)
        return false;
    }
    else if (!this.firstName.equals(other.firstName))
      return false;
    if (this.gender != other.gender)
      return false;
    if (this.id != other.id)
      return false;
    if (this.image == null) {
      if (other.image != null)
        return false;
    }
    else if (this.image != null) {
      if (other.image == null)
        return false;
    }
    else if (Images.hashCode(this.image) != Images.hashCode(other.image))
      return false;
    if (this.otherNames == null) {
      if (other.otherNames != null)
        return false;
    }
    else if (!this.otherNames.equals(other.otherNames))
      return false;
    if (this.useName == null) {
      if (other.useName != null)
        return false;
    }
    else if (!this.useName.equals(other.useName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    if (!getFirstName().isPresent() && !getFamilyName().isPresent())
      return "?";
    return getFirstName().orElse("?") + " " + getFamilyName().orElse("?");
  }

  /**
   * Copies this member and sets the copy's ID.
   * 
   * @param id the copy's ID
   * @return the copied person
   */
  FamilyMember clone(long id) {
    return new FamilyMember(id, getImage().orElse(null), this.familyName, this.useName, this.firstName, this.otherNames, this.gender,
        getBirthDate().orElse(null), this.birthLocation, getDeathDate().orElse(null), this.deathLocation, this.dead, this.comment);
  }

  @Override
  public int compareTo(FamilyMember f) {
    return Long.compare(getId(), f.getId());
  }

  @Override
  public FamilyMember clone() {
    return clone(getId());
  }
}
