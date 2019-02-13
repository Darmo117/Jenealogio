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
package net.darmo_creations.jenealogio.gui.components.canvas_view;

import java.awt.Point;

/**
 * This class represents the state of a card in the canvas.
 *
 * @author Damien Vergnet
 */
public final class CardState implements Cloneable {
  private final Point location;
  private final boolean pixelLocation;

  public CardState(final Point location, boolean pixelLocation) {
    this.location = (Point) location.clone();
    this.pixelLocation = pixelLocation;
  }

  public Point getLocation() {
    return (Point) this.location.clone();
  }

  public boolean isPixelLocation() {
    return this.pixelLocation;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
    result = prime * result + (this.pixelLocation ? 1231 : 1237);
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
    if (this.pixelLocation != other.pixelLocation)
      return false;
    return true;
  }

  @Override
  public CardState clone() {
    try {
      return (CardState) super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)[%s]", this.location.x, this.location.y, this.pixelLocation ? "pixel" : "grid");
  }
}
