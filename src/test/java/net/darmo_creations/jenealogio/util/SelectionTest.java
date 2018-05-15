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
package net.darmo_creations.jenealogio.util;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SelectionTest {
  private Selection selection1, selection2;

  @Before
  public void setUp() {
    this.selection1 = new Selection(Collections.emptyList(), Collections.emptyList());
    this.selection2 = new Selection(Collections.emptyList(), Collections.emptyList());
  }

  @Test
  public void testIsEmpty() {
    assertTrue(this.selection1.isEmpty());
  }

  @Test
  public void test0Size() {
    assertEquals(0, this.selection1.size());
  }

  @Test
  public void test1Size1Member() {
    assertEquals(1, new Selection(Collections.singletonList(1L), Collections.emptyList()).size());
  }

  @Test
  public void test1Size1Relation() {
    assertEquals(1, new Selection(Collections.emptyList(), Collections.singletonList(new Pair<>(1L, 2L))).size());
  }

  @Test
  public void test2Size1Member1Relation() {
    assertEquals(2, new Selection(Collections.singletonList(1L), Collections.singletonList(new Pair<>(1L, 2L))).size());
  }

  @Test
  public void testMembersEqual() {
    List<Long> members = Collections.singletonList(1L);
    Selection s = new Selection(members, Collections.emptyList());
    assertEquals(members, s.getMembers());
  }

  @Test
  public void testRelationsEqual() {
    List<Pair<Long, Long>> relations = Collections.singletonList(new Pair<>(1L, 2L));
    Selection s = new Selection(Collections.emptyList(), relations);
    assertEquals(relations, s.getRelations());
  }

  @Test
  public void testEqualsSameArgs() {
    List<Long> members = Collections.singletonList(1L);
    List<Pair<Long, Long>> relations = Collections.singletonList(new Pair<>(1L, 2L));
    this.selection1 = new Selection(members, relations);
    this.selection2 = new Selection(members, relations);

    assertEquals(this.selection1, this.selection2);
  }

  @Test
  public void testEqualsArgsEqualButNotSame() {
    List<Long> members = Collections.singletonList(1L);
    List<Pair<Long, Long>> relations = Collections.singletonList(new Pair<>(1L, 2L));
    this.selection1 = new Selection(members, relations);
    List<Long> m = Collections.singletonList(1L);
    List<Pair<Long, Long>> r = Collections.singletonList(new Pair<>(1L, 2L));
    this.selection2 = new Selection(m, r);

    assertEquals(this.selection1, this.selection2);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testMembersUnmodifiable() {
    this.selection1.getMembers().add(2L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testRelationsUnmodifiable() {
    this.selection1.getRelations().add(new Pair<>(3L, 4L));
  }
}
