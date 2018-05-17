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
package net.darmo_creations.jenealogio.gui.components.canvas_view;

import java.util.HashMap;
import java.util.Map;

public final class CanvasState implements Cloneable {
  private Map<Long, CardState> cardStates;

  public CanvasState() {
    this.cardStates = new HashMap<>();
  }

  public Map<Long, CardState> getCardStates() {
    return new HashMap<>(this.cardStates);
  }

  public void addCardState(long id, final CardState state) {
    this.cardStates.put(id, state);
  }

  @Override
  public CanvasState clone() {
    try {
      CanvasState state = (CanvasState) super.clone();
      state.cardStates = new HashMap<>(this.cardStates);
      return state;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.cardStates == null) ? 0 : this.cardStates.hashCode());
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
    CanvasState other = (CanvasState) obj;
    if (this.cardStates == null) {
      if (other.cardStates != null)
        return false;
    }
    else if (!this.cardStates.equals(other.cardStates))
      return false;
    return true;
  }
}
