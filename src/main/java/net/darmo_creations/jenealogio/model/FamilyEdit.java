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

import java.awt.Dimension;
import java.awt.Point;
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
  private final Map<Long, Point> locations;
  private final Map<Long, Dimension> sizes;

  /**
   * Creates an edit.
   * 
   * @param family the family
   * @param locations locations for all cards
   */
  public FamilyEdit(final Family family, final Map<Long, Point> locations, final Map<Long, Dimension> sizes) {
    this.family = family.clone();
    this.locations = new HashMap<>(locations);
    this.sizes = new HashMap<>(sizes);
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
  public Map<Long, Point> getLocations() {
    return new HashMap<>(this.locations);
  }

  /**
   * @return sizes for all cards
   */
  public Map<Long, Dimension> getSizes() {
    return new HashMap<>(this.sizes);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.family == null) ? 0 : this.family.hashCode());
    result = prime * result + ((this.locations == null) ? 0 : this.locations.hashCode());
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
    if (this.locations == null) {
      if (other.locations != null)
        return false;
    }
    else if (!this.locations.equals(other.locations))
      return false;
    return true;
  }
}
