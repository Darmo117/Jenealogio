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
package net.darmo_creations.jenealogio.gui.components.display_panel;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.CardDragEvent;
import net.darmo_creations.jenealogio.events.CardEvent;
import net.darmo_creations.jenealogio.gui.components.FamilyMemberPanel;

/**
 * This controller handles dragging events inside the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class ComponentDragController extends MouseAdapter {
  /** Grid size in pixels */
  static final int GRID_STEP = 10;

  private DisplayPanel displayPanel;
  private FamilyMemberPanel memberPanel;
  /** The point where the mouse grabbed in the panel. */
  private Point grabPoint;
  private boolean dragging;

  /**
   * Creates a controller with the given container and component.
   * 
   * @param displayPanel the container
   * @param memberPanel the component this controller is monitoring
   */
  ComponentDragController(DisplayPanel displayPanel, FamilyMemberPanel memberPanel) {
    this.displayPanel = displayPanel;
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
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDragEvent.Post(this.memberPanel.getMemberId()));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      int modifiers = e.getModifiers();
      boolean isCtrlDown = (modifiers & MouseEvent.CTRL_MASK) != 0;
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(this.memberPanel.getMemberId(), isCtrlDown));
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      Rectangle bounds = this.memberPanel.getBounds();
      Rectangle containerBounds = this.displayPanel.getBounds();
      if (this.grabPoint == null)
        mousePressed(e);
      int newX = Math.max(containerBounds.x,
          /* Math.min(containerBounds.width - bounds.width, */ e.getXOnScreen() - getXOffset() - this.grabPoint.x);
      int newY = Math.max(containerBounds.y,
          /* Math.min(containerBounds.height - bounds.height, */ e.getYOnScreen() - getYOffset() - this.grabPoint.y);
      newX = (newX / GRID_STEP) * GRID_STEP;
      newY = (newY / GRID_STEP) * GRID_STEP;
      Point oldLocation = this.memberPanel.getLocation();
      Point newLocation = new Point(newX, newY);

      if (!oldLocation.equals(newLocation)) {
        if (!this.dragging) {
          this.dragging = true;
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDragEvent.Pre(this.memberPanel.getMemberId()));
        }
        this.memberPanel.setLocation(newLocation);
        Point mouse = new Point(newLocation.x + this.grabPoint.x, newLocation.y + this.grabPoint.y);
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(
            new CardDragEvent.Dragging(this.memberPanel.getMemberId(), oldLocation, newLocation, mouse));
      }
    }
  }

  /**
   * @return the onscreen x offset
   */
  private int getXOffset() {
    return this.displayPanel.getLocationOnScreen().x;
  }

  /**
   * @return the onscreen y offset
   */
  private int getYOffset() {
    return this.displayPanel.getLocationOnScreen().y;
  }
}
