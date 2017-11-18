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

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.gui.components.canvas_view.member_panel.FamilyMemberPanel;
import net.darmo_creations.jenealogio.gui.components.view.View;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropListener;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropTarget;
import net.darmo_creations.utils.swing.drag_and_drop.DropTargetHandler;

/**
 * This panel displays the family tree and handles click events. It can notify observers of any
 * action occuring.
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
  private MouseAdapter doubleClickController;
  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;

  private Canvas canvas;

  public CanvasView() {
    super(I18n.getLocalizedString("label.canvas.text"), new CanvasViewController());

    this.doubleClickController = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && e.getComponent() instanceof FamilyMemberPanel) {
          FamilyMemberPanel p = (FamilyMemberPanel) e.getComponent();
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDoubleClickEvent(p.getMemberId()));
        }
      }
    };

    this.canvas = new Canvas();
    this.canvas.setLayout(null);
    this.canvas.setPreferredSize(DEFAULT_SIZE);
    this.canvas.addMouseListener(getController());
    this.canvas.addMouseMotionListener(getController());
    this.canvas.addFocusListener(this.controller);
    setViewport(this.canvas);

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();

    this.dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
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
    Set<Long> updatedOrAdded = new HashSet<>();
    Set<Long> keysToDelete = new HashSet<>(this.panels.keySet());

    // Add/update members
    family.getAllMembers().forEach(member -> {
      long id = member.getId();

      if (this.panels.containsKey(id)) {
        FamilyMemberPanel panel = this.panels.get(id);

        panel.setInfo(member, this.config);
        if (positions != null && positions.containsKey(member.getId())) {
          panel.setBounds(new Rectangle(positions.get(member.getId()), panel.getSize()));
        }
        if (sizes != null && sizes.containsKey(member.getId())) {
          panel.setSize(sizes.get(member.getId()));
        }
      }
      else {
        FamilyMemberPanel panel = new FamilyMemberPanel(member, this.config);
        ComponentDragController dragController = new ComponentDragController(this, panel);

        if (positions != null && positions.containsKey(member.getId())) {
          panel.setBounds(new Rectangle(positions.get(member.getId()), panel.getSize()));
        }
        else
          panel.setBounds(new Rectangle(panel.getSize()));
        if (sizes != null && sizes.containsKey(member.getId())) {
          panel.setSize(sizes.get(member.getId()));
        }
        panel.setName("member-" + id);
        panel.addMouseListener(dragController);
        panel.addMouseListener(this.doubleClickController);
        panel.addMouseMotionListener(dragController);
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

        this.canvas.add(panel);
      }
      updatedOrAdded.add(id);
    });

    // Delete members removed from the model
    keysToDelete.removeAll(updatedOrAdded);
    keysToDelete.forEach(id -> {
      this.canvas.remove(this.panels.get(id));
      this.panels.remove(id);
    });

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Add/update links
    family.getAllRelations().forEach(relation -> {
      long id1 = relation.getPartner1();
      long id2 = relation.getPartner2();
      Map<Long, Boolean> children = new HashMap<>();
      for (Long id : relation.getChildren()) {
        children.put(id, relation.isAdopted(id));
      }
      Link link = new Link(id1, id2, children, relation.isWedding(), relation.hasEnded());

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
    List<Link> linksToDelete = new ArrayList<>(this.links);
    linksToDelete.removeAll(updatedOrAddedLinks);
    this.links.removeAll(linksToDelete);

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
  List<Long> getPanelsInsideRectangle(Rectangle r) {
    if (r != null)
      return this.panels.entrySet().stream().filter(e -> r.contains(e.getValue().getBounds())).map(e -> e.getKey()).collect(
          Collectors.toList());
    return Collections.emptyList();
  }

  /**
   * Selects the given panels.
   * 
   * @param ids panels' IDs
   */
  void selectPanels(List<Long> ids) {
    this.panels.forEach((id, p) -> p.setSelected(false));

    if (!ids.isEmpty()) {
      long last = ids.get(ids.size() - 1);

      this.panels.get(last).setSelected(true);
      this.panels.entrySet().stream().filter(e -> ids.contains(e.getKey()) && e.getKey() != last).forEach(
          e -> e.getValue().setSelectedBackground(true));
    }
    revalidate();
    repaint();
  }

  /**
   * Selects the given links.
   */
  void selectLinks(List<Pair<Long, Long>> links) {
    this.links.forEach(l -> l.setSelected(false));

    if (!links.isEmpty())
      this.links.stream().filter(l -> links.contains(new Pair<>(l.getParent1(), l.getParent2()))
          || links.contains(new Pair<>(l.getParent2(), l.getParent1()))).forEach(l -> l.setSelected(true));
    revalidate();
    repaint();
  }

  /**
   * Called when a panel is clicked.
   * 
   * @param id
   * @param keepSelection
   */
  void panelClicked(long id, boolean keepSelection) {
    getController().panelClicked(id, keepSelection);
  }

  /**
   * Called when a card is dragged.
   */
  void cardDragged(long id, Point translation, Point mouseLocation) {
    getController().cardDragged(mouseLocation);
    this.panels.entrySet().stream().filter(
        e -> e.getKey() != id && (e.getValue().isSelectedBackground() || e.getValue().isSelected())).forEach(
            e -> e.getValue().setLocation(e.getValue().getLocation().x + translation.x, e.getValue().getLocation().y + translation.y));
  }

  /**
   * @return an array containing the two partners' IDs from the currently hovered link
   */
  Optional<Pair<Long, Long>> getHoveredLinkPartners() {
    for (Link link : this.links) {
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);

      if (isMouseOnLink(p1, p2)) {
        return Optional.of(new Pair<>(link.getParent1(), link.getParent2()));
      }
    }
    return Optional.empty();
  }

  /**
   * Tells is the mouse is over a link.
   * 
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
  private boolean isMouseOnLink(Point p1, Point p2) {
    Point m = getController().getMouseLocation();
    double dx = p2.getX() - p1.getX();
    double dy = p2.getY() - p1.getY();
    double innerProduct = (m.getX() - p1.getX()) * dx + (m.getY() - p1.getY()) * dy;
    boolean mouseInSegmentRange = 0 <= innerProduct && innerProduct <= dx * dx + dy * dy;

    double a = p2.getY() - p1.getY();
    double b = -(p2.getX() - p1.getX());
    double c = -a * p1.getX() - b * p1.getY();
    Point p = getController().getMouseLocation();
    double d = Math.abs(a * p.getX() + b * p.getY() + c) / Math.hypot(a, b);

    return mouseInSegmentRange && d <= HOVER_DISTANCE;
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
      Point[] points = getLinkCoords(link.get());
      Point middle = points[2];
      Rectangle v = this.canvas.getVisibleRect();
      Rectangle r = new Rectangle(v.getSize());

      r.x = middle.x - v.width / 2;
      r.y = middle.y - v.height / 2;
      this.canvas.scrollRectToVisible(r);
    }
  }

  private Point[] getLinkCoords(Link link) {
    Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
    Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
    Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
    Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);
    Point middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

    return new Point[]{p1, p2, middle};
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
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      if (CanvasView.this.config != null) {
        // Selection
        Optional<Rectangle> optStart = CanvasView.this.getController().getSelectionRectangle();
        if (optStart.isPresent()) {
          Rectangle r = optStart.get();

          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.SELECTION_BACKGROUND_COLOR));
          g2d.fillRect(r.x, r.y, r.width, r.height);
          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.SELECTION_BORDER_COLOR));
          g2d.drawRect(r.x, r.y, r.width, r.height);
        }

        // Links
        CanvasView.this.links.forEach(link -> {
          final int width = link.isWedding() ? 2 : 1;
          if (link.hasEnded())
            g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
          else
            g2d.setStroke(new BasicStroke(width));

          // Link between parents
          Point[] points = getLinkCoords(link);
          Point p1 = points[0];
          Point p2 = points[1];
          Point middle = points[2];

          if (isMouseOnLink(p1, p2))
            g2d.setColor(CanvasView.this.config.getValue(ConfigTags.LINK_HOVERED_COLOR));
          else
            g2d.setColor(link.isSelected() ? CanvasView.this.config.getValue(ConfigTags.LINK_SELECTED_COLOR)
                : CanvasView.this.config.getValue(ConfigTags.LINK_COLOR));
          g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

          g2d.setStroke(new BasicStroke(width));
          // Links to children
          link.getChildren().forEach((id, adopted) -> {
            Rectangle r = CanvasView.this.panels.get(id).getBounds();
            Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);

            g2d.setColor(CanvasView.this.config.getValue(adopted ? ConfigTags.LINK_ADOPTED_CHILD_COLOR : ConfigTags.LINK_CHILD_COLOR));
            g2d.drawLine(middle.x, middle.y, p.x, p.y);
          });
        });
      }
    }
  }

  /**
   * This class represents a link between two cards.
   *
   * @author Damien Vergnet
   */
  private class Link {
    private final long parent1, parent2;
    private boolean wedding;
    private boolean ended;
    private Map<Long, Boolean> children;
    private boolean selected;

    /**
     * Creates a link between {@code parent1} and {@code parent2}.
     * 
     * @param parent1 one parent
     * @param parent2 the other parent
     * @param children the children
     */
    public Link(long parent1, long parent2, Map<Long, Boolean> children, boolean wedding, boolean ended) {
      this.parent1 = parent1;
      this.parent2 = parent2;
      this.children = new HashMap<>(children);
      this.wedding = wedding;
      this.ended = ended;
      this.selected = false;
    }

    public long getParent1() {
      return this.parent1;
    }

    public long getParent2() {
      return this.parent2;
    }

    public Map<Long, Boolean> getChildren() {
      return this.children;
    }

    public void setChildren(Map<Long, Boolean> children) {
      this.children = children;
    }

    public boolean isWedding() {
      return this.wedding;
    }

    public void setWedding(boolean wedding) {
      this.wedding = wedding;
    }

    public boolean hasEnded() {
      return this.ended;
    }

    public void setEnded(boolean ended) {
      this.ended = ended;
    }

    public boolean isSelected() {
      return this.selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;

      result = prime * result + (int) (this.parent1 ^ (this.parent1 >>> 32));
      result = prime * result + (int) (this.parent2 ^ (this.parent2 >>> 32));

      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Link) {
        Link l = (Link) o;
        return l.getParent1() == getParent1() && l.getParent2() == getParent2();
      }

      return false;
    }
  }
}
