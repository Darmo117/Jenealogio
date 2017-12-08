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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.events.ViewEditEvent;
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

  private boolean resizingComponent;
  private GrabHandle handle;

  /** All currently selected cards */
  private List<GraphicalObject> selectedObjects;

  CanvasViewController() {
    super(ViewType.CANVAS);

    this.mouseLocation = new Point();
    this.selectionStart = null;
    this.selection = null;
    this.resizingComponent = false;
    this.handle = null;

    this.selectedObjects = new ArrayList<>();
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
    return new Selection(getSelectedPanels(), getSelectedLinks());
  }

  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);
    GrabHandle h = getView().isPointOnHandle(e.getPoint());

    if (!this.view.hasFocus())
      this.view.requestFocus();

    if (SwingUtilities.isLeftMouseButton(e)) {
      this.selectionStart = e.getPoint();
      if (h != null) {
        this.handle = h;
        this.resizingComponent = true;
        getView().resizing(true);
      }
      else if (!getView().isMouseOverObject(this.mouseLocation)) {
        this.selection = new Rectangle(this.selectionStart);
        if (!getView().getHoveredPanel(this.mouseLocation).isPresent() && !getView().getHoveredLink(this.mouseLocation).isPresent())
          deselectAll();
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    super.mouseReleased(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      if (this.handle != null) {
        this.handle = null;
        this.resizingComponent = false;
        getView().setCursor(Cursor.getDefaultCursor());
        getView().resizing(false);
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(new ViewEditEvent());
      }
      if (!getView().isResizing() && this.selection != null) {
        objectsSelected(getView().getPanelsInsideRectangle(this.selection));
        this.selection = null;
        this.view.repaint();
      }
    }
  }

  /**
   * Deselects all cards and checks if a link was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      Optional<FamilyMemberPanel> p = getView().getHoveredPanel(this.mouseLocation);
      boolean ctrlDown = e.isControlDown();

      if (p.isPresent()) {
        FamilyMemberPanel member = p.get();
        objectClicked(member, ctrlDown);
        if (!ctrlDown && e.getClickCount() == 2)
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDoubleClickEvent(member.getId()));
      }
      else {
        Optional<Link> l = getView().getHoveredLink(this.mouseLocation);

        if (l.isPresent()) {
          Link link = l.get();
          objectClicked(link, ctrlDown);
          if (!ctrlDown && e.getClickCount() == 2)
            ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkDoubleClickEvent(link.getParent1(), link.getParent2()));
        }
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);
    this.handle = getView().isPointOnHandle(e.getPoint());

    if (this.handle == null)
      getView().setCursor(Cursor.getDefaultCursor());
    else
      getView().setCursor(this.handle.getCursor());

    updateMouseLocation(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    super.mouseDragged(e);

    Point prevLocation = this.mouseLocation;

    updateMouseLocation(e);
    if (SwingUtilities.isLeftMouseButton(e)) {
      if (this.handle != null) {
        Point p = e.getPoint();
        p.x -= prevLocation.x;
        p.y -= prevLocation.y;

        if (!this.resizingComponent)
          this.resizingComponent = true;
        if (this.handle.getDirection().isHorizontal()) {
          p.y = 0;
          this.handle.translate(p);
        }
        else if (this.handle.getDirection().isVertical()) {
          p.x = 0;
          this.handle.translate(p);
        }
        else {
          this.handle.translate(p);
        }
        resizePanelIfOutside();
        scrollIfOutside();
      }
    }

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
    this.selectedObjects.clear();
    updateSelection(old);
  }

  void cardDragged(Point translation, Point mouseLocation) {
    this.mouseLocation = mouseLocation;
    this.selectedObjects.stream().filter(o -> o instanceof FamilyMemberPanel).map(o -> (FamilyMemberPanel) o).forEach(
        m -> m.setLocation(new Point(m.getLocation().x + translation.x, m.getLocation().y + translation.y)));
    resizePanelIfOutside();
    scrollIfOutside();
  }

  /**
   * Called when several objects are selected.
   * 
   * @param object the object
   */
  private void objectsSelected(List<? extends GraphicalObject> object) {
    Selection old = getSelection();

    this.selectedObjects.clear();
    this.selectedObjects.addAll(object);

    updateSelection(old);
  }

  /**
   * Called when an object is clicked.
   * 
   * @param object the object
   * @param keepSelection if true, the previous selection will be kept
   */
  private void objectClicked(GraphicalObject object, boolean keepSelection) {
    Selection old = getSelection();

    if (keepSelection) {
      if (this.selectedObjects.contains(object)) {
        this.selectedObjects.remove(object);
      }
      else {
        this.selectedObjects.add(object);
      }
    }
    else {
      this.selectedObjects.clear();
      this.selectedObjects.add(object);
    }

    updateSelection(old);
  }

  private void updateSelection(Selection old) {
    Selection current = getSelection();

    if (!current.equals(old)) {
      getView().selectObjects(this.selectedObjects);
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, current));
    }
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
    this.view.repaint();
  }

  private List<Long> getSelectedPanels() {
    return this.selectedObjects.stream().filter(o -> o instanceof FamilyMemberPanel).map(m -> m.getId()).collect(Collectors.toList());
  }

  private List<Pair<Long, Long>> getSelectedLinks() {
    return this.selectedObjects.stream().filter(o -> o instanceof Link).map(o -> {
      Link l = (Link) o;
      return new Pair<>(l.getParent1(), l.getParent2());
    }).collect(Collectors.toList());
  }

  private static final int LEFT = 1;
  private static final int RIGHT = 2;
  private static final int TOP = 4;
  private static final int BOTTOM = 8;

  private int isOutsideRectangle(Point p, Rectangle r) {
    int res = 0;

    if (p.getX() < r.x)
      res = LEFT;
    else if (p.getX() > r.x + r.width)
      res = RIGHT;
    if (p.getY() < r.y)
      res |= TOP;
    else if (p.getY() > r.y + r.height)
      res |= BOTTOM;

    return res;
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

    if ((mouse & RIGHT) != 0)
      hAdd = step;
    if ((mouse & BOTTOM) != 0)
      vAdd = step;

    if (vAdd != 0 || hAdd != 0) {
      Dimension d = getView().getCanvasBounds().getSize();
      getView().setCanvasPreferredSize(new Dimension(d.width + hAdd, d.height + vAdd));
      this.view.revalidate();
      this.view.repaint();
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

    if ((mouse & LEFT) != 0)
      hTrans = -step;
    else if ((mouse & RIGHT) != 0)
      hTrans = step;
    if ((mouse & TOP) != 0)
      vTrans = -step;
    else if ((mouse & BOTTOM) != 0)
      vTrans = step;

    if (vTrans != 0)
      this.view.setVerticalScroll(this.view.getVerticalScroll() + vTrans);
    if (hTrans != 0)
      this.view.setHorizontalScroll(this.view.getHorizontalScroll() + hTrans);

  }

  private CanvasView getView() {
    return (CanvasView) this.view;
  }
}
