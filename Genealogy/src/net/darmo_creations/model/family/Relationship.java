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
package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import net.darmo_creations.model.Date;
import net.darmo_creations.util.Nullable;

/**
 * This class represents a relationship between two people.
 * 
 * @author Damien Vergnet
 */
public final class Relationship {
  private Date date;
  private String location;
  private FamilyMember partner1, partner2;
  private Set<FamilyMember> children;
  private boolean isWedding;

  /**
   * Creates a new wedding. The two spouses must be different or else an IllegalArgumentException
   * will be thrown.
   * 
   * @param date the date
   * @param location the location
   * @param partner1 one partner
   * @param partner2 the other partner
   * @param children the children
   */
  public Relationship(@Nullable Date date, @Nullable String location, boolean isWedding, FamilyMember partner1, FamilyMember partner2,
      FamilyMember... children) {
    if (partner1.equals(partner2))
      throw new IllegalArgumentException("partners must be different");
    setDate(date != null ? date.clone() : null);
    setLocation(location);
    setPartner1(partner1);
    setPartner2(partner2);
    this.children = new HashSet<>();
    for (FamilyMember child : children)
      addChild(child);
  }

  /**
   * @return the wedding's date
   */
  public Optional<Date> getDate() {
    return Optional.ofNullable(this.date);
  }

  /**
   * Sets the wedding's date. May be null.
   * 
   * @param date the new date
   */
  void setDate(@Nullable Date date) {
    this.date = date;
  }

  /**
   * @return the wedding's location
   */
  public Optional<String> getLocation() {
    return Optional.ofNullable(this.location);
  }

  /**
   * Sets the wedding's location. May be null.
   * 
   * @param location the new location
   */
  void setLocation(@Nullable String location) {
    this.location = location;
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
   */
  public void setWedding(boolean isWedding) {
    this.isWedding = isWedding;
  }

  /**
   * @return the first partner
   */
  public FamilyMember getPartner1() {
    return this.partner1;
  }

  /**
   * Sets the first partner.
   * 
   * @param partner1 the new partner
   */
  void setPartner1(FamilyMember partner1) {
    this.partner1 = partner1;
  }

  /**
   * @return the second partner
   */
  public FamilyMember getPartner2() {
    return this.partner2;
  }

  /**
   * Sets the second partner.
   * 
   * @param partner2 the second partner
   */
  void setPartner2(FamilyMember partner2) {
    this.partner2 = partner2;
  }

  /**
   * Tells if the given person is a child from this wedding.
   * 
   * @param member the person
   * @return true if and only if the person is a child from this wedding
   */
  public boolean isChild(FamilyMember member) {
    return this.children.contains(member);
  }

  /**
   * @return a set of all the children
   */
  public Set<FamilyMember> getChildren() {
    return new TreeSet<>(this.children);
  }

  /**
   * Adds a child to this wedding. The child must be different from the two spouses and not already
   * present in the children list.
   * 
   * @param child the child to add
   */
  public void addChild(FamilyMember child) {
    Objects.requireNonNull(child);
    if (child.equals(getPartner1()) || child.equals(getPartner2()))
      throw new IllegalArgumentException("can't be their own child");
    if (this.children.contains(child.getId()))
      throw new IllegalArgumentException("child already present");
    this.children.add(child);
  }

  /**
   * Deletes the given child.
   * 
   * @param child the child to delete
   */
  public void removeChild(FamilyMember child) {
    this.children.remove(child);
  }

  /**
   * Tells if the given person is one of the two partners from this relationship.
   * 
   * @param member the person
   * @return true if and only if the person is one of the partners
   */
  public boolean isInRelationship(FamilyMember member) {
    return getPartner1().equals(member) || getPartner2().equals(member);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + getPartner1().hashCode();
    result = prime * result + getPartner2().hashCode();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Relationship) {
      Relationship relation = (Relationship) o;
      return getPartner1() == relation.getPartner1() && getPartner2() == relation.getPartner2();
    }
    return false;
  }

  @Override
  public String toString() {
    return getPartner1() + " <-> " + getPartner2();
  }
}
