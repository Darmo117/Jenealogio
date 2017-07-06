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

import net.darmo_creations.model.date.Date;
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
  private boolean hasEnded;
  private Date endDate;

  /**
   * Creates a new relation. The two partners must be different or else an IllegalArgumentException
   * will be thrown.
   * 
   * @param date the start date
   * @param location the location when it started
   * @param isWedding is it a wedding?
   * @param hasEnded has it ended?
   * @param endDate the date when it ended
   * @param partner1 one partner
   * @param partner2 the other partner
   * @param children the children
   */
  public Relationship(@Nullable Date date, @Nullable String location, boolean isWedding, boolean hasEnded, @Nullable Date endDate,
      FamilyMember partner1, FamilyMember partner2, FamilyMember... children) {
    if (partner1.equals(partner2))
      throw new IllegalArgumentException("partners must be different");
    setDate(date != null ? date.clone() : null);
    setLocation(location);
    setWedding(isWedding);
    setPartner1(partner1);
    setPartner2(partner2);
    setEndDate(endDate != null ? endDate.clone() : null);
    setHasEnded(hasEnded);
    this.children = new HashSet<>();
    for (FamilyMember child : children)
      addChild(child);
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
   */
  void setDate(@Nullable Date date) {
    this.date = date;
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
  void setWedding(boolean isWedding) {
    this.isWedding = isWedding;
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
   */
  void setHasEnded(boolean hasEnded) {
    if (this.endDate == null)
      this.hasEnded = hasEnded;
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
   */
  void setEndDate(@Nullable Date endDate) {
    this.hasEnded = endDate != null;
    this.endDate = endDate;
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
   * Adds a child to this relation. The child must be different from the two spouses and not already
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
