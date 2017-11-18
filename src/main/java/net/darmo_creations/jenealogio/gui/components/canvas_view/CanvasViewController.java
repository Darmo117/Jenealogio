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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.gui.components.view.ViewController;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class CanvasViewController extends ViewController {
  private Point mouseLocation;
  private Point selectionStart;
  private Rectangle selection;

  /** All currently selected cards */
  private List<Long> selectedCards;
  /** The currently selected links */
  private List<Pair<Long, Long>> selectedLinks;

  CanvasViewController() {
    super(ViewType.CANVAS);

    this.mouseLocation = new Point();
    this.selectionStart = null;
    this.selection = null;

    this.selectedCards = new ArrayList<>();
    this.selectedLinks = new ArrayList<>();
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
  Optional<Rectangle> getSelectionRectangle() {
    return Optional.ofNullable(this.selection);
  }

  /**
   * Returns the current selection.
   */
  public Selection getSelection() {
    return new Selection(this.selectedCards, this.selectedLinks);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);

    this.view.requestFocus();
    if (SwingUtilities.isLeftMouseButton(e)) {
      this.selectionStart = e.getPoint();
      this.selection = new Rectangle(this.selectionStart);
      deselectAll();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    super.mouseReleased(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      cardsSelected(getView().getPanelsInsideRectangle(this.selection));
      this.selection = null;
      repaint();
    }
  }

  /**
   * Deselects all cards and checks if a link was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      Optional<Pair<Long, Long>> l = getView().getHoveredLinkPartners();

      if (l.isPresent()) {
        boolean ctrlDown = e.isControlDown();
        Pair<Long, Long> ids = l.get();
        linkClicked(ids.getValue1(), ids.getValue2(), ctrlDown);
        if (!ctrlDown && e.getClickCount() == 2)
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkDoubleClickEvent(ids.getValue1(), ids.getValue2()));
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);

    updateMouseLocation(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    super.mouseDragged(e);

    Point prevLocation = this.mouseLocation;

    updateMouseLocation(e);
    if (SwingUtilities.isMiddleMouseButton(e)) {
      Point newLocation = this.mouseLocation;

      int xTrans = newLocation.x - prevLocation.x;
      int yTrans = newLocation.y - prevLocation.y;

      this.view.setHorizontalScroll(this.view.getHorizontalScroll() - xTrans);
      this.view.setVerticalScroll(this.view.getVerticalScroll() - yTrans);
    }
  }

  /**
   * Deselects all components and sends a ViewEditEvent.
   */
  void deselectAll() {
    Selection old = getSelection();

    this.selectedCards.clear();
    this.selectedLinks.clear();

    getView().selectPanels(this.selectedCards);
    getView().selectLinks(this.selectedLinks);

    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, getSelection()));
  }

  /**
   * Called when several cards are selected simultaneously.
   */
  void cardsSelected(List<Long> ids) {
    Selection old = getSelection();

    this.selectedLinks.clear();
    this.selectedCards.clear();
    this.selectedCards.addAll(ids);
    getView().selectPanels(ids);

    if (!ids.isEmpty())
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, getSelection()));
  }

  void cardDragged(Point mouseLocation) {
    this.mouseLocation = mouseLocation;
    resizePanelIfOutside();
    scrollIfOutside();
  }

  /**
   * Called when a card is clicked.
   * 
   * @param e the event
   */
  void panelClicked(long id, boolean keepSelection) {
    if (id < 0)
      throw new IllegalArgumentException("" + id);

    if (!this.view.hasFocus())
      this.view.requestFocus();

    Selection old = getSelection();

    if (keepSelection) {
      if (this.selectedCards.contains(id))
        this.selectedCards.remove(id);
      else
        this.selectedCards.add(id);
    }
    else {
      this.selectedCards.clear();
      this.selectedCards.add(id);
      this.selectedLinks.clear();
    }

    getView().selectPanels(this.selectedCards);
    getView().selectLinks(this.selectedLinks);

    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, getSelection()));
  }

  private void linkClicked(long id1, long id2, boolean keepSelection) {
    Selection old = getSelection();

    if (keepSelection) {
      this.selectedLinks.add(new Pair<>(id1, id2));
    }
    else {
      this.selectedLinks.clear();
      this.selectedLinks.add(new Pair<>(id1, id2));
      this.selectedCards.clear();
    }

    getView().selectPanels(this.selectedCards);
    getView().selectLinks(this.selectedLinks);

    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, getSelection()));
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
      scrollIfOutside();
    }
    repaint();
  }

  private static final int INSIDE = 0;
  private static final int LEFT = 1;
  private static final int RIGHT = 2;
  private static final int TOP = 4;
  private static final int BOTTOM = 8;

  private int isOutsideRectangle(Point p, Rectangle r) {
    if (p.getX() < r.x) {
      return LEFT;
    }
    else if (p.getX() > r.x + r.width) {
      return RIGHT;
    }
    if (p.getY() < r.y) {
      return TOP;
    }
    else if (p.getY() > r.y + r.height) {
      return BOTTOM;
    }
    return INSIDE;
  }

  /**
   * Resizes the panel if a component is dragged outside.
   */
  private void resizePanelIfOutside() {
    Rectangle r = getView().getCanvasBounds();
    r.x = r.y = 0;
    int mouse = isOutsideRectangle(this.mouseLocation, r);
    int vAdd = 0;
    int hAdd = 0;
    final int step = 30;

    switch (mouse) {
      case RIGHT:
        hAdd = step;
        break;
      case BOTTOM:
        vAdd = step;
        break;
    }

    if (vAdd != 0 || hAdd != 0) {
      Dimension d = getView().getCanvasBounds().getSize();
      getView().setCanvasPreferredSize(new Dimension(d.width + hAdd, d.height + vAdd));
      this.view.revalidate();
      repaint();
    }
  }

  /**
   * Scrolls if the mouse is outside the viewport.
   */
  private void scrollIfOutside() {
    int mouse = isOutsideRectangle(this.mouseLocation, this.view.getViewportVisibleRect());
    int vTrans = 0;
    int hTrans = 0;
    final int step = 16;

    switch (mouse) {
      case LEFT:
        hTrans = -step;
        break;
      case RIGHT:
        hTrans = step;
        break;
      case TOP:
        vTrans = -step;
        break;
      case BOTTOM:
        vTrans = step;
        break;
    }

    if (vTrans != 0)
      this.view.setVerticalScroll(this.view.getVerticalScroll() + vTrans);
    if (hTrans != 0)
      this.view.setHorizontalScroll(this.view.getHorizontalScroll() + hTrans);
  }

  private void repaint() {
    this.view.repaint();
  }

  private CanvasView getView() {
    return (CanvasView) this.view;
  }
}
