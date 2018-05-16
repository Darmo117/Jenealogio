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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.components.canvas_view.GrabHandle.Direction;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.util.Images;

/**
 * This panel represents a family member in the tree display. It can be dragged with the mouse and
 * selected.
 *
 * @author Damien Vergnet
 */
class FamilyMemberPanel extends GraphicalObject {
  private static final Dimension MINIMUM_SIZE = new Dimension(30, 30);
  private static final int INSETS = 10;
  private static final int IMAGE_SIZE = 16;

  private static final Font FONT = new Font("Tahoma", Font.PLAIN, 12);
  private static final Stroke STROKE = new BasicStroke(1);

  private Rectangle bounds;
  private FamilyMember member;

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
    Rectangle2D r = g.getFontMetrics(FONT).getStringBounds(member.toString(), g);

    Dimension size = new Dimension((int) r.getWidth(), (int) r.getHeight());
    size.width += 2 * INSETS + (member.isDead() ? IMAGE_SIZE + INSETS : 0) + 8;
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
  public void handleMoved(Direction direction, Dimension translation) {
    if (direction == Direction.NORTH || direction == Direction.NORTH_EAST || direction == Direction.NORTH_WEST) {
      if (translation.height > 0 && this.bounds.height - translation.height < getMinimumSize().height
          || translation.height < 0 && this.bounds.y + translation.height < 0)
        translation.height = 0;
      this.bounds.y += translation.height;
      this.bounds.height += -translation.height;
    }
    else if (direction == Direction.SOUTH || direction == Direction.SOUTH_EAST || direction == Direction.SOUTH_WEST) {
      this.bounds.height += translation.height;
    }
    if (direction == Direction.WEST || direction == Direction.NORTH_WEST || direction == Direction.SOUTH_WEST) {
      if (translation.width > 0 && this.bounds.width - translation.width < getMinimumSize().width
          || translation.width < 0 && this.bounds.x + translation.width < 0)
        translation.width = 0;
      this.bounds.x += translation.width;
      this.bounds.width += -translation.width;
    }
    else if (direction == Direction.EAST || direction == Direction.NORTH_EAST || direction == Direction.SOUTH_EAST) {
      this.bounds.width += translation.width;
    }
    capBounds();
    updateHandles();
  }

  @Override
  public void paint(Graphics g, WritableConfig config) {
    Graphics2D g2d = (Graphics2D) g;

    if (this.member.isMan())
      g2d.setColor(config.getValue(ConfigTags.GENDER_MALE_COLOR));
    else if (this.member.isWoman())
      g2d.setColor(config.getValue(ConfigTags.GENDER_FEMALE_COLOR));
    else
      g2d.setColor(config.getValue(ConfigTags.GENDER_UNKNOWN_COLOR));

    g2d.fillRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    if (isSelected() || isSelectedBackground())
      g2d.setColor(Color.BLACK);
    else
      g2d.setColor(Color.GRAY);
    g2d.setStroke(STROKE);
    g2d.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);

    String text = this.member.toString();
    Rectangle textZone = this.bounds.getBounds();

    textZone.x += INSETS;
    textZone.width -= 2 * INSETS;

    if (this.member.isDead()) {
      textZone.x += INSETS + IMAGE_SIZE;
      textZone.width -= INSETS + IMAGE_SIZE;
      g2d.drawImage(Images.TOMBSTONE.getImage(), this.bounds.x + INSETS, this.bounds.y + (this.bounds.height - IMAGE_SIZE) / 2, null);
    }

    if (textZone.width >= 0) {
      g2d.setColor(Color.BLACK);
      drawCenteredString(g2d, text, textZone);
    }
  }

  private void drawCenteredString(Graphics2D g2d, String text, Rectangle rect) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setFont(FONT);

    AttributedString as = new AttributedString(text);
    AttributedCharacterIterator aci = as.getIterator();
    FontRenderContext frc = g2d.getFontRenderContext();
    LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
    List<TextLayout> lines = new ArrayList<>();
    float w = rect.width;

    float h = 0;
    while (lbm.getPosition() < aci.getEndIndex()) {
      TextLayout tl = lbm.nextLayout(w);
      lines.add(tl);
      h += tl.getDescent() + tl.getLeading() + tl.getAscent();
      // Ignore lines that are out of bounds
      if (h > rect.height)
        break;
    }

    float y = rect.y + (rect.height - h) / 2;
    for (TextLayout tl : lines) {
      float x = (float) (rect.x + (w - tl.getBounds().getWidth()) / 2);
      tl.draw(g2d, x, y + tl.getAscent());
      y += tl.getDescent() + tl.getLeading() + tl.getAscent();
    }

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
  }

  public Point getLocation() {
    return this.bounds.getLocation();
  }

  public void setLocation(Point p) {
    Point p1 = new Point(p);

    if (p1.x < 0)
      p1.x = 0;
    if (p1.y < 0)
      p1.y = 0;

    this.bounds.setLocation(p1);
    updateHandles();
  }

  public Dimension getSize() {
    return this.bounds.getSize();
  }

  public void setSize(Dimension size) {
    Dimension d = new Dimension(size);

    if (d.width < MINIMUM_SIZE.width)
      d.width = MINIMUM_SIZE.width;
    if (d.height < MINIMUM_SIZE.height)
      d.height = MINIMUM_SIZE.height;

    this.bounds.setSize(d);
    updateHandles();
  }

  public Dimension getMinimumSize() {
    return MINIMUM_SIZE;
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
    this.bounds.setBounds(bounds);
    capBounds();
    updateHandles();
  }

  private void capBounds() {
    if (this.bounds.x < 0)
      this.bounds.x = 0;
    if (this.bounds.y < 0)
      this.bounds.y = 0;
    if (this.bounds.width < MINIMUM_SIZE.width)
      this.bounds.width = MINIMUM_SIZE.width;
    if (this.bounds.height < MINIMUM_SIZE.height)
      this.bounds.height = MINIMUM_SIZE.height;
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
