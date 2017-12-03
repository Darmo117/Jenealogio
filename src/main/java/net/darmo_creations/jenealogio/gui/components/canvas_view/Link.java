package net.darmo_creations.jenealogio.gui.components.canvas_view;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.util.Pair;

/**
 * This class represents a link between two cards.
 *
 * @author Damien Vergnet
 */
class Link extends GraphicalObject {
  private Point end1, end2, middle;
  private final Pair<Long, Rectangle> parent1, parent2;
  private boolean wedding;
  private boolean ended;
  private Map<Long, Pair<Boolean, Rectangle>> children;

  /**
   * Creates a link between {@code parent1} and {@code parent2}.
   * 
   * @param parent the parent component
   * @param parent1 one parent
   * @param parent2 the other parent
   * @param children the children
   * @param wedding indicate if the link is a wedding
   * @param ended indicate if the link has ended
   */
  public Link(JComponent parent, Pair<Long, Rectangle> parent1, Pair<Long, Rectangle> parent2, Map<Long, Pair<Boolean, Rectangle>> children,
      boolean wedding, boolean ended) {
    super(parent, parent1.getValue1() ^ parent2.getValue1());
    this.parent1 = parent1;
    this.parent2 = parent2;
    this.children = new HashMap<>(children);
    this.wedding = wedding;
    this.ended = ended;
    updateLinkCoords(parent1.getValue2(), parent2.getValue2());
  }

  public long getParent1() {
    return this.parent1.getValue1();
  }

  public long getParent2() {
    return this.parent2.getValue1();
  }

  public Map<Long, Pair<Boolean, Rectangle>> getChildren() {
    return this.children;
  }

  public void setChildren(Map<Long, Pair<Boolean, Rectangle>> children) {
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

  @Override
  public void paintComponent(Graphics g, WritableConfig config) {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    final int width = isWedding() ? 2 : 1;
    if (hasEnded())
      g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
    else
      g2d.setStroke(new BasicStroke(width));

    if (isHovered())
      g2d.setColor(config.getValue(ConfigTags.LINK_HOVERED_COLOR));
    else if (isSelected())
      g2d.setColor(config.getValue(ConfigTags.LINK_SELECTED_COLOR));
    else if (isSelectedBackground())
      g2d.setColor(config.getValue(ConfigTags.LINK_SELECTED_BACKGROUND_COLOR));
    else
      g2d.setColor(config.getValue(ConfigTags.LINK_COLOR));

    g2d.drawLine(this.end1.x, this.end1.y, this.end2.x, this.end2.y);

    // Links to children
    this.children.forEach((id, pair) -> {
      boolean adopted = pair.getValue1();
      Rectangle r = pair.getValue2();
      Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);

      g2d.setColor(config.getValue(adopted ? ConfigTags.LINK_ADOPTED_CHILD_COLOR : ConfigTags.LINK_CHILD_COLOR));
      g2d.drawLine(this.middle.x, this.middle.y, p.x, p.y);
    });
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
  }

  public void updateLinkCoords(Rectangle parent1, Rectangle parent2) {
    this.end1 = new Point(parent1.x + parent1.width / 2, parent1.y + parent1.height / 2);
    this.end2 = new Point(parent2.x + parent2.width / 2, parent2.y + parent2.height / 2);
    this.middle = new Point((this.end1.x + this.end2.x) / 2, (this.end1.y + this.end2.y) / 2);
  }

  @Override
  public long getId() {
    return hashCode();
  }

  public Point getMiddle() {
    return this.middle;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (int) (this.parent1.getValue1() ^ (this.parent1.getValue1() >>> 32));
    result = prime * result + (int) (this.parent2.getValue1() ^ (this.parent2.getValue1() >>> 32));

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