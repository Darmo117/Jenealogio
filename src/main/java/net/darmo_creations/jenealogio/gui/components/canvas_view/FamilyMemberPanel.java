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

  private Dimension preferredSize;
  private Rectangle bounds;
  private FamilyMember member;
  /** Not used to draw */
  private Point locationInGrid;

  /**
   * Creates a panel for the given person.
   * 
   * @param parent the parent of this component
   * @param member the member to display
   */
  public FamilyMemberPanel(JComponent parent, FamilyMember member) {
    super(parent, member.getId());
    this.bounds = new Rectangle();
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
    int w = (int) r.getWidth() + 2 * INSETS + (member.isDead() ? IMAGE_SIZE + INSETS : 0) + 8;

    this.preferredSize = new Dimension(w, 30);
    setSize(this.preferredSize);
  }

  public Dimension getPreferredSize() {
    return this.preferredSize;
  }

  @Override
  public void paint(Graphics2D g, WritableConfig config) {
    if (this.member.isMan())
      g.setColor(config.getValue(ConfigTags.GENDER_MALE_COLOR));
    else if (this.member.isWoman())
      g.setColor(config.getValue(ConfigTags.GENDER_FEMALE_COLOR));
    else
      g.setColor(config.getValue(ConfigTags.GENDER_UNKNOWN_COLOR));

    g.fillRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    if (isSelected())
      g.setColor(SELECTION_COLOR);
    else if (isSelectedBackground())
      g.setColor(BG_SELECTION_COLOR);
    else
      g.setColor(Color.DARK_GRAY);
    g.setStroke(STROKE);
    g.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);

    String text = this.member.toString();
    Rectangle textZone = this.bounds.getBounds();

    textZone.x += INSETS;
    textZone.width -= 2 * INSETS;

    if (this.member.isDead()) {
      textZone.x += INSETS + IMAGE_SIZE;
      textZone.width -= INSETS + IMAGE_SIZE;
      g.drawImage(Images.TOMBSTONE.getImage(), this.bounds.x + INSETS, this.bounds.y + (this.bounds.height - IMAGE_SIZE) / 2, null);
    }

    if (textZone.width >= 0) {
      g.setColor(Color.BLACK);
      drawCenteredString(g, text, textZone);
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

  public Point getLocationInGrid() {
    return this.locationInGrid != null ? this.locationInGrid.getLocation() : null;
  }

  public void setLocationInGrid(Point locationInGrid) {
    this.locationInGrid = locationInGrid.getLocation();
  }

  public Point getLocation() {
    return this.bounds.getLocation();
  }

  public void setLocation(Point p) {
    this.bounds.setLocation(p);
    capBounds();
  }

  public Dimension getSize() {
    return this.bounds.getSize();
  }

  public void setSize(Dimension size) {
    this.bounds.setSize(size);
    capBounds();
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
  }

  public CardState getState() {
    return new CardState(getLocationInGrid(), false);
  }

  public void setState(CardState state) {
    if (state.getLocation() != null)
      setLocationInGrid(state.getLocation());
  }

  public FamilyMember getMember() {
    return this.member.clone();
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
}
