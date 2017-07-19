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
package net.darmo_creations.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.controllers.Undoable;
import net.darmo_creations.model.family.Family;

/**
 * This class represents a family edit with the positions of all cards.
 *
 * @author Damien Vergnet
 */
public final class FamilyEdit implements Undoable {
  private final Family family;
  private final Map<Long, Point> locations;

  /**
   * Creates an edit.
   * 
   * @param family the family
   * @param locations locations for all cards
   */
  public FamilyEdit(Family family, Map<Long, Point> locations) {
    this.family = family.clone();
    this.locations = new HashMap<>(locations);
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
}
