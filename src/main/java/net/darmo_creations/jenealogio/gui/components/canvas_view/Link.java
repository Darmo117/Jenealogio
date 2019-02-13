/*
 * Copyright © 2018 Damien Vergnet
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
  private final FamilyMemberPanel parent1, parent2;
  private boolean wedding;
  private boolean ended;
  private Map<Long, Pair<Boolean, FamilyMemberPanel>> children;

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
  public Link(JComponent parent, FamilyMemberPanel parent1, FamilyMemberPanel parent2, Map<Long, Pair<Boolean, FamilyMemberPanel>> children,
      boolean wedding, boolean ended) {
    super(parent, parent1.getId() ^ parent2.getId());
    this.parent1 = parent1;
    this.parent2 = parent2;
    this.children = new HashMap<>(children);
    this.wedding = wedding;
    this.ended = ended;
  }

  public long getParent1() {
    return this.parent1.getId();
  }

  public long getParent2() {
    return this.parent2.getId();
  }

  public Map<Long, Pair<Boolean, FamilyMemberPanel>> getChildren() {
    return this.children;
  }

  public void setChildren(Map<Long, Pair<Boolean, FamilyMemberPanel>> children) {
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
  public void paint(Graphics2D g, WritableConfig config) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    final int width = isWedding() ? 2 : 1;
    if (hasEnded())
      g.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
    else
      g.setStroke(new BasicStroke(width));

    if (isSelected())
      g.setColor(SELECTION_COLOR);
    else if (isSelectedBackground())
      g.setColor(BG_SELECTION_COLOR);
    else
      g.setColor(config.getValue(ConfigTags.LINK_COLOR));

    Point[] points = getPoints();
    Point end1 = points[0];
    Point middle = points[1];
    Point end2 = points[2];
    g.drawLine(end1.x, end1.y, end2.x, end2.y);

    // Links to children
    this.children.forEach((id, pair) -> {
      boolean adopted = pair.getValue1();
      Rectangle r = pair.getValue2().getBounds();
      Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);

      g.setColor(config.getValue(adopted ? ConfigTags.LINK_ADOPTED_CHILD_COLOR : ConfigTags.LINK_CHILD_COLOR));
      g.drawLine(middle.x, middle.y, p.x, p.y);
    });
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
  }

  private Point[] getPoints() {
    Rectangle r1 = this.parent1.getBounds();
    Rectangle r2 = this.parent2.getBounds();
    Point end1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
    Point end2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);
    Point middle = new Point((end1.x + end2.x) / 2, (end1.y + end2.y) / 2);

    return new Point[] { end1, middle, end2 };
  }

  @Override
  public long getId() {
    return hashCode();
  }

  public Point getMiddle() {
    return getPoints()[1];
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (int) (this.parent1.getId() ^ (this.parent1.getId() >>> 32));
    result = prime * result + (int) (this.parent2.getId() ^ (this.parent2.getId() >>> 32));

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

  enum Side {
    TOP,
    BOTTOM,
    LEFT,
    RIGHT;
  }
}