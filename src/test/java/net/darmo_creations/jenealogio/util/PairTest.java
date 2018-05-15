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

import org.junit.Test;

public class PairTest {
  @Test(expected = NullPointerException.class)
  public void testNullFirstParameter() {
    new Pair<>(null, 5);
  }

  @Test(expected = NullPointerException.class)
  public void testNullSecondParameter() {
    new Pair<>(7, null);
  }

  @Test
  public void testEquals() {
    Pair<Integer, Integer> p1 = new Pair<>(7, 5);
    Pair<Integer, Integer> p2 = new Pair<>(7, 5);
    assertEquals(p1, p2);
  }

  @Test
  public void testNotEqualsDifferentValues1() {
    Pair<Integer, Integer> p1 = new Pair<>(7, 5);
    Pair<Integer, Integer> p2 = new Pair<>(8, 5);
    assertNotEquals(p1, p2);
  }

  @Test
  public void testNotEqualsDifferentValues2() {
    Pair<Integer, Integer> p1 = new Pair<>(7, 5);
    Pair<Integer, Integer> p2 = new Pair<>(7, 4);
    assertNotEquals(p1, p2);
  }

  @Test
  public void testNotEqualsDifferentTypes1() {
    Pair<Integer, Integer> p1 = new Pair<>(7, 5);
    Pair<String, Integer> p2 = new Pair<>("e", 5);
    assertNotEquals(p1, p2);
  }

  @Test
  public void testNotEqualsDifferentTypes2() {
    Pair<Integer, Integer> p1 = new Pair<>(7, 5);
    Pair<Integer, String> p2 = new Pair<>(7, "e");
    assertNotEquals(p1, p2);
  }
}
