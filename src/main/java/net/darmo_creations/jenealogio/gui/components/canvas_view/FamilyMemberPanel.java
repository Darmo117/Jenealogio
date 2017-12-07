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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.util.Images;

/**
 * This panel represents a family member in the tree display. It can be dragged with the mouse and
 * selected.
 *
 * @author Damien Vergnet
 */
class FamilyMemberPanel extends GraphicalObject {
  private static final Stroke STROKE = new BasicStroke(1);
  private static final int IMAGE_OFFSET = 8;
  private static final int IMAGE_SIZE = 16;
  private static final Font FONT = new Font("Tahoma", Font.PLAIN, 12);

  private Rectangle bounds;
  private FamilyMember member;
  private GrabHandle[] handles;

  /**
   * Creates a panel for the given person.
   * 
   * @param parent the parent of this component
   * @param member the member to display
   */
  public FamilyMemberPanel(JComponent parent, FamilyMember member) {
    super(parent, member.getId());
    this.bounds = new Rectangle();
    this.handles = new GrabHandle[8];
    for (int i = 0; i < this.handles.length; i++) {
      this.handles[i] = new GrabHandle(this, new Point(), GrabHandle.Direction.values()[i]);
    }
    setInfo(member);
  }

  /**
   * Sets the data to display.
   * 
   * @param member the member to display
   * @param config the config
   */
  public void setInfo(FamilyMember member) {
    this.member = member;

    Graphics g = getParent().getGraphics();
    FontMetrics metrics = g.getFontMetrics(FONT);
    Rectangle2D r = metrics.getStringBounds(member.toString(), g);
    Dimension size = new Dimension((int) r.getWidth(), (int) r.getHeight());
    size.width += 20 + (member.isDead() ? IMAGE_OFFSET + IMAGE_SIZE : 0);
    size.height = 30;
    setSize(size);
  }

  @Override
  public void setSelected() {
    super.setSelected();
    for (GrabHandle h : this.handles)
      h.setBackgroundSelected(false);
  }

  @Override
  public void setSelectedBackground() {
    super.setSelectedBackground();
    for (GrabHandle h : this.handles)
      h.setBackgroundSelected(true);
  }

  @Override
  public void paintComponent(Graphics g, WritableConfig config) {
    Graphics2D g2d = (Graphics2D) g;

    if (this.member.isMan())
      g2d.setColor(config.getValue(ConfigTags.GENDER_MALE_COLOR));
    else if (this.member.isWoman())
      g2d.setColor(config.getValue(ConfigTags.GENDER_FEMALE_COLOR));
    else
      g2d.setColor(config.getValue(ConfigTags.GENDER_UNKNOWN_COLOR));

    g2d.fillRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(STROKE);
    g2d.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);

    String text = this.member.toString();
    Rectangle textZone = this.bounds.getBounds();

    if (this.member.isDead()) {
      textZone.x += IMAGE_OFFSET + IMAGE_SIZE;
      textZone.width -= IMAGE_OFFSET + IMAGE_SIZE;
      g2d.drawImage(Images.TOMBSTONE.getImage(), this.bounds.x + IMAGE_OFFSET, this.bounds.y + (this.bounds.height - IMAGE_SIZE) / 2, null);
    }
    g2d.setColor(Color.BLACK);
    drawCenteredString(g2d, text, textZone);

    if (isSelected() || isSelectedBackground())
      for (GrabHandle h : this.handles)
        h.draw(g2d);
  }

  public void drawCenteredString(Graphics2D g2d, String text, Rectangle rect) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    FontMetrics metrics = g2d.getFontMetrics(FONT);
    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
    g2d.setFont(FONT);
    g2d.drawString(text, x, y);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }

  private void drawTextUgly(String text, FontMetrics textMetrics, Graphics2D g2) {
    int lineHeight = textMetrics.getHeight();
    String textToDraw = text;
    String[] arr = textToDraw.split(" ");
    int nIndex = 0;
    int startX = 319;
    int startY = 113;

    while (nIndex < arr.length) {
      String line = arr[nIndex++];
      while ((nIndex < arr.length) && (textMetrics.stringWidth(line + " " + arr[nIndex]) < 447)) {
        line = line + " " + arr[nIndex];
        nIndex++;
      }
      g2.drawString(line, startX, startY);
      startY = startY + lineHeight;
    }
  }

  public GrabHandle isOnHandle(Point point) {
    for (GrabHandle h : this.handles) {
      if (h.getBounds().contains(point))
        return h;
    }
    return null;
  }

  public Point getLocation() {
    return this.bounds.getLocation();
  }

  public void setLocation(Point p) {
    if (p.x < 0)
      p.x = 0;
    if (p.y < 0)
      p.y = 0;
    if (p.x + this.bounds.width > getParent().getWidth())
      p.x = getParent().getWidth() - this.bounds.width;
    if (p.y + this.bounds.height > getParent().getHeight())
      p.y = getParent().getHeight() - this.bounds.height;
    this.bounds.setLocation(p);
    updateHandles();
  }

  public Dimension getSize() {
    return this.bounds.getSize();
  }

  public void setSize(Dimension size) {
    if (this.bounds.x + size.width > getParent().getWidth())
      size.width = getParent().getWidth() - this.bounds.x;
    if (this.bounds.y + size.height > getParent().getHeight())
      size.height = getParent().getHeight() - this.bounds.y;
    this.bounds.setSize(size);
    updateHandles();
  }

  public int getWidth() {
    return this.bounds.width;
  }

  public int getHeight() {
    return this.bounds.height;
  }

  public Rectangle getBounds() {
    return new Rectangle(this.bounds);
  }

  public void setBounds(Rectangle bounds) {
    if (bounds.x < 0) {
      bounds.width += bounds.x;
      bounds.x = 0;
    }
    if (bounds.y < 0) {
      bounds.height += bounds.y;
      bounds.y = 0;
    }

    if (bounds.x + bounds.width > getParent().getWidth())
      bounds.x = getParent().getWidth() - this.bounds.width;
    if (bounds.y + bounds.height > getParent().getHeight())
      bounds.y = getParent().getHeight() - this.bounds.height;

    if (bounds.x + bounds.width > getParent().getWidth())
      bounds.width = getParent().getWidth() - bounds.x;
    if (bounds.y + bounds.height > getParent().getHeight())
      bounds.height = getParent().getHeight() - bounds.y;

    this.bounds.setBounds(bounds);
    updateHandles();
  }

  private void updateHandles() {
    int westX = this.bounds.x;
    int eastX = this.bounds.x + this.bounds.width;
    int northY = this.bounds.y;
    int southY = this.bounds.y + this.bounds.height;
    int halfW = this.bounds.x + this.bounds.width / 2;
    int halfH = this.bounds.y + this.bounds.height / 2;

    this.handles[GrabHandle.Direction.NORTH.ordinal()].setLocation(new Point(halfW, northY));
    this.handles[GrabHandle.Direction.NORTH_EAST.ordinal()].setLocation(new Point(eastX, northY));
    this.handles[GrabHandle.Direction.EAST.ordinal()].setLocation(new Point(eastX, halfH));
    this.handles[GrabHandle.Direction.SOUTH_EAST.ordinal()].setLocation(new Point(eastX, southY));
    this.handles[GrabHandle.Direction.SOUTH.ordinal()].setLocation(new Point(halfW, southY));
    this.handles[GrabHandle.Direction.SOUTH_WEST.ordinal()].setLocation(new Point(westX, southY));
    this.handles[GrabHandle.Direction.WEST.ordinal()].setLocation(new Point(westX, halfH));
    this.handles[GrabHandle.Direction.NORTH_WEST.ordinal()].setLocation(new Point(westX, northY));
  }
}
