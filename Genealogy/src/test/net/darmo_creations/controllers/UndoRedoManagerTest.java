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
package net.darmo_creations.controllers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UndoRedoManagerTest {
  private UndoRedoManager<TestObject> manager;
  private TestObject o1, o2, o3;

  @Before
  public void setUp() throws Exception {
    this.manager = new UndoRedoManager<>();
    this.o1 = new TestObject(1);
    this.o2 = new TestObject(2);
    this.o3 = new TestObject(3);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testClear() {
    this.manager.addEdit(this.o1);
    this.manager.clear();
    this.manager.getEdit();
    fail("No exception thrown");
  }

  @Test
  public void testAddEdit() {
    this.manager.addEdit(this.o1.clone());
    assertEquals(this.o1, this.manager.getEdit());
  }

  @Test
  public void testUndo2Elements() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.undo();
    assertEquals(this.o1, this.manager.getEdit());
  }

  @Test
  public void testUndo3Elements() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.addEdit(this.o3.clone());
    this.manager.undo();
    assertEquals(this.o2, this.manager.getEdit());
  }

  @Test
  public void testRedo2Elements() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.undo();
    this.manager.redo();
    assertEquals(this.o2, this.manager.getEdit());
  }

  @Test
  public void testRedo3Elements() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.addEdit(this.o3.clone());
    this.manager.undo();
    this.manager.redo();
    assertEquals(this.o3, this.manager.getEdit());
  }

  @Test
  public void test2Undo1Redo() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.addEdit(this.o3.clone());
    this.manager.undo();
    this.manager.undo();
    this.manager.redo();
    assertEquals(this.o2, this.manager.getEdit());
  }

  @Test
  public void test2Undo2Redo() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.addEdit(this.o3.clone());
    this.manager.undo();
    this.manager.undo();
    this.manager.redo();
    this.manager.redo();
    assertEquals(this.o3, this.manager.getEdit());
  }

  @Test
  public void testCanUndo() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    assertTrue(this.manager.canUndo());
  }

  @Test
  public void testCannotUndoEmptyList() {
    assertFalse(this.manager.canUndo());
  }

  @Test
  public void testCannotUndoIndex0() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.undo();
    assertFalse(this.manager.canUndo());
  }

  @Test
  public void testCanRedo() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    this.manager.undo();
    assertTrue(this.manager.canRedo());
  }

  @Test
  public void testCannotRedoEmptyList() {
    assertFalse(this.manager.canRedo());
  }

  @Test
  public void testCannotRedoLastIndex() {
    this.manager.addEdit(this.o1.clone());
    this.manager.addEdit(this.o2.clone());
    assertFalse(this.manager.canRedo());
  }

  private static class TestObject implements Cloneable, Undoable {
    private final int id;

    public TestObject(int id) {
      this.id = id;
    }

    public int getId() {
      return this.id;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + this.id;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof TestObject)
        return getId() == ((TestObject) obj).getId();
      return false;
    }

    @Override
    public TestObject clone() {
      try {
        return (TestObject) super.clone();
      }
      catch (CloneNotSupportedException e) {
        throw new Error(e);
      }
    }
  }
}
