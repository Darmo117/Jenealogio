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
package net.darmo_creations.jenealogio.model;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.family.Family;

public class FamilyEditTest {
  private FamilyEdit edit1, edit2;

  @Before
  public void setUp() throws Exception {
    this.edit1 = new FamilyEdit(new Family("test"), Collections.singletonMap(1L, new CardState(new Point(), new Dimension())));
    this.edit2 = new FamilyEdit(new Family("test"), Collections.singletonMap(1L, new CardState(new Point(), new Dimension())));
  }

  @Test
  public void testEquals() {
    assertEquals(this.edit2, this.edit1);
  }

  @Test
  public void testNotEqualsDifferentFamily() {
    assertNotEquals(new FamilyEdit(new Family("test2"), Collections.singletonMap(1L, new CardState(new Point(), new Dimension()))),
        this.edit1);
  }

  @Test
  public void testNotEqualsDifferentStates() {
    assertNotEquals(new FamilyEdit(new Family("test"), Collections.emptyMap()), this.edit1);
  }

  @Test
  public void testHashcode() {
    assertEquals(this.edit2.hashCode(), this.edit1.hashCode());
  }

  @Test
  public void testGetFamily() {
    assertEquals(new Family("test"), this.edit1.getFamily());
  }

  @Test
  public void getStates() {
    assertEquals(Collections.singletonMap(1L, new CardState(new Point(), new Dimension())), this.edit1.getStates());
  }
}
