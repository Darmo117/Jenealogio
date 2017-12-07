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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.components.view.View;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropListener;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropTarget;
import net.darmo_creations.utils.swing.drag_and_drop.DropTargetHandler;

/**
 * This view displays the family tree.
 *
 * @author Damien Vergnet
 */
public class CanvasView extends View implements Scrollable, DragAndDropTarget {
  private static final long serialVersionUID = 8747904983365363275L;

  private static final Dimension DEFAULT_SIZE = new Dimension(4000, 4000);
  /** The maximum distance away from a link the mouse must be to count as a hover. */
  private static final int HOVER_DISTANCE = 5;

  private WritableConfig config;
  private DropTarget dropTarget;
  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;

  private Canvas canvas;
  private boolean resizing;

  public CanvasView() {
    super(I18n.getLocalizedString("label.canvas.text"), new CanvasViewController());

    this.canvas = new Canvas();
    this.canvas.setLayout(null);
    this.canvas.setPreferredSize(DEFAULT_SIZE);
    this.canvas.addMouseListener(this.controller);
    this.canvas.addMouseMotionListener(this.controller);
    this.canvas.addFocusListener(this.controller);

    ComponentDragController dragController = new ComponentDragController(this);
    this.canvas.addMouseListener(dragController);
    this.canvas.addMouseMotionListener(dragController);

    setViewport(this.canvas);

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();

    this.dropTarget = new DropTarget(this.canvas, DnDConstants.ACTION_COPY_OR_MOVE, null);
  }

  /**
   * Adds a drag-and-drop listener.
   * 
   * @param handler the new handler
   */
  @Override
  public void addDragAndDropListener(DragAndDropListener l) {
    try {
      this.dropTarget.addDropTargetListener(new DropTargetHandler(l, this));
    }
    catch (TooManyListenersException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Resets the panel. All internal components are destroyed.
   */
  public void reset() {
    getController().deselectAll();
    this.panels.clear();
    this.links.clear();

    this.canvas.removeAll();
    this.canvas.setPreferredSize(DEFAULT_SIZE);
    revalidate();
    repaint();
  }

  /**
   * Refreshes the display from the given model.
   * 
   * @param family the model
   */
  public void refresh(Family family, WritableConfig config) {
    refresh(family, Collections.emptyMap(), Collections.emptyMap(), config);
  }

  /**
   * Refreshes the display from the given model and updates positions of panels specified in the
   * map.
   * 
   * @param family the model
   * @param positions the positions
   */
  public void refresh(Family family, Map<Long, Point> positions, Map<Long, Dimension> sizes, WritableConfig config) {
    this.config = config;
    Set<Long> updatedOrAddedPanels = new HashSet<>();
    Set<Long> panelsToDelete = new HashSet<>(this.panels.keySet());

    // Add/update members
    family.getAllMembers().forEach(member -> {
      long id = member.getId();

      if (this.panels.containsKey(id)) {
        FamilyMemberPanel panel = this.panels.get(id);

        panel.setInfo(member);
        if (positions != null && positions.containsKey(member.getId())) {
          panel.setBounds(new Rectangle(positions.get(member.getId()), panel.getSize()));
        }
        if (sizes != null && sizes.containsKey(member.getId())) {
          panel.setSize(sizes.get(member.getId()));
        }
      }
      else {
        FamilyMemberPanel panel = new FamilyMemberPanel(this.canvas, member);

        if (positions != null && positions.containsKey(member.getId())) {
          panel.setBounds(new Rectangle(positions.get(member.getId()), panel.getSize()));
        }
        else
          panel.setBounds(new Rectangle(panel.getSize()));
        if (sizes != null && sizes.containsKey(member.getId())) {
          panel.setSize(sizes.get(member.getId()));
        }
        this.panels.put(id, panel);

        final int gap = 50;
        Dimension size = this.canvas.getPreferredSize();
        Rectangle panelBounds = panel.getBounds();
        if (panelBounds.x + panelBounds.width > size.width)
          size.width += panelBounds.x + panelBounds.width - size.width + gap;
        if (panelBounds.y + panelBounds.height > size.height)
          size.height += panelBounds.y + panelBounds.height - size.height + gap;
        if (!size.equals(this.canvas.getPreferredSize()))
          this.canvas.setPreferredSize(size);
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
      Link link = new Link(this, id1, id2, children, relation.isWedding(), relation.hasEnded());

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

    revalidate();
    repaint();
  }

  @Override
  public void deselectAll() {
    getController().deselectAll();
  }

  @Override
  public Selection getSelection() {
    return getController().getSelection();
  }

  /**
   * @return the positions of all panels
   */
  public Map<Long, Point> getCardsPositions() {
    Map<Long, Point> points = new HashMap<>();

    for (Long id : this.panels.keySet()) {
      points.put(id, this.panels.get(id).getLocation());
    }

    return points;
  }

  /**
   * @return the sizes of all cards
   */
  public Map<Long, Dimension> getCardsSizes() {
    Map<Long, Dimension> sizes = new HashMap<>();

    for (Long id : this.panels.keySet()) {
      sizes.put(id, this.panels.get(id).getSize());
    }

    return sizes;
  }

  /**
   * Returns all panels that are fully inside the given zone.
   * 
   * @param r the zone
   * @return all panels inside the zone
   */
  List<FamilyMemberPanel> getPanelsInsideRectangle(Rectangle r) {
    if (r != null)
      return this.panels.entrySet().stream().filter(e -> r.contains(e.getValue().getBounds())).map(e -> e.getValue()).collect(
          Collectors.toList());
    return Collections.emptyList();
  }

  void selectObjects(List<GraphicalObject> objects) {
    this.panels.forEach((id, p) -> p.deselect());
    this.links.forEach(l -> l.deselect());

    if (!objects.isEmpty()) {
      objects.forEach(o -> o.setSelectedBackground());
      objects.get(objects.size() - 1).setSelected();
    }
    repaint();
  }

  /**
   * Called when a card is dragged.
   */
  void cardDragged(long id, Point translation, Point mouseLocation) {
    getController().cardDragged(translation, mouseLocation);
  }

  boolean isResizing() {
    return this.resizing;
  }

  void resizing(boolean resizing) {
    this.resizing = resizing;
  }

  public Rectangle getCanvasBounds() {
    return this.canvas.getBounds();
  }

  public void setCanvasPreferredSize(Dimension size) {
    this.canvas.setPreferredSize(size);
  }

  Point getCanvasOffset() {
    return this.canvas.getLocationOnScreen();
  }

  boolean isMouseOverObject(Point mouseLocation) {
    return getHoveredPanel(mouseLocation).isPresent() || getHoveredLink(mouseLocation).isPresent();
  }

  Optional<FamilyMemberPanel> getHoveredPanel(Point mouseLocation) {
    for (Map.Entry<Long, FamilyMemberPanel> m : this.panels.entrySet()) {
      if (m.getValue().getBounds().contains(mouseLocation))
        return Optional.of(m.getValue());
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

  public GrabHandle isPointOnHandle(Point point) {
    return this.panels.values().stream().map(p -> p.isOnHandle(point)).filter(h -> h != null).findAny().orElse(null);
  }

  /**
   * Scrolls to the card with the given ID.
   * 
   * @param id the ID
   */
  public void scrollToPanel(long id) {
    FamilyMemberPanel p = this.panels.get(id);

    if (p != null) {
      Rectangle r = p.getBounds();
      Rectangle v = this.canvas.getVisibleRect();
      Rectangle r1 = new Rectangle(v.getSize());

      // Centers the component in the panel.
      r1.x = r.x - (v.width - r.width) / 2;
      r1.y = r.y - (v.height - r.height) / 2;
      this.canvas.scrollRectToVisible(r1);
    }
  }

  /**
   * Scrolls to the given link.
   * 
   * @param id1 partner 1 ID
   * @param id2 partner 2 ID
   */
  public void scrollToLink(long id1, long id2) {
    Optional<Link> link = this.links.stream().filter(l -> l.getParent1() == id1 && l.getParent2() == id2).findAny();

    if (link.isPresent()) {
      Point middle = link.get().getMiddle();
      Rectangle v = this.canvas.getVisibleRect();
      Rectangle r = new Rectangle(v.getSize());

      r.x = middle.x - v.width / 2;
      r.y = middle.y - v.height / 2;
      this.canvas.scrollRectToVisible(r);
    }
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return this.canvas.getPreferredSize();
  }

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 16;
  }

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 25;
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  @Override
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }

  /**
   * Exports this panel to an image.
   * 
   * @return this panel as an image
   */
  public BufferedImage exportToImage() {
    BufferedImage image = new BufferedImage(this.canvas.getWidth(), this.canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
    this.canvas.paint(image.createGraphics());

    Point p1 = getTopLeftPoint();
    Point p2 = getBottomRightPoint();

    return image.getSubimage(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
  }

  /**
   * @return the topmost leftmost point of the tree
   */
  private Point getTopLeftPoint() {
    Point point = new Point(4000, 4000);

    this.panels.values().forEach(p -> {
      Point l = p.getLocation();
      point.x = Math.min(point.x, l.x);
      point.y = Math.min(point.y, l.y);
    });

    return point;
  }

  /**
   * @return the bottommost rightmost point of the tree
   */
  private Point getBottomRightPoint() {
    Point point = new Point();

    this.panels.values().forEach(p -> {
      Rectangle r = p.getBounds();
      point.x = Math.max(point.x, r.x + r.width);
      point.y = Math.max(point.y, r.y + r.height);
    });

    return point;
  }

  private CanvasViewController getController() {
    return (CanvasViewController) this.controller;
  }

  private class Canvas extends JPanel {
    private static final long serialVersionUID = -7308185736594294332L;

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      if (CanvasView.this.config != null) {
        Optional<Rectangle> selection = CanvasView.this.getController().getSelectionRectangle();

        if (selection.isPresent()) {
          Rectangle r = selection.get();

          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.ZONE_SELECTION_BACKGROUND_COLOR));
          g2d.fillRect(r.x, r.y, r.width, r.height);
          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.ZONE_SELECTION_BORDER_COLOR));
          g2d.drawRect(r.x, r.y, r.width, r.height);
        }

        CanvasView.this.links.forEach(link -> link.paintComponent(g2d, CanvasView.this.config));
        CanvasView.this.panels.forEach((id, p) -> p.paintComponent(g2d, CanvasView.this.config));
      }
    }
  }
}
