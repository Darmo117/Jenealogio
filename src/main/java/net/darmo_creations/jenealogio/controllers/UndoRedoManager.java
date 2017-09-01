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
package net.darmo_creations.jenealogio.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * This class handles undo/redo actions.
 * 
 * @author Damien Vergnet
 *
 * @param <T> type of edits
 */
public class UndoRedoManager<T> {
  private List<T> edits;
  private int index;

  /**
   * Creates an empty manager.
   */
  public UndoRedoManager() {
    this.edits = new ArrayList<>();
    this.index = -1;
  }

  /**
   * Clears all edits.
   */
  public void clear() {
    this.edits.clear();
    this.index = -1;
  }

  /**
   * Adds an edit.
   * 
   * @param edit the new edit
   */
  public void addEdit(T edit) {
    this.index++;
    this.edits = this.edits.subList(0, this.index);
    this.edits.add(edit);
  }

  /**
   * @return the current edit
   */
  public T getEdit() {
    return this.edits.get(this.index);
  }

  /**
   * Undoes an edit.
   */
  public void undo() {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    this.index--;
  }

  /**
   * Restores an edit.
   */
  public void redo() {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    this.index++;
  }

  /**
   * @return true if the manager can undo an edit
   */
  public boolean canUndo() {
    return this.index > 0;
  }

  /**
   * @return true if the manager can restore an edit
   */
  public boolean canRedo() {
    return this.index >= 0 && this.index < this.edits.size() - 1;
  }
}
