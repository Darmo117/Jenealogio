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
package net.darmo_creations.gui.components.display_panel;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import net.darmo_creations.events.CardEvent;
import net.darmo_creations.events.CardsSelectionEvent;
import net.darmo_creations.events.EventsDispatcher;
import net.darmo_creations.events.LinkEvent;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class DisplayController extends MouseAdapter {
  private DisplayPanel panel;
  private Point mouseLocation;
  private Point selectionStart;
  private Rectangle selection;

  DisplayController(DisplayPanel panel) {
    this.panel = panel;
    this.mouseLocation = new Point();
    this.selectionStart = null;
    this.selection = null;
  }

  /**
   * @return the last location of the mouse
   */
  Point getMouseLocation() {
    return (Point) this.mouseLocation.clone();
  }

  /**
   * @return selection's starting position
   */
  Optional<Rectangle> getSelection() {
    return Optional.ofNullable(this.selection);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      this.selectionStart = e.getPoint();
      this.selection = new Rectangle(this.selectionStart);
    }
    EventsDispatcher.EVENT_BUS.dispatchEvent(new CardEvent.Clicked(-1, false));
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    Optional<long[]> opt = this.panel.getPanelsInsideRectangle(this.selection);
    if (opt.isPresent()) {
      EventsDispatcher.EVENT_BUS.dispatchEvent(new CardsSelectionEvent(opt.get()));
    }
    this.selection = null;
    repaint();
  }

  /**
   * Called the mouse is clicked inside the DisplayPanel. Deselects all cards and checks if a link
   * was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    Optional<long[]> l = this.panel.getHoveredLinkPartners();

    if (l.isPresent()) {
      long[] ids = l.get();
      EventsDispatcher.EVENT_BUS.dispatchEvent(new LinkEvent.Clicked(ids[0], ids[1]));
      if (e.getClickCount() == 2)
        EventsDispatcher.EVENT_BUS.dispatchEvent(new LinkEvent.DoubleClicked(ids[0], ids[1]));
    }
  }

  /**
   * Repaints the DisplayPanel everytime the mouse moves.
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    updateMouseLocation(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    updateMouseLocation(e);
  }

  private void updateMouseLocation(MouseEvent e) {
    this.mouseLocation = e.getPoint();
    // Selection computation
    if (this.selection != null) {
      int width = this.mouseLocation.x - this.selectionStart.x;
      int height = this.mouseLocation.y - this.selectionStart.y;

      if (width < 0) {
        width = -width;
        this.selection.x = this.selectionStart.x - width;
        this.selection.width = width;
      }
      else {
        this.selection.width = width;
      }
      if (height < 0) {
        height = -height;
        this.selection.y = this.selectionStart.y - height;
        this.selection.height = height;
      }
      else {
        this.selection.height = height;
      }
    }
    repaint();
  }

  private void repaint() {
    this.panel.repaint();
  }
}
