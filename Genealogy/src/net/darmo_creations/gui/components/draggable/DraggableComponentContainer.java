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
import java.awt.event.MouseEvent;

/**
 * A component implementing this interface can hold draggable components.
 * 
 * @author Damien Vergnet
 *
 * @param <T> type of the inner components
 */
public interface DraggableComponentContainer<T extends Draggable> {
  /**
   * @return the bounds of this container
   */
  Rectangle getBounds();

  /**
   * @return this component's onscreen offset
   */
  Point getScrollOffset();

  /**
   * This method is called when a component has been dragged.
   * 
   * @param e the mouse event
   * @param component the dragged component
   * @param translation distance traveled by the mouse
   */
  void componentDragged(MouseEvent e, T component, Point translation);
}
