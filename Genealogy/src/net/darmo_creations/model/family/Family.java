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
  // TODO to refactor
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
   * Deletes a member from the family. Also removes associated relation. If the member does not
   * exist in this family, nothing will happen.
   * 
   * @param member the member to remove
   */
  public void removeMember(FamilyMember member) {
    for (Iterator<Relationship> it = this.relations.iterator(); it.hasNext();) {
      Relationship relation = it.next();
      if (relation.isInRelationship(member))
        it.remove();
      else if (relation.isChild(member))
        relation.removeChild(member);
    }
    this.members.remove(member);
  }

  /**
   * Returns all the relations. The set returned is a copy of the internal one.
   * 
   * @return all the relations
   */
  public Set<Relationship> getAllRelations() {
    return new HashSet<>(this.relations);
  }

  /**
   * Gets the relation for the given member. This method checks for every relation if one of the two
   * spouses is the given member.
   * 
   * @param member the member
   * @return the relation or nothing if none were found
   */
  public Optional<Relationship> getRelation(FamilyMember member) {
    return this.relations.stream().filter(
        relation -> relation.getPartner1().equals(member) || relation.getPartner2().equals(member)).findAny();
  }

  /**
   * Adds a relation. If one of the two spouses is already married or the relation already exists,
   * nothing will happen.
   * 
   * @param relation the new relation
   */
  // TODO to refactor
  public void addRelation(Relationship relation) {
    // TODO : accepter les remariages.
    if (!this.relations.contains(relation) && !isInRelationship(relation.getPartner1()) && !isInRelationship(relation.getPartner2())) {
      FamilyMember[] children = relation.getChildren().stream().map(child -> getMember(child.getId()).get()).toArray(FamilyMember[]::new);

      this.relations.add(new Relationship(relation.getDate().orElse(null), relation.getLocation().orElse(null), relation.isWedding(),
          relation.hasEnded(), relation.getEndDate().orElse(null), getMember(relation.getPartner1().getId()).get(),
          getMember(relation.getPartner2().getId()).get(), children));
    }
  }

  /**
   * Updates the given relation. If the relation does not exist in this family, nothing will happen.
   * 
   * @param relation the relation's new data
   */
  // TODO to refactor
  public void updateRelation(Relationship relation) {
    Optional<Relationship> optional = getRelation(relation.getPartner1());

    if (optional.isPresent()) {
      Relationship r = optional.get();

      r.setDate(relation.getDate().orElse(null));
      r.setLocation(relation.getLocation().orElse(null));
      r.setWedding(relation.isWedding());
      r.setEndDate(relation.getEndDate().orElse(null));
      r.setHasEnded(relation.hasEnded());
      Set<FamilyMember> children = r.getChildren();
      children.forEach(child -> r.removeChild(child));
      relation.getChildren().forEach(child -> r.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  /**
   * Deletes a relation. If the relation does not exist in this family, nothing will happen.
   * 
   * @param relation the relation to delete
   */
  public void removeRelationship(Relationship relation) {
    this.relations.remove(relation);
  }

  /**
   * Tells if a member is married.
   * 
   * @param member the member
   * @return true if and only if the member is married
   */
  public boolean isInRelationship(FamilyMember member) {
    return this.relations.stream().anyMatch(relation -> relation.isInRelationship(member));
  }

  /**
   * Tells if a member has known parents.
   * 
   * @param member the member
   * @return true if and only if the member has known parents
   */
  public boolean hasParents(FamilyMember member) {
    return this.relations.stream().anyMatch(w -> w.getChildren().contains(member));
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
    return getPotentialChildren(relation.getPartner1(), relation.getPartner2(), relation.getChildren());
  }

  /**
   * Returns all members that can be children of the given couple. If one of the partners is null,
   * all members are returned.
   * 
   * @param partner1 the first partner
   * @param partner2 the second partner
   * @param chidren the children
   * @return a list of potential children
   */
  public Set<FamilyMember> getPotentialChildren(FamilyMember partner1, FamilyMember partner2, Set<FamilyMember> chidren) {
    Set<FamilyMember> all = getAllMembers();

    if (partner1 == null || partner2 == null)
      return all;

    all.remove(partner1);
    all.remove(partner2);

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

    all.removeIf(m -> chidren.contains(m) || hasParents(m));

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
}