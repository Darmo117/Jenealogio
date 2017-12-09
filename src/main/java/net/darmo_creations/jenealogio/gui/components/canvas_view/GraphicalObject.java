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

import java.awt.Graphics;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.config.WritableConfig;

/**
 * This inteface represents objects that can be drawn and selected.
 *
 * @author Damien Vergnet
 */
abstract class GraphicalObject {
  private static final int NOT_SELECTED = 0;
  private static final int SELECTED = 1;
  private static final int SELECTED_BG = 2;

  private final JComponent parent;
  private final long id;
  private int selectionMode;
  private boolean hovered;

  /**
   * Creates an object.
   * 
   * @param parent the parent of this component
   * @param id object's ID
   */
  public GraphicalObject(JComponent parent, long id) {
    this.parent = parent;
    this.id = id;
    this.selectionMode = NOT_SELECTED;
    this.hovered = false;
  }

  /**
   * This object's unique ID.
   */
  public long getId() {
    return this.id;
  }

  /**
   * This component's parent.
   */
  public JComponent getParent() {
    return this.parent;
  }

  /**
   * Deselects this object.
   */
  public void deselect() {
    this.selectionMode = NOT_SELECTED;
  }

  /**
   * @return true if this object is selected; false otherwise
   */
  public boolean isSelected() {
    return this.selectionMode == SELECTED;
  }

  /**
   * Sets the selection for this object.
   * 
   * @param selected
   */
  public void setSelected() {
    this.selectionMode = SELECTED;
  }

  /**
   * @return true if this object is selected in the background; false otherwise
   */
  public boolean isSelectedBackground() {
    return this.selectionMode == SELECTED_BG;
  }

  /**
   * Sets the background selection for this object.
   * 
   * @param selected
   */
  public void setSelectedBackground() {
    this.selectionMode = SELECTED_BG;
  }

  public boolean isHovered() {
    return this.hovered;
  }

  public void setHovered(boolean hovered) {
    this.hovered = hovered;
  }

  /**
   * Paints this object.
   * 
   * @param g the graphical context
   * @param config the config
   */
  public abstract void paintComponent(Graphics g, WritableConfig config);

  @Override
  public boolean equals(Object o) {
    if (o instanceof GraphicalObject)
      return this.id == ((GraphicalObject) o).getId();
    return false;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(this.id);
  }
}
