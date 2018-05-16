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
package net.darmo_creations.jenealogio.model;

import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasState;
import net.darmo_creations.jenealogio.model.family.Family;

/**
 * This class represents a family edit used by the UndoManager class in the MainController.
 *
 * @author Damien Vergnet
 */
public final class FamilyEdit {
  private final Family family;
  private final CanvasState canvasState;

  public FamilyEdit(final Family family, final CanvasState canvasStates) {
    this.family = family.clone();
    this.canvasState = canvasStates.clone();
  }

  /**
   * @return the family
   */
  public Family getFamily() {
    return this.family.clone();
  }

  /**
   * @return locations for all cards
   */
  public CanvasState getCanvasState() {
    return this.canvasState.clone();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.family == null) ? 0 : this.family.hashCode());
    result = prime * result + ((this.canvasState == null) ? 0 : this.canvasState.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FamilyEdit other = (FamilyEdit) obj;
    if (this.family == null) {
      if (other.family != null)
        return false;
    }
    else if (!this.family.equals(other.family))
      return false;
    if (this.canvasState == null) {
      if (other.canvasState != null)
        return false;
    }
    else if (!this.canvasState.equals(other.canvasState))
      return false;
    return true;
  }
}
