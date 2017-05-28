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
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A family has members and each member can be married. The update methods do not use directly the
 * provided arguments but rather copy their fields to keep the family consistent.
 * 
 * @author Damien Vergnet
 */
public class Family {
  /** The global ID */
  private long globalId;
  /** This family's name */
  private String name;
  /** Members */
  private Set<FamilyMember> members;
  /** Weddings */
  private Set<Wedding> weddings;

  /**
   * Creates a family with no members and no weddings.
   * 
   * @param name family's name
   */
  public Family(String name) {
    this(0, name, new HashSet<>(), new HashSet<>());
  }

  /**
   * Creates a family with the given members and weddings.
   * 
   * @param globalId global ID's initial value
   * @param name family's name
   * @param members the members
   * @param weddings the weddings
   */
  public Family(long globalId, String name, Set<FamilyMember> members, Set<Wedding> weddings) {
    this.globalId = globalId;
    setName(name);
    this.members = Objects.requireNonNull(members);
    this.weddings = Objects.requireNonNull(weddings);
  }

  /**
   * @return this family's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the family name.
   * 
   * @param name the new name
   */
  public void setName(String name) {
    this.name = Objects.requireNonNull(name);
  }

  /**
   * Returns all the members. The set returned is a copy of the internal one.
   * 
   * @return all the members
   */
  public Set<FamilyMember> getAllMembers() {
    return new HashSet<>(this.members);
  }

  /**
   * Gets the member with the given ID.
   * 
   * @param id the ID
   * @return the member or nothing if none were found
   */
  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny();
  }

  /**
   * Adds a member to the family. If the given member already exist in this family, nothing will
   * happen.
   * 
   * @param member the new member
   */
  public void addMember(FamilyMember member) {
    if (!this.members.contains(member)) {
      this.members.add(member.copy(getNextMemberId()));
    }
  }

  /**
   * Updates an existing member. If the member does not exist in this family, nothing will happen.
   * 
   * @param member the member's updated data
   */
  public void updateMember(FamilyMember member) {
    Optional<FamilyMember> optional = getMember(member.getId());

    if (optional.isPresent()) {
      FamilyMember m = optional.get();

      m.setImage(member.getImage().orElse(null));
      m.setFamilyName(member.getFamilyName().orElse(null));
      m.setUseName(member.getUseName().orElse(null));
      m.setFirstName(member.getFirstName().orElse(null));
      m.setOtherNames(member.getOtherNames().orElse(null));
      m.setGender(member.getGender());
      m.setBirthDate(member.getBirthDate().orElse(null));
      m.setBirthLocation(member.getBirthLocation().orElse(null));
      m.setDeathDate(member.getDeathDate().orElse(null));
      m.setDeathLocation(member.getDeathLocation().orElse(null));
      m.setDead(member.isDead());
      m.setComment(member.getComment().orElse(null));
    }
  }

  /**
   * Deletes a member from the family. Also removes associated wedding. If the member does not exist
   * in this family, nothing will happen.
   * 
   * @param member the member to remove
   */
  public void removeMember(FamilyMember member) {
    for (Iterator<Wedding> it = this.weddings.iterator(); it.hasNext();) {
      Wedding wedding = it.next();
      if (wedding.isMarried(member))
        it.remove();
      else if (wedding.isChild(member))
        wedding.removeChild(member);
    }
    this.members.remove(member);
  }

  /**
   * Returns all the weddings. The set returned is a copy of the internal one.
   * 
   * @return all the weddings
   */
  public Set<Wedding> getAllWeddings() {
    return new HashSet<>(this.weddings);
  }

  /**
   * Gets the wedding for the given member. This method checks for every wedding if one of the two
   * spouses is the given member.
   * 
   * @param member the member
   * @return the wedding or nothing if none were found
   */
  public Optional<Wedding> getWedding(FamilyMember member) {
    return this.weddings.stream().filter(wedding -> wedding.getSpouse1().equals(member) || wedding.getSpouse2().equals(member)).findAny();
  }

  /**
   * Adds a wedding. If one of the two spouses is already married or the wedding already exists,
   * nothing will happen.
   * 
   * @param wedding the new wedding
   */
  public void addWedding(Wedding wedding) {
    if (!this.weddings.contains(wedding) && !isMarried(wedding.getSpouse1()) && !isMarried(wedding.getSpouse2())) {
      // #f:0
      FamilyMember[] children = wedding.getChildren().stream()
          .map(child -> getMember(child.getId()).get())
          .toArray(FamilyMember[]::new);

      this.weddings.add(new Wedding(wedding.getDate().orElse(null), wedding.getLocation().orElse(null),
          getMember(wedding.getSpouse1().getId()).get(), getMember(wedding.getSpouse2().getId()).get(),
          children));
      // #f:1
    }
  }

  /**
   * Updates the given wedding. If the wedding does not exist in this family, nothing will happen.
   * 
   * @param wedding the wedding's new data
   */
  public void updateWedding(Wedding wedding) {
    Optional<Wedding> optional = getWedding(wedding.getSpouse1());

    if (optional.isPresent()) {
      Wedding w = optional.get();

      w.setDate(wedding.getDate().orElse(null));
      w.setLocation(wedding.getLocation().orElse(null));
      Set<FamilyMember> children = w.getChildren();
      children.forEach(child -> w.removeChild(child));
      wedding.getChildren().forEach(child -> w.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  /**
   * Deletes a wedding. If the wedding does not exist in this family, nothing will happen.
   * 
   * @param wedding the wedding to delete
   */
  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  /**
   * Tells if a member is married.
   * 
   * @param member the member
   * @return true if and only if the member is married
   */
  public boolean isMarried(FamilyMember member) {
    return this.weddings.stream().anyMatch(wedding -> wedding.isMarried(member));
  }

  /**
   * Returns all members that can be children of the given couple. If the argument is null, all
   * members are returned.
   * 
   * @param wedding the couple
   * @return a list of all potential children
   */
  public Set<FamilyMember> getPotentialChildren(Wedding wedding) {
    if (wedding == null)
      return getAllMembers();
    // #f:0
    return getAllMembers().stream()
        .filter(m -> !m.equals(wedding.getSpouse1()) && !m.equals(wedding.getSpouse2()) && !wedding.isChild(m))
        .collect(Collectors.toCollection(HashSet::new));
    // #f:1
  }

  /**
   * @return the global member ID
   */
  public long getGlobalId() {
    return this.globalId;
  }

  /**
   * @return the next member ID
   */
  private long getNextMemberId() {
    return this.globalId++;
  }

  @Override
  public String toString() {
    return getName() + this.members + "," + this.weddings;
  }
}