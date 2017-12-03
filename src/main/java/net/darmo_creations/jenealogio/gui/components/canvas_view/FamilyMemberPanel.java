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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

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
  private ComponentResizer resizer;

  private Rectangle bounds;
  private JLabel nameLbl;
  private FamilyMember member;

  /**
   * Creates a panel for the given person.
   * 
   * @param parent the parent of this component
   * @param member the member to display
   */
  public FamilyMemberPanel(JComponent parent, FamilyMember member) {
    super(parent, member.getId());
    this.nameLbl = new JLabel();
    this.resizer = new ComponentResizer(this);
    this.bounds = new Rectangle();
    setInfo(member);
  }

  /**
   * Tells if the mouse is on the border.
   */
  public boolean isMouseOnBorder() {
    return this.resizer.isMouseOnBorder();
  }

  /**
   * Sets the data to display.
   * 
   * @param member the member to display
   * @param config the config
   */
  public void setInfo(FamilyMember member) {
    this.member = member;
    this.nameLbl.setIcon(member.isDead() ? Images.TOMBSTONE : null);
    this.nameLbl.setText("<html><center>" + member.toString() + "</center></html>");
    this.nameLbl.setBorder(new EmptyBorder(0, 4, 0, 4));

    Dimension size = this.nameLbl.getPreferredSize();
    size.width += 20;
    size.height = 30;
    setSize(size);
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
    g2d.setStroke(new BasicStroke(2));
    if (isSelected())
      g2d.setColor(config.getValue(ConfigTags.CARD_SELECTED_BORDER_COLOR));
    else if (isSelectedBackground())
      g2d.setColor(config.getValue(ConfigTags.CARD_SELECTED_BACKGROUND_BORDER_COLOR));
    else
      g2d.setColor(config.getValue(ConfigTags.CARD_BORDER_COLOR));
    g2d.drawRect(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
    this.nameLbl.paintAll(g);
  }

  public Point getLocation() {
    return this.bounds.getLocation();
  }

  public void setLocation(Point p) {
    this.bounds.setLocation(p);
  }

  public Dimension getSize() {
    return this.bounds.getSize();
  }

  public void setSize(Dimension size) {
    this.bounds.setSize(size);
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
  }
}
