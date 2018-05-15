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

/**
 * This class represents the state of a card in the canvas.
 *
 * @author Damien Vergnet
 */
public final class CardState {
  private final Point location;
  private final Dimension size;

  public CardState(Point location, Dimension size) {
    this.location = location;
    this.size = size;
  }

  public Point getLocation() {
    return this.location;
  }

  public Dimension getSize() {
    return this.size;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
    result = prime * result + ((this.size == null) ? 0 : this.size.hashCode());
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
    CardState other = (CardState) obj;
    if (this.location == null) {
      if (other.location != null)
        return false;
    }
    else if (!this.location.equals(other.location))
      return false;
    if (this.size == null) {
      if (other.size != null)
        return false;
    }
    else if (!this.size.equals(other.size))
      return false;
    return true;
  }
}
