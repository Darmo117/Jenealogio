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

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class UndoRedoManager<T extends Undoable> {
  private List<T> edits;
  private int index;

  public UndoRedoManager() {
    this.edits = new ArrayList<>();
    this.index = -1;
  }

  public void clear() {
    this.edits.clear();
    this.index = -1;
  }

  public void addEdit(T edit) {
    this.index++;
    if (!this.edits.isEmpty())
      this.edits = this.edits.subList(0, this.index);
    this.edits.add(edit);
  }

  public T getEdit() {
    return this.edits.get(this.index);
  }

  public void undo() {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    this.index--;
  }

  public void redo() {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    this.index++;
  }

  public boolean canUndo() {
    return this.index > 0;
  }

  public boolean canRedo() {
    return this.index >= 0 && this.index < this.edits.size() - 1;
  }
}
