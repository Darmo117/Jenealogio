/*
 * Copyright © 2018 Damien Vergnet
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

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.date.Date;

public class FamilyTest {
  private Family family, family2;

  @Before
  public void setUp() throws Exception {
    this.family = new Family("test");
    Set<FamilyMember> members = new HashSet<>();
    members.add(new FamilyMemberMock().withId(0));
    members.add(new FamilyMemberMock().withId(1));
    members.add(new FamilyMemberMock().withId(2));
    members.add(new FamilyMemberMock().withId(3));
    Relationship r = new Relationship(0, 1);
    r.addChild(3);
    this.family2 = new Family(0, "test2", members, Collections.singleton(r));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitNullName() {
    new Family(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitNulEmptyName() {
    new Family("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitBlankName() {
    new Family(" ");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNullName() {
    this.family.setName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetNulEmptyName() {
    this.family.setName("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetBlankName() {
    this.family.setName(" ");
  }

  @Test
  public void testMembersSetNotSame() {
    Set<FamilyMember> members = Collections.emptySet();
    Family f = new Family(0, "test", members, Collections.emptySet());
    assertNotSame(members, f.getAllMembers());
    assertEquals(members, f.getAllMembers());
  }

  @Test
  public void testRelationsSetNotSame() {
    Set<Relationship> relations = Collections.emptySet();
    Family f = new Family(0, "test", Collections.emptySet(), relations);
    assertNotSame(relations, f.getAllRelations());
    assertEquals(relations, f.getAllRelations());
  }

  @Test
  public void testGetAllMembersModificationNoSideEffects() {
    this.family.addMember(new FamilyMemberMock());
    this.family.getAllMembers().clear();
    assertEquals(1, this.family.getAllMembers().size());
  }

  @Test
  public void testGetAllRelationsModificationNoSideEffects() {
    this.family2.getAllRelations().clear();
    assertEquals(1, this.family2.getAllRelations().size());
  }

  @Test
  public void testGetMemberId() {
    this.family.addMember(new FamilyMemberMock());
    assertTrue(this.family.getMember(0).isPresent());
  }

  @Test
  public void testMemberEqualsButNotSame() {
    FamilyMember m = new FamilyMemberMock().withId(0);
    this.family.addMember(m);
    assertNotSame(m, this.family.getMember(0).get());
    assertEquals(m, this.family.getMember(0).get());
  }

  @Test
  public void testAddMember() {
    this.family.addMember(new FamilyMemberMock());
    assertEquals(1, this.family.getAllMembers().size());
  }

  @Test
  public void testAddMemberIgnoresId() {
    this.family.addMember(new FamilyMemberMock().withId(0));
    this.family.addMember(new FamilyMemberMock().withId(0));
    assertEquals(2, this.family.getAllMembers().size());
  }

  @Test
  public void testUpdateMember() {
    FamilyMember m = new FamilyMemberMock();
    this.family.addMember(m);
    m = m.withId(0);
    m.setFamilyName("Mick");
    this.family.updateMember(m);
    assertEquals("Mick", this.family.getMember(0).get().getFamilyName().get());
  }

  @Test(expected = NoSuchElementException.class)
  public void updateNonExistantMember() {
    this.family.updateMember(new FamilyMemberMock().withId(0));
  }

  @Test
  public void testRemoveMember() {
    this.family2.removeMember(2);
    assertEquals(3, this.family2.getAllMembers().size());
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveMemberNotExists() {
    this.family2.removeMember(99);
  }

  @Test
  public void testRemoveMemberInRelationship() {
    this.family2.removeMember(0);
    assertEquals(0, this.family2.getAllRelations().size());
  }

  @Test
  public void testRemoveMemberChild() {
    this.family2.removeMember(3);
    assertEquals(1, this.family2.getAllRelations().size());
  }

  @Test
  public void testRemoveMemberChildUpdatesRelation() {
    this.family2.removeMember(3);
    assertFalse(this.family2.getRelation(0, 1).get().isChild(3));
  }

  @Test
  public void testRelationEqualsButNotSame() {
    Relationship r = new Relationship(2, 3);
    this.family2.addRelation(r);
    assertNotSame(r, this.family2.getRelation(2, 3).get());
    assertEquals(r, this.family2.getRelation(2, 3).get());
  }

  @Test
  public void testGetRelationIdsSwapped() {
    assertTrue(this.family2.getRelation(1, 0).isPresent());
  }

  @Test
  public void testAddRelation() {
    Relationship r = new Relationship(2, 3);
    this.family2.addRelation(r);
    assertEquals(2, this.family2.getAllRelations().size());
  }

  @Test(expected = NoSuchElementException.class)
  public void testAddRelationWithWrongParentIdError() {
    this.family2.addRelation(new Relationship(0, 10));
  }

  @Test(expected = NoSuchElementException.class)
  public void testAddRelationWithWrongChildIdError() {
    Relationship r = new Relationship(0, 1);
    r.addChild(6);
    this.family2.addRelation(r);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRelationWithSameParentIdsError() {
    this.family2.addRelation(new Relationship(0, 1));
  }

  @Test
  public void testUpdateRelation() {
    Relationship r = this.family2.getRelation(0, 1).get();
    r.setDate(Date.getDate(2010, 1, 1));
    this.family2.updateRelation(r);
    assertEquals(r, this.family2.getRelation(0, 1).get());
  }

  @Test
  public void testUpdateRelationIdsSwapped() {
    this.family2.addRelation(new Relationship(2, 3));
    Relationship r = new Relationship(3, 2).setDate(Date.getDate(2010, 1, 1));
    this.family2.updateRelation(r);
    assertEquals(r, this.family2.getRelation(2, 3).get());
  }

  @Test(expected = NoSuchElementException.class)
  public void updateNonExistingRelation() {
    this.family2.updateRelation(new Relationship(2, 3));
  }

  @Test(expected = NoSuchElementException.class)
  public void testUpdateRelationWithWrongChildIdError() {
    Relationship r = this.family2.getRelation(0, 1).get();
    r.addChild(6);
    this.family2.updateRelation(r);
  }

  @Test
  public void testRemoveRelation() {
    this.family2.removeRelation(0, 1);
    assertFalse(this.family2.getRelation(0, 1).isPresent());
  }

  @Test
  public void testRemoveRelationIdsSwapped() {
    this.family2.removeRelation(1, 0);
    assertFalse(this.family2.getRelation(0, 1).isPresent());
  }

  @Test
  public void testAreInRelation() {
    assertTrue(this.family2.areInRelation(0, 1));
  }

  @Test
  public void testAreInRelationIdsSwapped() {
    assertTrue(this.family2.areInRelation(1, 0));
  }

  @Test
  public void testAreNotInRelation() {
    assertFalse(this.family2.areInRelation(0, 3));
  }

  @Test
  public void testHasParents() {
    assertTrue(this.family2.hasParents(3));
  }

  @Test
  public void testHasNoParents() {
    assertFalse(this.family2.hasParents(0));
  }

  @Test
  public void testGlobalId() {
    this.family.addMember(new FamilyMemberMock());
    assertEquals(1, this.family.getGlobalId());
  }

  @Test
  public void testGetPotentialChildren_Relation() {
    assertEquals(Collections.singleton(this.family2.getMember(2).get()),
        this.family2.getPotentialChildren(this.family2.getRelation(0, 1).get()));
  }

  @Test
  public void testGetPotentialChildren_ParentsAndChildren() {
    assertEquals(Collections.singleton(this.family2.getMember(2).get()),
        this.family2.getPotentialChildren(this.family2.getMember(0).get(), this.family2.getMember(1).get(), Collections.singleton(3L)));
  }

  @Test
  public void testClone() {
    assertEquals(this.family2, this.family2.clone());
  }

  @Test
  public void testHashcode() {
    assertEquals(new Family("test").hashCode(), this.family.hashCode());
  }

  @Test
  public void testEquals() {
    assertEquals(new Family("test"), this.family);
  }
}
