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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.util.StringUtil;
import net.darmo_creations.utils.Nullable;

/**
 * This class represents a relationship between two people.
 * 
 * @author Damien Vergnet
 */
public final class Relationship implements Comparable<Relationship>, Cloneable {
  private Date date;
  private String location;
  private final long partner1, partner2;
  private Set<Long> children;
  private Map<Long, Date> adoptions;
  private boolean isWedding;
  private boolean hasEnded;
  private Date endDate;

  /**
   * Creates a new relation. The two partners must be different or else an exception will be thrown.
   * 
   * @param partner1 one partner
   * @param partner2 the other partner
   */
  public Relationship(long partner1, long partner2) {
    if (partner1 == partner2)
      throw new IllegalArgumentException("partners must be different");
    this.partner1 = partner1;
    this.partner2 = partner2;
    this.children = new HashSet<>();
    this.adoptions = new HashMap<>();
  }

  /**
   * @return the relation's start date
   */
  public Optional<Date> getDate() {
    return Optional.ofNullable(this.date);
  }

  /**
   * Sets the relation's start date. May be null.
   * 
   * @param date the new date
   * @return this object
   */
  public Relationship setDate(@Nullable Date date) {
    this.date = date;
    return this;
  }

  /**
   * @return the relation's location when it started
   */
  public Optional<String> getLocation() {
    return Optional.ofNullable(this.location);
  }

  /**
   * Sets the relation's location when it started. May be null.
   * 
   * @param location the new location
   * @return this object
   */
  public Relationship setLocation(@Nullable String location) {
    this.location = StringUtil.nullFromEmpty(location);
    return this;
  }

  /**
   * @return true if the relationship is a marriage
   */
  public boolean isWedding() {
    return this.isWedding;
  }

  /**
   * Sets the relationship's type.
   * 
   * @param isWedding true if it is a wedding
   * @return this object
   */
  public Relationship setWedding(boolean isWedding) {
    this.isWedding = isWedding;
    return this;
  }

  /**
   * @return true if the relation has ended
   */
  public boolean hasEnded() {
    return this.hasEnded;
  }

  /**
   * Sets if the relation has ended. If an end date is already set, this will do nothing.
   * 
   * @param hasEnded the new value
   * @return this object
   */
  public Relationship setHasEnded(boolean hasEnded) {
    if (this.endDate == null)
      this.hasEnded = hasEnded;
    return this;
  }

  /**
   * @return the end date
   */
  public Optional<Date> getEndDate() {
    return Optional.ofNullable(this.endDate);
  }

  /**
   * Sets the end date. It will also update the hasEnded property.
   * 
   * @param endDate the new date
   * @return this object
   */
  public Relationship setEndDate(@Nullable Date endDate) {
    if (endDate != null)
      this.hasEnded = true;
    this.endDate = endDate;
    return this;
  }

  /**
   * @return the first partner's ID
   */
  public long getPartner1() {
    return this.partner1;
  }

  /**
   * @return the second partner's ID
   */
  public long getPartner2() {
    return this.partner2;
  }

  /**
   * Tells if the given person is a child from this wedding.
   * 
   * @param id the person's ID
   * @return true if and only if the person is a child from this wedding
   */
  public boolean isChild(long id) {
    return this.children.contains(id);
  }

  /**
   * @return a set of all the children's IDs
   */
  public Set<Long> getChildren() {
    return new HashSet<>(this.children);
  }

  /**
   * Adds a child to this relation. The child must be different from the two partners and not
   * already present in the children list.
   * 
   * @param id the child ID to add
   */
  public void addChild(long id) {
    if (id == getPartner1() || id == getPartner2())
      throw new IllegalArgumentException("can't be their own child");
    this.children.add(id);
  }

  /**
   * Sets a child as adopted.
   * 
   * @param id the child's ID
   * @param date the adoption date
   */
  public void setAdopted(long id, @Nullable Date date) {
    if (!this.children.contains(id))
      throw new NoSuchElementException("child ID " + id + " is not present in this relationship");
    this.adoptions.put(id, date);
  }

  /**
   * Sets a child as not adopted.
   * 
   * @param id the child's ID
   */
  public void setNotAdopted(long id) {
    if (!this.children.contains(id))
      throw new NoSuchElementException("child ID " + id + " is not present in this relationship");
    this.adoptions.remove(id);
  }

  /**
   * Deletes the given child.
   * 
   * @param id the child ID to delete
   */
  public void removeChild(long id) {
    if (!this.children.contains(id))
      throw new NoSuchElementException("child ID " + id + " is not present in this relationship");
    this.children.remove(id);
    this.adoptions.remove(id);
  }

  /**
   * Tells if the given person is one of the two partners from this relationship.
   * 
   * @param id the person's ID
   * @return true if and only if the person is one of the partners
   */
  public boolean isInRelationship(long id) {
    return getPartner1() == id || getPartner2() == id;
  }

  /**
   * Indicates if a child is adopted.
   * 
   * @param id the child's ID
   * @return true if the child has been adopted by this couple in this relationship; false otherwise
   */
  public boolean isAdopted(long id) {
    return this.adoptions.containsKey(id);
  }

  /**
   * Returns the adoption date for a given child.
   * 
   * @param id the child's ID
   * @return the adoption date
   */
  public Optional<Date> getAdoptionDate(long id) {
    return Optional.ofNullable(this.adoptions.get(id));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    long partner1 = Math.min(this.partner1, this.partner2);
    long partner2 = Math.max(this.partner1, this.partner2);

    result = prime * result + this.adoptions.hashCode();
    result = prime * result + this.children.hashCode();
    result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
    result = prime * result + ((this.endDate == null) ? 0 : this.endDate.hashCode());
    result = prime * result + (this.hasEnded ? 1231 : 1237);
    result = prime * result + (this.isWedding ? 1231 : 1237);
    result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
    result = prime * result + (int) (partner1 ^ (partner1 >>> 32));
    result = prime * result + (int) (partner2 ^ (partner2 >>> 32));

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
    Relationship other = (Relationship) obj;
    if (this.adoptions == null) {
      if (other.adoptions != null)
        return false;
    }
    else if (!this.adoptions.equals(other.adoptions))
      return false;
    if (this.children == null) {
      if (other.children != null)
        return false;
    }
    else if (!this.children.equals(other.children))
      return false;
    if (this.date == null) {
      if (other.date != null)
        return false;
    }
    else if (!this.date.equals(other.date))
      return false;
    if (this.endDate == null) {
      if (other.endDate != null)
        return false;
    }
    else if (!this.endDate.equals(other.endDate))
      return false;
    if (this.hasEnded != other.hasEnded)
      return false;
    if (this.isWedding != other.isWedding)
      return false;
    if (this.location == null) {
      if (other.location != null)
        return false;
    }
    else if (!this.location.equals(other.location))
      return false;
    if (!(this.partner1 == other.partner1 && this.partner2 == other.partner2
        || this.partner1 == other.partner2 && this.partner2 == other.partner1))
      return false;
    return true;
  }

  @Override
  public Relationship clone() {
    try {
      Relationship r = (Relationship) super.clone();

      r.children = getChildren();
      r.adoptions = new HashMap<>(this.adoptions);

      return r;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public int compareTo(Relationship r) {
    if (getDate().isPresent() && r.getDate().isPresent()) {
      return getDate().get().compareTo(r.getDate().get());
    }
    else if (getEndDate().isPresent() && r.getEndDate().isPresent()) {
      return getEndDate().get().compareTo(r.getEndDate().get());
    }
    return 0;
  }

  @Override
  public String toString() {
    return getPartner1() + " <-> " + getPartner2();
  }
}
