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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.events.GoToObjectEvent;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.events.ViewEditEvent;
import net.darmo_creations.jenealogio.gui.components.view.ViewController;
import net.darmo_creations.jenealogio.model.CardState;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.events.SubscribeEvent;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class CanvasViewController extends ViewController {
  /** The maximum distance away from a link the mouse must be to count as a hover. */
  private static final int HOVER_DISTANCE = 5;

  private Point mouseLocation;
  private Point selectionStart;
  private Rectangle selection;

  private boolean resizingComponent;
  private GrabHandle handle;

  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;
  /** All currently selected cards */
  private List<GraphicalObject> selectedObjects;

  CanvasViewController() {
    super(ViewType.CANVAS);

    this.mouseLocation = new Point();
    this.selectionStart = null;
    this.selection = null;
    this.resizingComponent = false;
    this.handle = null;

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();
    this.selectedObjects = new ArrayList<>();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);
    GrabHandle h = isPointOnHandle(e.getPoint());

    if (!this.view.hasFocus())
      this.view.requestFocus();

    if (SwingUtilities.isLeftMouseButton(e)) {
      this.selectionStart = e.getPoint();
      if (h != null) {
        this.handle = h;
        this.resizingComponent = true;
        getView().resizing(true);
      }
      else if (!isMouseOverObject(this.mouseLocation)) {
        this.selection = new Rectangle(this.selectionStart);
        if (!getHoveredPanel(this.mouseLocation).isPresent() && !getHoveredLink(this.mouseLocation).isPresent())
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
        objectsSelected(getPanelsInsideRectangle(this.selection));
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
      Optional<FamilyMemberPanel> p = getHoveredPanel(this.mouseLocation);
      boolean ctrlDown = e.isControlDown();

      if (p.isPresent()) {
        FamilyMemberPanel member = p.get();
        objectClicked(member, ctrlDown);
        if (!ctrlDown && e.getClickCount() == 2)
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDoubleClickEvent(member.getId()));
      }
      else {
        Optional<Link> l = getHoveredLink(this.mouseLocation);

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
    updateMouseLocation(e);

    this.panels.forEach((id, p) -> p.setHovered(false));
    this.links.forEach(l -> l.setHovered(false));

    getHoveredLink(this.mouseLocation).ifPresent(l -> l.setHovered(true));
    getHoveredPanel(this.mouseLocation).ifPresent(p -> p.setHovered(true));

    this.handle = isPointOnHandle(this.mouseLocation);

    if (this.handle == null)
      getView().setCursor(Cursor.getDefaultCursor());
    else
      getView().setCursor(this.handle.getCursor());
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

  private GrabHandle isPointOnHandle(Point point) {
    return this.panels.values().stream().map(p -> p.isOnHandle(point)).filter(h -> h != null).findAny().orElse(null);
  }

  @SubscribeEvent
  public void onGoToObject(GoToObjectEvent e) {
    Object o = e.getObject();
    GraphicalObject obj = null;

    if (o instanceof FamilyMember) {
      Optional<FamilyMemberPanel> m = this.panels.entrySet().stream().map(entry -> entry.getValue()).filter(
          p -> p.getId() == ((FamilyMember) o).getId()).findAny();
      if (m.isPresent())
        obj = m.get();
    }
    else if (o instanceof Relationship) {
      Relationship r = (Relationship) o;
      Optional<Link> link = this.links.stream().filter(
          l -> l.getParent1() == r.getPartner1() || l.getParent2() == r.getPartner2()).findAny();
      if (link.isPresent())
        obj = link.get();
    }

    scrollToObject(obj);
  }

  void reset() {
    deselectAll();
    this.panels.clear();
    this.links.clear();
  }

  void refresh(Family family, Map<Long, CardState> cardsStates) {
    Set<Long> updatedOrAddedPanels = new HashSet<>();
    Set<Long> panelsToDelete = new HashSet<>(this.panels.keySet());

    // Add/update members
    family.getAllMembers().forEach(member -> {
      long id = member.getId();
      CardState state = cardsStates.get(id);

      if (this.panels.containsKey(id)) {
        FamilyMemberPanel panel = this.panels.get(id);

        panel.setInfo(member);
        if (state != null) {
          Point pos = state.getLocation();
          Dimension size = state.getSize();

          if (pos != null)
            panel.setBounds(new Rectangle(pos, panel.getSize()));
          if (size != null)
            panel.setSize(size);
        }
      }
      else {
        FamilyMemberPanel panel = new FamilyMemberPanel(this.view, member);

        if (state != null) {
          Point pos = state.getLocation();
          Dimension size = state.getSize();

          if (pos != null)
            panel.setBounds(new Rectangle(pos, panel.getSize()));
          if (size != null)
            panel.setSize(size);
        }

        this.panels.put(id, panel);

        final int gap = 50;
        Dimension size = this.view.getPreferredSize();
        Rectangle panelBounds = panel.getBounds();
        if (panelBounds.x + panelBounds.width > size.width)
          size.width += panelBounds.x + panelBounds.width - size.width + gap;
        if (panelBounds.y + panelBounds.height > size.height)
          size.height += panelBounds.y + panelBounds.height - size.height + gap;
        if (!size.equals(this.view.getPreferredSize()))
          this.view.setPreferredSize(size);
      }
      updatedOrAddedPanels.add(id);
    });

    // Delete members removed from the model
    panelsToDelete.removeAll(updatedOrAddedPanels);
    panelsToDelete.forEach(id -> this.panels.remove(id));

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Add/update links
    family.getAllRelations().forEach(relation -> {
      FamilyMemberPanel id1 = this.panels.get(relation.getPartner1());
      FamilyMemberPanel id2 = this.panels.get(relation.getPartner2());
      Map<Long, Pair<Boolean, FamilyMemberPanel>> children = new HashMap<>();
      for (Long id : relation.getChildren()) {
        children.put(id, new Pair<>(relation.isAdopted(id), this.panels.get(id)));
      }
      Link link = new Link(this.view, id1, id2, children, relation.isWedding(), relation.hasEnded());

      if (this.links.contains(link)) {
        Link l = this.links.get(this.links.indexOf(link));

        l.setWedding(link.isWedding());
        l.setEnded(relation.hasEnded());
        l.setChildren(link.getChildren());
      }
      else {
        this.links.add(link);
      }
      updatedOrAddedLinks.add(link);
    });

    // Delete links removed from the model
    this.links.retainAll(updatedOrAddedLinks);

  }

  /**
   * Deselects all components and sends a ViewEditEvent.
   */
  void deselectAll() {
    Selection old = getSelection();
    this.selectedObjects.clear();
    updateSelection(old);
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
  Selection getSelection() {
    return new Selection(getSelectedPanels(), getSelectedLinks());
  }

  Stream<FamilyMemberPanel> getPanels() {
    return this.panels.values().stream();
  }

  Stream<Link> getLinks() {
    return this.links.stream();
  }

  Map<Long, CardState> getCardsStates() {
    Map<Long, CardState> states = new HashMap<>();

    for (Map.Entry<Long, FamilyMemberPanel> e : this.panels.entrySet()) {
      states.put(e.getKey(), new CardState(e.getValue().getLocation(), e.getValue().getSize()));
    }

    return states;
  }

  /**
   * Scrolls to the card with the given ID.
   * 
   * @param id the ID
   */
  private void scrollToObject(GraphicalObject obj) {
    if (this.panels.containsValue(obj)) {
      FamilyMemberPanel p = (FamilyMemberPanel) obj;
      Rectangle r = p.getBounds();
      Rectangle v = getView().getCanvasVisibleRect();
      Rectangle r1 = new Rectangle(v.getSize());

      // Centers the component in the panel.
      r1.x = r.x - (v.width - r.width) / 2;
      r1.y = r.y - (v.height - r.height) / 2;
      getView().scrollRectToVisible(r1);
    }
    else if (obj instanceof Link) {
      Link l = (Link) obj;
      Point middle = l.getMiddle();
      Rectangle v = getView().getCanvasVisibleRect();
      Rectangle r = new Rectangle(v.getSize());

      r.x = middle.x - v.width / 2;
      r.y = middle.y - v.height / 2;
      getView().scrollRectToVisible(r);
    }
  }

  Optional<FamilyMemberPanel> getHoveredPanel(Point mouseLocation) {
    for (FamilyMemberPanel m : this.panels.values()) {
      if (m.getBounds().contains(mouseLocation))
        return Optional.of(m);
    }
    return Optional.empty();
  }

  Optional<Link> getHoveredLink(Point mouseLocation) {
    for (Link link : this.links) {
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);

      if (isMouseOnSegment(mouseLocation, p1, p2))
        return Optional.of(link);
    }
    return Optional.empty();
  }

  private boolean isMouseOverObject(Point mouseLocation) {
    return getHoveredPanel(mouseLocation).isPresent() || getHoveredLink(mouseLocation).isPresent();
  }

  /**
   * Tells is the mouse is over a segment.
   * 
   * @param mouseLocation mouse's location
   * @param p1 one end
   * @param p2 the other end
   * @return true if and only if the cursor is within a distance of {@link #HOVER_DISTANCE} pixels
   *         from the link
   * @see <a href=
   *      "https://stackoverflow.com/questions/17581738/check-if-a-point-projected-on-a-line-segment-is-not-outside-it">detect
   *      if point is in segment range</a>
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points">distance
   *      between a point and a line</a>
   */
  private boolean isMouseOnSegment(Point mouseLocation, Point p1, Point p2) {
    double dx = p2.getX() - p1.getX();
    double dy = p2.getY() - p1.getY();
    double innerProduct = (mouseLocation.getX() - p1.getX()) * dx + (mouseLocation.getY() - p1.getY()) * dy;
    boolean mouseInSegmentRange = 0 <= innerProduct && innerProduct <= dx * dx + dy * dy;

    double a = p2.getY() - p1.getY();
    double b = -(p2.getX() - p1.getX());
    double c = -a * p1.getX() - b * p1.getY();
    double d = Math.abs(a * mouseLocation.getX() + b * mouseLocation.getY() + c) / Math.hypot(a, b);

    return mouseInSegmentRange && d <= HOVER_DISTANCE;
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
      selectObjects(this.selectedObjects);
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, current));
    }
  }

  private void selectObjects(List<GraphicalObject> objects) {
    this.panels.forEach((id, p) -> p.deselect());
    this.links.forEach(l -> l.deselect());

    if (!objects.isEmpty()) {
      objects.forEach(o -> o.setSelectedBackground());
      objects.get(objects.size() - 1).setSelected();
    }
    getView().repaint();
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

  /**
   * Returns all panels that are fully inside the given zone.
   * 
   * @param r the zone
   * @return all panels inside the zone
   */
  private List<FamilyMemberPanel> getPanelsInsideRectangle(Rectangle r) {
    if (r != null)
      return this.panels.entrySet().stream().filter(e -> r.contains(e.getValue().getBounds())).map(e -> e.getValue()).collect(
          Collectors.toList());
    return Collections.emptyList();
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
