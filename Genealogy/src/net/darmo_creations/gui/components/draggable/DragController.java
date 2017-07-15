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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This controller handles dragging events.
 * 
 * @author Damien Vergnet
 *
 * @param <T> a draggable component type
 */
public class DragController<T extends Draggable> extends MouseAdapter {
  /** Grid size in pixels */
  public static final int GRID_STEP = 10;

  private DraggableComponentContainer<T> handler;
  private T dragable;
  /** The point where the mouse grabbed in the panel. */
  private Point grabPoint;

  /**
   * Creates a controller with the given container and component.
   * 
   * @param handler the container
   * @param dragable the component this controller is monitoring
   */
  public DragController(DraggableComponentContainer<T> handler, T dragable) {
    this.handler = handler;
    this.dragable = dragable;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.dragable.doClick(e);
    this.grabPoint = new Point(e.getX(), e.getY());
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Rectangle bounds = this.dragable.getBounds();
    Rectangle containerBounds = this.handler.getBounds();
    if (this.grabPoint == null)
      mousePressed(e);
    int newX = Math.max(containerBounds.x,
        Math.min(containerBounds.width - bounds.width, e.getXOnScreen() - getXOffset() - this.grabPoint.x));
    int newY = Math.max(containerBounds.y,
        Math.min(containerBounds.height - bounds.height, e.getYOnScreen() - getYOffset() - this.grabPoint.y));

    this.dragable.setLocation(new Point((newX / GRID_STEP) * GRID_STEP, (newY / GRID_STEP) * GRID_STEP));
    this.handler.componentDragged(e, this.dragable);
  }

  /**
   * @return the onscreen x offset
   */
  private int getXOffset() {
    return this.handler.getScrollOffset().x;
  }

  /**
   * @return the onscreen y offset
   */
  private int getYOffset() {
    return this.handler.getScrollOffset().y;
  }
}
