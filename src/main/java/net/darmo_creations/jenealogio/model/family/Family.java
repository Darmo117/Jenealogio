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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A family has members and each member can be in relationships.
 * 
 * @author Damien Vergnet
 */
public class Family implements Cloneable {
  /** The global ID */
  private long globalId;
  /** This family's name */
  private String name;
  /** Members */
  private Set<FamilyMember> members;
  /** Relationships */
  private Set<Relationship> relations;

  /**
   * Creates a family with no members and no relations.
   * 
   * @param name family's name
   */
  public Family(String name) {
    this(0, name, new HashSet<>(), new HashSet<>());
  }

  /**
   * Creates a family with the given members and relations.
   * 
   * @param globalId global ID's initial value
   * @param name family's name
   * @param members the members
   * @param relations the relations
   */
  public Family(long globalId, String name, Set<FamilyMember> members, Set<Relationship> relations) {
    this.globalId = globalId;
    setName(name);
    this.members = Objects.requireNonNull(members);
    this.relations = Objects.requireNonNull(relations);
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
    return this.members.stream().map(member -> member.clone()).collect(Collectors.toSet());
  }

  /**
   * Gets the member with the given ID.
   * 
   * @param id the ID
   * @return the member or nothing if none were found
   */
  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny().map(member -> member.clone());
  }

  /**
   * Adds a member to the family. If the given member already exist in this family, nothing will
   * happen.
   * 
   * @param member the new member
   */
  public void addMember(FamilyMember member) {
    this.members.add(member.clone(getNextMemberId()));
  }

  /**
   * Updates an existing member. If the member does not exist in this family, nothing will happen.
   * 
   * @param member the member's updated data
   */
  public void updateMember(FamilyMember member) {
    Predicate<FamilyMember> p = m -> m.getId() == member.getId();

    if (this.members.stream().anyMatch(p)) {
      this.members.removeIf(p);
      this.members.add(member.clone());
    }
  }

  /**
   * Deletes a member from the family. Also removes associated relation. If the member does not
   * exist in this family, nothing will happen.
   * 
   * @param id the ID of the member to remove
   */
  public void removeMember(long id) {
    for (Iterator<Relationship> it = this.relations.iterator(); it.hasNext();) {
      Relationship relation = it.next();
      if (relation.isInRelationship(id))
        it.remove();
      else if (relation.isChild(id))
        relation.removeChild(id);
    }
    this.members.removeIf(m -> m.getId() == id);
  }

  /**
   * Returns all the relations. The set returned is a copy of the internal one.
   * 
   * @return all the relations
   */
  public Set<Relationship> getAllRelations() {
    return this.relations.stream().map(r -> r.clone()).collect(Collectors.toSet());
  }

  /**
   * Gets the relation for the given members.
   * 
   * @param id1 the first ID
   * @param id2 the second ID
   * @return the relationship between the two IDs
   */
  public Optional<Relationship> getRelation(long id1, long id2) {
    return this.relations.stream().filter(r -> r.isInRelationship(id1) && r.isInRelationship(id2)).findAny().map(
        relation -> relation.clone());
  }

  /**
   * Gets the relations for the given member. This method checks for every relation if one of the
   * two partners is the given member.
   * 
   * @param memberId the member's ID
   * @return the relations
   */
  public Set<Relationship> getRelations(long memberId) {
    return this.relations.stream().filter(relation -> relation.isInRelationship(memberId)).map(relation -> relation.clone()).collect(
        Collectors.toSet());
  }

  /**
   * Adds a relation. If one of the two spouses is already married or the relation already exists,
   * nothing will happen.
   * 
   * @param relation the new relation
   */
  public void addRelation(Relationship relation) {
    if (!areInRelationship(relation.getPartner1(), relation.getPartner2())) {
      this.relations.add(relation.clone());
    }
  }

  /**
   * Updates the given relation. If the relation does not exist in this family, nothing will happen.
   * 
   * @param relation the relation's new data
   */
  public void updateRelation(Relationship relation) {
    Predicate<Relationship> p = r -> r.isInRelationship(relation.getPartner1()) && r.isInRelationship(relation.getPartner2());

    if (this.relations.stream().anyMatch(p)) {
      relation.getChildren().forEach(id -> {
        if (!getMember(id).isPresent())
          throw new IllegalStateException("member ID '" + id + "' does not exist");
      });
      this.relations.removeIf(p);
      this.relations.add(relation.clone());
    }
  }

  /**
   * Deletes a relation. If the relation does not exist in this family, nothing will happen.
   * 
   * @param relation the relation to delete
   */
  public void removeRelationship(Relationship relation) {
    this.relations.removeIf(r -> r.isInRelationship(relation.getPartner1()) && r.isInRelationship(relation.getPartner2()));
  }

  /**
   * Tells if two members are in a relationship.
   * 
   * @param id1 first member ID
   * @param id2 second member ID
   * @return true if and only if they are in a relationship
   */
  public boolean areInRelationship(long id1, long id2) {
    return this.relations.stream().anyMatch(relation -> relation.isInRelationship(id1) && relation.isInRelationship(id2));
  }

  /**
   * Tells if a member has any known parent.
   * 
   * @param memberId member's ID
   * @return true if and only if the member has known parents
   */
  public boolean hasParents(long memberId) {
    return this.relations.stream().anyMatch(r -> r.getChildren().contains(memberId));
  }

  /**
   * Returns all members that can be children of the given couple. If the argument is null, all
   * members are returned.
   * 
   * @param relation the couple
   * @return a list of all potential children
   */
  public Set<FamilyMember> getPotentialChildren(Relationship relation) {
    if (relation == null)
      return getAllMembers();
    return getPotentialChildren(getMember(relation.getPartner1()).get(), getMember(relation.getPartner2()).get(), relation.getChildren());
  }

  /**
   * Returns all members that can be children of the given couple. If one of the partners is null,
   * all members are returned.
   * 
   * @param partner1 the first partner
   * @param partner2 the second partner
   * @param children the children
   * @return a list of potential children
   */
  public Set<FamilyMember> getPotentialChildren(FamilyMember partner1, FamilyMember partner2, Set<Long> children) {
    Set<FamilyMember> all = getAllMembers();

    if (partner1 == null || partner2 == null)
      return all;

    all.removeIf(m -> m.getId() == partner1.getId());
    all.removeIf(m -> m.getId() == partner2.getId());

    if (partner1.getBirthDate().isPresent() || partner2.getBirthDate().isPresent()) {
      // Filter out all members that are older than the youngest spouse.
      FamilyMember youngest = null;

      if (partner1.getBirthDate().isPresent() && partner2.getBirthDate().isPresent()) {
        youngest = partner1.compareBirthdays(partner2).get() > 0 ? partner1 : partner2;
      }
      else if (partner1.getBirthDate().isPresent()) {
        youngest = partner1;
      }
      else {
        youngest = partner2;
      }
      final FamilyMember y = youngest;
      all.removeIf(m -> m.compareBirthdays(y).orElse(1) <= 0);
    }

    all.removeIf(m -> children.contains(m.getId()) || hasParents(m.getId()));

    return all;
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
    return getName() + this.members + "," + this.relations;
  }

  @Override
  public Family clone() {
    return new Family(this.globalId, this.name, getAllMembers(), getAllRelations());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (int) (this.globalId ^ (this.globalId >>> 32));
    result = prime * result + ((this.members == null) ? 0 : this.members.hashCode());
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.relations == null) ? 0 : this.relations.hashCode());

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
    Family other = (Family) obj;
    if (this.globalId != other.globalId)
      return false;
    if (this.members == null) {
      if (other.members != null)
        return false;
    }
    else if (!this.members.equals(other.members))
      return false;
    if (this.name == null) {
      if (other.name != null)
        return false;
    }
    else if (!this.name.equals(other.name))
      return false;
    if (this.relations == null) {
      if (other.relations != null)
        return false;
    }
    else if (!this.relations.equals(other.relations))
      return false;
    return true;
  }
}