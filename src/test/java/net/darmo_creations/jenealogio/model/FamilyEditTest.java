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

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasState;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CardState;
import net.darmo_creations.jenealogio.model.family.Family;

public class FamilyEditTest {
  private FamilyEdit edit1, edit2;

  @Before
  public void setUp() throws Exception {
    CanvasState state1 = new CanvasState();
    state1.addCardState(1, new CardState(new Point(), false));
    CanvasState state2 = new CanvasState();
    state2.addCardState(1, new CardState(new Point(), false));

    this.edit1 = new FamilyEdit(new Family("test"), state1);
    this.edit2 = new FamilyEdit(new Family("test"), state2);
  }

  @Test
  public void testEquals() {
    assertEquals(this.edit2, this.edit1);
  }

  @Test
  public void testNotEqualsDifferentFamily() {
    CanvasState state = new CanvasState();
    state.addCardState(1, new CardState(new Point(), false));
    assertNotEquals(new FamilyEdit(new Family("test2"), state), this.edit1);
  }

  @Test
  public void testNotEqualsDifferentStates() {
    assertNotEquals(new FamilyEdit(new Family("test"), new CanvasState()), this.edit1);
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
    CanvasState state = new CanvasState();
    state.addCardState(1L, new CardState(new Point(), false));
    assertEquals(state, this.edit1.getCanvasState());
  }
}
