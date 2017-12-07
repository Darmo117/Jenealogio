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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.Objects;

/**
 * This class represents a grab handle for resizing panels.
 *
 * @author Damien Vergnet
 */
class GrabHandle {
  private static final Stroke STROKE = new BasicStroke(1);
  private static final int SIZE = 4;

  private final FamilyMemberPanel panel;
  private final Direction direction;
  private Rectangle bounds;
  private boolean backgroundSelected;

  public GrabHandle(FamilyMemberPanel panel, Point location, Direction direction) {
    this.panel = panel;
    this.bounds = new Rectangle(location, new Dimension(SIZE, SIZE));
    this.backgroundSelected = false;
    this.direction = Objects.requireNonNull(direction);
  }

  public FamilyMemberPanel getPanel() {
    return this.panel;
  }

  public void setBackgroundSelected(boolean backgroundSelected) {
    this.backgroundSelected = backgroundSelected;
  }

  /**
   * Sets the location of this handle. To translate when grabbed with the mouse,use
   * {@link #translate(Point)}.
   * 
   * @param location the new location
   */
  public void setLocation(Point location) {
    this.bounds.setLocation(location);
  }

  /**
   * Moves this handle and resizes the associated component.
   * 
   * @param p translation amount
   */
  public void translate(Point p) {
    Rectangle bounds = this.panel.getBounds();

    if (this.direction == Direction.NORTH || this.direction == Direction.NORTH_EAST || this.direction == Direction.NORTH_WEST) {
      bounds.y += p.y;
      bounds.height += -p.y;
    }
    if (this.direction == Direction.SOUTH || this.direction == Direction.SOUTH_EAST || this.direction == Direction.SOUTH_WEST) {
      bounds.height += p.y;
    }
    if (this.direction == Direction.EAST || this.direction == Direction.NORTH_EAST || this.direction == Direction.SOUTH_EAST) {
      bounds.width += p.x;
    }
    if (this.direction == Direction.WEST || this.direction == Direction.NORTH_WEST || this.direction == Direction.SOUTH_WEST) {
      bounds.x += p.x;
      bounds.width += -p.x;
    }

    this.panel.setBounds(bounds);
  }

  public int getSize() {
    return SIZE;
  }

  public Direction getDirection() {
    return this.direction;
  }

  public Rectangle getBounds() {
    return this.bounds.getBounds();
  }

  public Cursor getCursor() {
    return this.direction.getCursor();
  }

  public void draw(Graphics2D g) {
    g.setStroke(STROKE);
    if (this.backgroundSelected)
      g.setColor(Color.WHITE);
    else
      g.setColor(Color.BLACK);
    int x = this.bounds.x - SIZE / 2;
    int y = this.bounds.y - SIZE / 2;
    g.fillRect(x, y, SIZE, SIZE);
    g.setColor(Color.BLACK);
    g.drawRect(x, y, SIZE, SIZE);
  }

  public static enum Direction {
    NORTH(Cursor.N_RESIZE_CURSOR),
    NORTH_EAST(Cursor.NE_RESIZE_CURSOR),
    NORTH_WEST(Cursor.NW_RESIZE_CURSOR),
    SOUTH(Cursor.S_RESIZE_CURSOR),
    SOUTH_EAST(Cursor.SE_RESIZE_CURSOR),
    SOUTH_WEST(Cursor.SW_RESIZE_CURSOR),
    EAST(Cursor.E_RESIZE_CURSOR),
    WEST(Cursor.W_RESIZE_CURSOR);

    private final Cursor cursor;

    private Direction(int cursorType) {
      this.cursor = Cursor.getPredefinedCursor(cursorType);
    }

    public Cursor getCursor() {
      return this.cursor;
    }

    public boolean isHorizontal() {
      return this == EAST || this == WEST;
    }

    public boolean isVertical() {
      return this == NORTH || this == SOUTH;
    }

    public boolean isDiagonal() {
      return this == NORTH_EAST || this == NORTH_WEST || this == SOUTH_EAST || this == SOUTH_WEST;
    }
  }
}
