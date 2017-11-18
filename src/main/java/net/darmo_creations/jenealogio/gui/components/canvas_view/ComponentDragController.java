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

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.ViewEditEvent;
import net.darmo_creations.jenealogio.gui.components.canvas_view.member_panel.FamilyMemberPanel;

/**
 * This controller handles dragging events inside the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class ComponentDragController extends MouseAdapter {
  /** Grid size in pixels */
  static final int GRID_STEP = 10;

  private CanvasView canvasView;
  private FamilyMemberPanel memberPanel;
  /** The point where the mouse grabbed in the panel. */
  private Point grabPoint;
  private boolean dragging;

  /**
   * Creates a controller with the given container and component.
   * 
   * @param canvas the container
   * @param memberPanel the component this controller is monitoring
   */
  ComponentDragController(CanvasView canvas, FamilyMemberPanel memberPanel) {
    this.canvasView = canvas;
    this.memberPanel = memberPanel;
    this.grabPoint = null;
    this.dragging = false;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.grabPoint = new Point(e.getX(), e.getY());
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (this.dragging && SwingUtilities.isLeftMouseButton(e)) {
      this.dragging = false;
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new ViewEditEvent());
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      int modifiers = e.getModifiers();
      boolean isCtrlDown = (modifiers & MouseEvent.CTRL_MASK) != 0;
      this.canvasView.panelClicked(this.memberPanel.getMemberId(), isCtrlDown);
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!this.memberPanel.isMouseOnBorder() && SwingUtilities.isLeftMouseButton(e)) {
      Rectangle containerBounds = this.canvasView.getCanvasBounds();
      if (this.grabPoint == null)
        mousePressed(e);
      int newX = Math.max(containerBounds.x, e.getXOnScreen() - getXOffset() - this.grabPoint.x);
      int newY = Math.max(containerBounds.y, e.getYOnScreen() - getYOffset() - this.grabPoint.y);
      newX = (newX / GRID_STEP) * GRID_STEP;
      newY = (newY / GRID_STEP) * GRID_STEP;
      Point oldLocation = this.memberPanel.getLocation();
      Point newLocation = new Point(newX, newY);

      if (!oldLocation.equals(newLocation)) {
        if (!this.dragging)
          this.dragging = true;

        Point translation = new Point(newLocation.x - oldLocation.x, newLocation.y - oldLocation.y);
        Point middle = new Point(newLocation.x + this.memberPanel.getWidth() / 2, newLocation.y + this.memberPanel.getHeight() / 2);

        this.memberPanel.setLocation(newLocation);
        this.canvasView.cardDragged(this.memberPanel.getMemberId(), translation, middle);
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
