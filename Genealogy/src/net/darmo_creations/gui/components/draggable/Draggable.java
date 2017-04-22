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
package net.darmo_creations.gui.components.draggable;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A component can implement this interface to become draggable.
 *
 * @author Damien Vergnet
 */
public interface Draggable {
  /**
   * Sets the location of this component.
   * 
   * @param p the new location
   */
  void setLocation(Point p);

  /**
   * @return the bounds as a rectangle
   */
  Rectangle getBounds();

  /**
   * Performs a click action.
   */
  void doClick();
}
