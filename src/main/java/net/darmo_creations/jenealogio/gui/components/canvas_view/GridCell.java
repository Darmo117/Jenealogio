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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.config.WritableConfig;

class GridCell extends GraphicalObject {
  private static final Color BORDER_COLOR = new Color(200, 200, 200);
  private static final Color TARGET_COLOR = new Color(179, 224, 255);

  private Point gridLocation;
  private Rectangle bounds, defaultBounds;
  private boolean isDropTarget;

  private FamilyMemberPanel panel;

  public GridCell(JComponent parent, int id, Point gridLocation, Rectangle bounds) {
    super(parent, id);
    this.gridLocation = gridLocation;
    this.bounds = bounds.getBounds();
    this.defaultBounds = bounds.getBounds();
    this.isDropTarget = false;
  }

  public Point getGridLocation() {
    return this.gridLocation;
  }

  public Rectangle getBounds() {
    return this.bounds.getBounds();
  }

  public void setBounds(Rectangle bounds) {
    this.bounds = bounds.getBounds();
    updatePanel();
  }

  public Dimension getSize() {
    return this.bounds.getSize();
  }

  public void setSize(Dimension size) {
    this.bounds.setSize(size);
    updatePanel();
  }

  public int getWidth() {
    return this.bounds.width;
  }

  public void setWidth(int width) {
    this.bounds.width = width;
    updatePanel();
  }

  public int getHeight() {
    return this.bounds.height;
  }

  public void setHeight(int height) {
    this.bounds.height = height;
    updatePanel();
  }

  public Point getLocation() {
    return this.bounds.getLocation();
  }

  public void setLocation(Point p) {
    this.bounds.setLocation(p);
    updatePanel();
  }

  public boolean isDropTarget() {
    return this.isDropTarget;
  }

  public void setDropTarget(boolean isDropTarget) {
    this.isDropTarget = isDropTarget;
  }

  public boolean isEmpty() {
    return this.panel == null;
  }

  public FamilyMemberPanel getPanel() {
    return this.panel;
  }

  public void setPanel(FamilyMemberPanel panel) {
    this.panel = panel;
    updatePanel();
  }

  public void pack() {
    if (this.panel != null) {
      Dimension d = this.panel.getPreferredSize();
      this.bounds.setSize(new Dimension(d.width + 2, d.height + 2));
    }
    else
      this.bounds.setBounds(this.defaultBounds);
  }

  private void updatePanel() {
    if (this.panel != null) {
      this.panel.setLocationInGrid(this.gridLocation);
      this.panel.setBounds(new Rectangle(this.bounds.x + 1, this.bounds.y + 1, this.bounds.width - 2, this.bounds.height - 2));
    }
  }

  @Override
  protected void paint(Graphics2D g, WritableConfig config) {
    if (this.isDropTarget) {
      g.setColor(TARGET_COLOR);
      g.fillRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    }
    g.setColor(BORDER_COLOR);
    g.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    g.drawString(String.format("%d, %d", this.gridLocation.x, this.gridLocation.y), this.bounds.x + 5, this.bounds.y + 10); // DEBUG
  }
}
