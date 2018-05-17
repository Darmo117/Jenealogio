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
import net.darmo_creations.jenealogio.util.StringUtil;
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
   */
  public FamilyMember() {
    this(-1);
  }

  /**
   * Creates a new member with the given ID.
   * 
   * @param id internal ID
   */
  public FamilyMember(long id) {
    this.id = id;
    this.gender = Gender.UNKNOW;
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
   * @return this object
   */
  public FamilyMember setImage(@Nullable BufferedImage image) {
    this.image = image != null ? Images.deepCopy(image) : null;
    return this;
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
   * @return this object
   */
  public FamilyMember setFamilyName(@Nullable String familyName) {
    this.familyName = StringUtil.nullFromEmpty(familyName);
    return this;
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
   * @return this object
   */
  public FamilyMember setUseName(@Nullable String useName) {
    this.useName = StringUtil.nullFromEmpty(useName);
    return this;
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
   * @return this object
   */
  public FamilyMember setFirstName(@Nullable String firstName) {
    this.firstName = StringUtil.nullFromEmpty(firstName);
    return this;
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
   * @return this object
   */
  public FamilyMember setOtherNames(@Nullable String otherNames) {
    this.otherNames = StringUtil.nullFromEmpty(otherNames);
    return this;
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
   * @return this object
   */
  public FamilyMember setGender(Gender gender) {
    this.gender = Objects.requireNonNull(gender);
    return this;
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
   * @return this object
   */
  public FamilyMember setBirthDate(@Nullable Date birthDate) {
    checkDatesOrder(birthDate, this.deathDate);
    this.birthDate = birthDate != null ? birthDate.clone() : null;
    return this;
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

    return CalendarUtil.getPeriod(birthDate, currentDate);
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
   * @return this object
   */
  public FamilyMember setBirthLocation(@Nullable String birthLocation) {
    this.birthLocation = StringUtil.nullFromEmpty(birthLocation);
    return this;
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
   * @return this object
   */
  public FamilyMember setDeathDate(@Nullable Date deathDate) {
    checkDatesOrder(this.birthDate, deathDate);
    this.deathDate = deathDate != null ? deathDate.clone() : null;
    updateDead();
    return this;
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
   * @return this object
   */
  public FamilyMember setDeathLocation(@Nullable String deathLocation) {
    this.deathLocation = StringUtil.nullFromEmpty(deathLocation);
    updateDead();
    return this;
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
   * @return this object
   */
  public FamilyMember setDead(boolean dead) {
    if (!getDeathDate().isPresent() && !getDeathLocation().isPresent())
      this.dead = dead;
    return this;
  }

  private void updateDead() {
    this.dead = getDeathDate().isPresent() || getDeathLocation().isPresent();
  }

  private void checkDatesOrder(Date birth, Date death) {
    if (birth != null && death != null && birth.after(death))
      throw new IllegalStateException("birth date before death");
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
   * @return this object
   */
  public FamilyMember setComment(@Nullable String comment) {
    this.comment = StringUtil.nullFromEmpty(comment);
    return this;
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
    if (!(obj instanceof FamilyMember))
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

  @Override
  public int compareTo(FamilyMember f) {
    return Long.compare(getId(), f.getId());
  }

  /**
   * Returns a copy of this object with the given ID.
   * 
   * @param id the copy's ID
   * @return the copied person
   */
  FamilyMember copy(long id) {
    FamilyMember m = new FamilyMember(id);

    m.image = getImage().orElse(null);
    m.birthDate = getBirthDate().orElse(null);
    m.deathDate = getDeathDate().orElse(null);

    return m;
  }

  @Override
  public FamilyMember clone() {
    try {
      FamilyMember m = (FamilyMember) super.clone();

      m.image = getImage().orElse(null);
      m.birthDate = getBirthDate().orElse(null);
      m.deathDate = getDeathDate().orElse(null);

      return m;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
