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

import java.awt.BasicStroke;
import java.awt.Color;
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
import java.util.ArrayList;
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

import net.darmo_creations.config.ColorConfigKey;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.events.CardEvent;
import net.darmo_creations.events.EventsDispatcher;
import net.darmo_creations.events.LinkEvent;
import net.darmo_creations.events.SubsribeEvent;
import net.darmo_creations.gui.components.FamilyMemberPanel;
import net.darmo_creations.gui.drag_and_drop.DropHandler;
import net.darmo_creations.gui.drag_and_drop.DropTargetHandler;
import net.darmo_creations.model.family.Family;

/**
 * This panel displays the family tree and handles click events. It can notify observers of any
 * action occuring.
 *
 * @author Damien Vergnet
 */
public class DisplayPanel extends JPanel implements Scrollable {
  private static final long serialVersionUID = 8747904983365363275L;

  /** The maximum distance away from a link the mouse must be to count as a hover. */
  private static final int HOVER_DISTANCE = 5;

  private GlobalConfig config;
  private DropTarget dropTarget;
  private DisplayController controller;
  private MouseAdapter doubleClickController;
  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;

  public DisplayPanel() {
    setPreferredSize(new Dimension(4000, 4000));
    setLayout(null);

    this.controller = new DisplayController(this);
    this.doubleClickController = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getComponent() instanceof FamilyMemberPanel) {
          FamilyMemberPanel p = (FamilyMemberPanel) e.getComponent();
          EventsDispatcher.EVENT_BUS.dispatchEvent(new CardEvent.DoubleClicked(p.getMemberId()));
        }
      }
    };
    addMouseListener(this.controller);
    addMouseMotionListener(this.controller);

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();

    this.dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
  }

  /**
   * Adds a DropHandler.
   * 
   * @param handler the new handler
   */
  public void addDropHandler(DropHandler handler) {
    try {
      this.dropTarget.addDropTargetListener(new DropTargetHandler(handler, this));
    }
    catch (TooManyListenersException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Resets the panel. All internal components are destroyed.
   */
  public void reset() {
    this.panels.clear();
    this.links.clear();
    removeAll();
  }

  /**
   * Refreshes the display from the given model.
   * 
   * @param family the model
   */
  public void refresh(Family family, GlobalConfig config) {
    refresh(family, new HashMap<>(), config);
  }

  /**
   * Refreshes the display from the given model and updates positions of panels specified in the
   * map.
   * 
   * @param family the model
   * @param positions the positions
   */
  public void refresh(Family family, Map<Long, Point> positions, GlobalConfig config) {
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
          Point p = positions.get(member.getId());
          panel.setBounds(new Rectangle(p, panel.getSize()));
        }
      }
      else {
        FamilyMemberPanel panel = new FamilyMemberPanel(member, this.config);
        DragController dragController = new DragController(this, panel);

        if (positions != null && positions.containsKey(member.getId())) {
          Point p = positions.get(member.getId());
          panel.setBounds(new Rectangle(p, panel.getSize()));
        }
        else
          panel.setBounds(new Rectangle(panel.getSize()));
        panel.setName("member-" + id);
        panel.addMouseListener(dragController);
        panel.addMouseListener(this.doubleClickController);
        panel.addMouseMotionListener(dragController);
        this.panels.put(id, panel);
        add(panel);
      }
      updatedOrAdded.add(id);
    });

    // Delete members removed from the model
    keysToDelete.removeAll(updatedOrAdded);
    keysToDelete.forEach(id -> {
      remove(this.panels.get(id));
      this.panels.remove(id);
    });

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Add/update links
    family.getAllRelations().forEach(relation -> {
      long id1 = relation.getPartner1();
      long id2 = relation.getPartner2();
      Set<Long> children = relation.getChildren().stream().collect(Collectors.toSet());
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
   * Returns all panels that are fully inside the given zone.
   * 
   * @param r the zone
   * @return all panels inside the zone
   */
  public Optional<long[]> getPanelsInsideRectangle(Rectangle r) {
    if (r != null)
      return Optional.of(
          this.panels.entrySet().stream().filter(e -> r.contains(e.getValue().getBounds())).mapToLong(e -> e.getKey()).toArray());
    return Optional.empty();
  }

  /**
   * Selects the given panel. All observers will be notified of the event. Giving -1 will deselect
   * all panels.
   * 
   * @param id panel's ID (member's ID)
   * @param keepSelection if true, the previous selection will be kept
   */
  public void selectPanel(long id, boolean keepSelection) {
    this.panels.forEach((pId, panel) -> panel.setSelected(pId == id));
    this.links.forEach(l -> l.setSelected(false));
    revalidate();
    repaint();
  }

  /**
   * Selects the given panels as background.
   * 
   * @param ids panels' IDs
   */
  public void selectPanelsAsBackground(List<Long> ids) {
    this.panels.entrySet().stream().filter(e -> ids.contains(e.getKey())).forEach(e -> e.getValue().setSelectedBackground(true));
    revalidate();
    repaint();
  }

  @SubsribeEvent
  public void onCardSelected(CardEvent.Clicked e) {
    selectPanel(e.getMemberId(), e.keepPreviousSelection());
  }

  @SubsribeEvent
  public void onLinkClicked(LinkEvent.Clicked e) {
    Optional<Link> optL = this.links.stream().filter(
        l -> l.getParent1() == e.getPartner1Id() && l.getParent2() == e.getPartner2Id()).findAny();

    if (optL.isPresent()) {
      Link link = optL.get();

      this.links.forEach(l -> l.setSelected(false));
      link.setSelected(true);
    }
  }

  /**
   * @return an array containing the two partners' IDs from the currently hovered link
   */
  public Optional<long[]> getHoveredLinkPartners() {
    for (Link link : this.links) {
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);

      if (isMouseOnLink(p1, p2)) {
        return Optional.of(new long[]{link.getParent1(), link.getParent2()});
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
    Point m = this.controller.getMouseLocation();
    double dx = p2.getX() - p1.getX();
    double dy = p2.getY() - p1.getY();
    double innerProduct = (m.getX() - p1.getX()) * dx + (m.getY() - p1.getY()) * dy;
    boolean mouseInSegmentRange = 0 <= innerProduct && innerProduct <= dx * dx + dy * dy;

    double a = p2.getY() - p1.getY();
    double b = -(p2.getX() - p1.getX());
    double c = -a * p1.getX() - b * p1.getY();
    Point p = this.controller.getMouseLocation();
    double d = Math.abs(a * p.getX() + b * p.getY() + c) / Math.hypot(a, b);

    return mouseInSegmentRange && d <= HOVER_DISTANCE;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Selection
    Optional<Rectangle> optStart = this.controller.getSelection();
    if (optStart.isPresent()) {
      Rectangle r = optStart.get();

      g2d.setColor(new Color(185, 213, 241, 128));
      g2d.fillRect(r.x, r.y, r.width, r.height);
      g2d.setColor(new Color(0, 120, 215, 128));
      g2d.drawRect(r.x, r.y, r.width, r.height);
    }

    // Links
    this.links.forEach(link -> {
      final int width = link.isWedding() ? 2 : 1;
      if (link.hasEnded())
        g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
      else
        g2d.setStroke(new BasicStroke(width));

      // Link between parents
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);
      Point middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

      if (isMouseOnLink(p1, p2))
        g2d.setColor(this.config.getValue(ColorConfigKey.LINK_HOVERED));
      else
        g2d.setColor(link.isSelected() ? this.config.getValue(ColorConfigKey.LINK_SELECTED) : this.config.getValue(ColorConfigKey.LINK));
      g2d.drawLine(p1.x, p1.y, p2.x, p2.y);

      g2d.setStroke(new BasicStroke(width));
      g2d.setColor(this.config.getValue(ColorConfigKey.LINK_CHILD));
      // Links to children
      link.getChildren().forEach(child -> {
        Rectangle r = this.panels.get(child).getBounds();
        Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);

        g2d.drawLine(middle.x, middle.y, p.x, p.y);
      });
    });
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return getPreferredSize();
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

  @SubsribeEvent
  public void onPanelDragged(CardEvent.Dragged evt) {
    final Point trans = evt.getTranslation();
    this.panels.entrySet().stream().filter(
        e -> e.getKey() != evt.getMemberId() && (e.getValue().isSelectedBackground() || e.getValue().isSelected())).forEach(
            e -> e.getValue().setLocation(e.getValue().getLocation().x + trans.x, e.getValue().getLocation().y + trans.y));
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
    private Set<Long> children;
    private boolean selected;

    /**
     * Creates a link between {@code parent1} and {@code parent2}.
     * 
     * @param parent1 one parent
     * @param parent2 the other parent
     * @param children the children
     */
    public Link(long parent1, long parent2, Set<Long> children, boolean wedding, boolean ended) {
      this.parent1 = parent1;
      this.parent2 = parent2;
      this.children = new HashSet<>(children);
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

    public Set<Long> getChildren() {
      return this.children;
    }

    public void setChildren(Set<Long> children) {
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
