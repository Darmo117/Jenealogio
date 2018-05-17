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
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.date.Date;

public class RelationshipTest {
  private Relationship relation;

  @Before
  public void setUp() {
    this.relation = new Relationship(1, 2);
  }

  @Test
  public void testNullLocation() {
    this.relation.setLocation(null);
    assertFalse(this.relation.getLocation().isPresent());
  }

  @Test
  public void testEmptyLocation() {
    this.relation.setLocation("");
    assertFalse(this.relation.getLocation().isPresent());
  }

  @Test
  public void testBlankLocation() {
    this.relation.setLocation(" ");
    assertFalse(this.relation.getLocation().isPresent());
  }

  @Test
  public void testHasEnded() {
    this.relation.setHasEnded(true);
    assertTrue(this.relation.hasEnded());
  }

  @Test
  public void testHasEnded2() {
    this.relation.setHasEnded(false);
    this.relation.setEndDate(Date.getDate(2000, 1, 1));
    assertTrue(this.relation.hasEnded());
  }

  @Test
  public void testHasNotEndedNoEffect() {
    this.relation.setEndDate(Date.getDate(2000, 1, 1));
    this.relation.setHasEnded(false);
    assertTrue(this.relation.hasEnded());
  }

  @Test
  public void testAddChild() {
    this.relation.addChild(3);
    assertEquals(Collections.singleton(3L), this.relation.getChildren());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddParent1AsChild() {
    this.relation.addChild(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddParent2AsChild() {
    this.relation.addChild(2);
  }

  @Test
  public void testSetAdopted() {
    this.relation.addChild(3);
    this.relation.setAdopted(3, Date.getDate(2000, 1, 1));
    assertTrue(this.relation.isAdopted(3));
  }

  @Test(expected = NoSuchElementException.class)
  public void testSetAdoptedNoChild() {
    this.relation.setAdopted(3, Date.getDate(2000, 1, 1));
  }

  @Test
  public void testGetAdoptionDate() {
    this.relation.addChild(3);
    this.relation.setAdopted(3, Date.getDate(2000, 1, 1));
    assertEquals(Date.getDate(2000, 1, 1), this.relation.getAdoptionDate(3).get());
  }

  @Test
  public void testSetNotAdopted() {
    this.relation.addChild(3);
    this.relation.setAdopted(3, Date.getDate(2000, 1, 1));
    this.relation.setNotAdopted(3);
    assertFalse(this.relation.isAdopted(3));
  }

  @Test(expected = NoSuchElementException.class)
  public void testNotAdoptedNoChild() {
    this.relation.setNotAdopted(3);
  }

  @Test
  public void testRemoveChild() {
    this.relation.addChild(3);
    this.relation.removeChild(3);
    assertTrue(this.relation.getChildren().isEmpty());
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveChildNotPresent() {
    this.relation.removeChild(3);
    assertTrue(this.relation.getChildren().isEmpty());
  }

  @Test
  public void testIsInRelationship1() {
    assertTrue(this.relation.isInRelationship(1));
  }

  @Test
  public void testIsInRelationship2() {
    assertTrue(this.relation.isInRelationship(2));
  }

  @Test
  public void testIsNotInRelationship() {
    assertFalse(this.relation.isInRelationship(3));
  }

  @Test
  public void testEquals() {
    Relationship r = new Relationship(1, 2);
    assertEquals(r, this.relation);
  }

  @Test
  public void testEqualsParentsSwapped() {
    Relationship r1 = new Relationship(1, 2);
    Relationship r2 = new Relationship(2, 1);
    assertEquals(r1, r2);
  }

  @Test
  public void testEqualsWithChildren() {
    this.relation.addChild(3);
    Relationship r = new Relationship(1, 2);
    r.addChild(3);
    assertEquals(r, this.relation);
  }

  @Test
  public void testEqualsWithAdoptions() {
    this.relation.addChild(3);
    this.relation.setAdopted(3, Date.getDate(2000, 1, 1));
    Relationship r = new Relationship(1, 2);
    r.addChild(3);
    r.setAdopted(3, Date.getDate(2000, 1, 1));
    assertEquals(r, this.relation);
  }

  @Test
  public void testHashcode() {
    Relationship r = new Relationship(1, 2);
    assertEquals(r.hashCode(), this.relation.hashCode());
  }

  @Test
  public void testHashcodeParentsSwapped() {
    Relationship r = new Relationship(2, 1);
    assertEquals(r.hashCode(), this.relation.hashCode());
  }

  @Test
  public void testClone() {
    assertEquals(this.relation, this.relation.clone());
  }

  @Test
  public void testCompareDateBefore() {
    Relationship r = new Relationship(1, 2).setDate(Date.getDate(2001, 1, 1));
    this.relation.setDate(Date.getDate(2000, 1, 1));
    assertTrue(this.relation.compareTo(r) < 0);
  }

  @Test
  public void testCompareDateAfter() {
    Relationship r = new Relationship(1, 2).setDate(Date.getDate(1999, 1, 1));
    this.relation.setDate(Date.getDate(2000, 1, 1));
    assertTrue(this.relation.compareTo(r) > 0);
  }

  @Test
  public void testCompareDateSame() {
    Relationship r = new Relationship(1, 2).setDate(Date.getDate(2000, 1, 1));
    this.relation.setDate(Date.getDate(2000, 1, 1));
    assertEquals(0, this.relation.compareTo(r));
  }

  @Test
  public void testCompareDateNull() {
    Relationship r = new Relationship(1, 2).setDate(Date.getDate(2000, 1, 1));
    assertEquals(0, this.relation.compareTo(r));
  }

  @Test
  public void testCompareDateBothNull() {
    assertEquals(0, this.relation.compareTo(new Relationship(1, 2)));
  }

  @Test
  public void testCompareEndDateBefore() {
    Relationship r = new Relationship(1, 2).setEndDate(Date.getDate(2001, 1, 1));
    this.relation.setEndDate(Date.getDate(2000, 1, 1));
    assertTrue(this.relation.compareTo(r) < 0);
  }

  @Test
  public void testCompareEndDateAfter() {
    Relationship r = new Relationship(1, 2).setEndDate(Date.getDate(1999, 1, 1));
    this.relation.setEndDate(Date.getDate(2000, 1, 1));
    assertTrue(this.relation.compareTo(r) > 0);
  }

  @Test
  public void testCompareEndDateSame() {
    Relationship r = new Relationship(1, 2).setEndDate(Date.getDate(2000, 1, 1));
    this.relation.setEndDate(Date.getDate(2000, 1, 1));
    assertEquals(0, this.relation.compareTo(r));
  }

  @Test
  public void testCompareEndDateNull() {
    Relationship r = new Relationship(1, 2).setEndDate(Date.getDate(2000, 1, 1));
    assertEquals(0, this.relation.compareTo(r));
  }

  @Test
  public void testCompareEndDateBothNull() {
    Relationship r = new Relationship(1, 2);
    assertEquals(0, this.relation.compareTo(r));
  }
}
