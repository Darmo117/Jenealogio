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

import org.junit.Test;

public class GenderTest {
  @Test
  public void testManIsMan() {
    assertTrue(Gender.MAN.isMan());
  }

  @Test
  public void testManIsNotWoman() {
    assertFalse(Gender.MAN.isWoman());
  }

  @Test
  public void testWomanIsWoman() {
    assertTrue(Gender.WOMAN.isWoman());
  }

  @Test
  public void testWomanIsNotMan() {
    assertFalse(Gender.WOMAN.isMan());
  }

  @Test
  public void testUnknownIsNotMan() {
    assertFalse(Gender.UNKNOW.isMan());
  }

  @Test
  public void testUnknownIsNotWoman() {
    assertFalse(Gender.UNKNOW.isWoman());
  }

  @Test
  public void testGetCodeMan() {
    assertEquals(Gender.MAN, Gender.fromCode("M"));
  }

  @Test
  public void testGetCodeWoman() {
    assertEquals(Gender.WOMAN, Gender.fromCode("F"));
  }

  @Test
  public void testGetCodeUnknown() {
    assertEquals(Gender.UNKNOW, Gender.fromCode(""));
  }
}
