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

import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.jenealogio.model.family.Family;

/**
 * This class represents a family edit used by the UndoManager class in the MainController.
 *
 * @author Damien Vergnet
 */
public final class FamilyEdit {
  private final Family family;
  private final Map<Long, CardState> cardsStates;

  /**
   * Creates an edit.
   * 
   * @param family the family
   * @param cardsStates states of all cards
   */
  public FamilyEdit(final Family family, final Map<Long, CardState> cardsStates) {
    this.family = family.clone();
    this.cardsStates = new HashMap<>(cardsStates);
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
  public Map<Long, CardState> getStates() {
    return new HashMap<>(this.cardsStates);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.family == null) ? 0 : this.family.hashCode());
    result = prime * result + ((this.cardsStates == null) ? 0 : this.cardsStates.hashCode());
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
    if (this.cardsStates == null) {
      if (other.cardsStates != null)
        return false;
    }
    else if (!this.cardsStates.equals(other.cardsStates))
      return false;
    return true;
  }
}
