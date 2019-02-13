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
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.ViewEditEvent;

/**
 * This controller handles dragging events inside the canvas.
 * 
 * @author Damien Vergnet
 */
class ComponentDragController extends MouseAdapter {
  private CanvasView canvasView;
  /** The point where the mouse grabbed in the panel. */
  private Point grabPoint;
  private boolean dragged;
  private FamilyMemberPanel draggedPanel;

  /**
   * Creates a controller with the given container and component.
   * 
   * @param canvas the canvas
   */
  ComponentDragController(CanvasView canvas) {
    this.canvasView = canvas;
    this.grabPoint = null;
    this.draggedPanel = null;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    Optional<FamilyMemberPanel> p = this.canvasView.getHoveredPanel(e.getPoint());

    if (SwingUtilities.isLeftMouseButton(e) && p.isPresent() && (p.get().isSelected() || p.get().isSelectedBackground())) {
      this.dragged = false;
      this.draggedPanel = p.get();
      Point panelLoc = this.draggedPanel.getLocation();
      this.grabPoint = e.getPoint();
      this.grabPoint.x -= panelLoc.x;
      this.grabPoint.y -= panelLoc.y;
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (this.draggedPanel != null && this.dragged && SwingUtilities.isLeftMouseButton(e)) {
      this.canvasView.cardDropped(this.draggedPanel.getId());
      this.grabPoint = null;
      this.draggedPanel = null;
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new ViewEditEvent());
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (this.draggedPanel != null && SwingUtilities.isLeftMouseButton(e)) {
      // Check wether the panel has been deselected (happens when performing zone selection).
      if (!this.draggedPanel.isSelected() && !this.draggedPanel.isSelectedBackground()) {
        this.draggedPanel = null;
        return;
      }
      Rectangle containerBounds = this.canvasView.getCanvasBounds();
      int newX = Math.max(containerBounds.x, e.getXOnScreen() - getXOffset() - this.grabPoint.x);
      int newY = Math.max(containerBounds.y, e.getYOnScreen() - getYOffset() - this.grabPoint.y);
      Point oldLocation = this.draggedPanel.getLocation();
      Point newLocation = new Point(newX, newY);

      if (!oldLocation.equals(newLocation)) {
        this.dragged = true;
        Point translation = new Point(newLocation.x - oldLocation.x, newLocation.y - oldLocation.y);
        Point middle = new Point(newLocation.x + this.draggedPanel.getWidth() / 2, newLocation.y + this.draggedPanel.getHeight() / 2);

        this.canvasView.cardDragged(translation, middle);
      }
    }
  }

  /**
   * @return the onscreen x offset
   */
  private int getXOffset() {
    return this.canvasView.getCanvasOffset().x;
  }

  /**
   * @return the onscreen y offset
   */
  private int getYOffset() {
    return this.canvasView.getCanvasOffset().y;
  }
}
