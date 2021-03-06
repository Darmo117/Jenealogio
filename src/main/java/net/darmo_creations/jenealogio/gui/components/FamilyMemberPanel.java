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
package net.darmo_creations.jenealogio.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
public class FamilyMemberPanel extends JPanel {
  private static final long serialVersionUID = 8199650844222484357L;

  private PanelModel model;

  private Border selectedBorder, backgroundBorder, unselectedBorder;
  private JLabel nameLbl;

  /**
   * Creates a panel for the given person.
   * 
   * @param member the member to display
   */
  public FamilyMemberPanel(FamilyMember member, WritableConfig config) {
    super(new BorderLayout());
    this.model = new PanelModel(member.getId());
    this.nameLbl = new JLabel();
    this.nameLbl.setHorizontalAlignment(JLabel.CENTER);
    add(this.nameLbl);
    setInfo(member, config);
    setSelected(false);
  }

  /**
   * Sets the data to display.
   * 
   * @param member the member to display
   */
  public void setInfo(FamilyMember member, WritableConfig config) {
    this.selectedBorder = new LineBorder(config.getValue(ConfigTags.CARD_SELECTED_BORDER_COLOR), 2);
    this.backgroundBorder = new LineBorder(config.getValue(ConfigTags.CARD_SELECTED_BACKGROUND_BORDER_COLOR), 2);
    this.unselectedBorder = new LineBorder(config.getValue(ConfigTags.CARD_BORDER_COLOR), 2);

    switch (member.getGender()) {
      case UNKNOW:
        setBackground(config.getValue(ConfigTags.GENDER_UNKNOWN_COLOR));
        break;
      case MAN:
        setBackground(config.getValue(ConfigTags.GENDER_MALE_COLOR));
        break;
      case WOMAN:
        setBackground(config.getValue(ConfigTags.GENDER_FEMALE_COLOR));
        break;
    }

    this.model.setId(member.getId());
    this.nameLbl.setIcon(member.isDead() ? Images.TOMBSTONE : null);
    this.nameLbl.setText(member.toString());

    Dimension size = this.nameLbl.getPreferredSize();
    size.width += 20;
    size.height = 30;
    setSize(size);
    revalidate();
  }

  public long getMemberId() {
    return this.model.getId();
  }

  @Override
  public void setLocation(Point p) {
    super.setLocation(p);
    getParent().repaint();
  }

  @Override
  public void setLocation(int x, int y) {
    super.setLocation(x, y);
    getParent().repaint();
  }

  /**
   * @return true if this panel is selected; false otherwise
   */
  public boolean isSelected() {
    return this.model.isSelected();
  }

  /**
   * Sets the selection.
   * 
   * @param selected
   */
  public void setSelected(boolean selected) {
    this.model.setSelected(selected);
    setBorder(selected ? this.selectedBorder : this.unselectedBorder);
  }

  /**
   * @return true if this panel is selected in the background; false otherwise
   */
  public boolean isSelectedBackground() {
    return this.model.isSelectedBackground();
  }

  /**
   * Sets the background selection.
   * 
   * @param selected
   */
  public void setSelectedBackground(boolean selected) {
    this.model.setSelectedBackground(selected);
    setBorder(selected ? this.backgroundBorder : this.unselectedBorder);
  }

  /**
   * This class is the model for the panel. It holds data about selection and the member's ID.
   *
   * @author Damien Vergnet
   */
  private class PanelModel {
    private boolean selected, background;
    private long id;

    public PanelModel(long id) {
      this.selected = false;
      this.background = false;
      this.id = id;
    }

    public boolean isSelected() {
      return this.selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
      this.background = false;
    }

    public boolean isSelectedBackground() {
      return this.background;
    }

    public void setSelectedBackground(boolean background) {
      this.background = background;
      this.selected = false;
    }

    public long getId() {
      return this.id;
    }

    public void setId(long id) {
      this.id = id;
    }
  }
}
