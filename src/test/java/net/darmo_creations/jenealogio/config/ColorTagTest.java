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
package net.darmo_creations.jenealogio.config;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

public class ColorTagTest {
  private static ColorTag tag;

  @BeforeClass
  public static void setUpBeforeClass() {
    tag = new ColorTag("test");
  }

  @Test
  public void testTagValueClass() {
    assertEquals(Color.class, tag.getValueClass());
  }

  @Test
  public void testDeserializeColorNoAlpha() {
    assertEquals(Color.BLUE, tag.deserializeValue("255"));
  }

  @Test
  public void testDeserializeColorAlpha() {
    assertEquals(new Color(0, 0, 255, 255), tag.deserializeValue(Integer.toString(255 | (255 << 24))));
  }

  @Test(expected = NumberFormatException.class)
  public void testDeserializeNotInt() {
    tag.deserializeValue("a");
  }

  @Test(expected = NullPointerException.class)
  public void testDeserializeNull() {
    tag.deserializeValue(null);
  }

  @Test
  public void testSerializeColor() {
    assertEquals(Integer.toString(255 | (255 << 24)), tag.serializeValueGeneric(Color.BLUE));
  }
}
