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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * A color button is a JButton which display a color. This color can be changed and recovered.
 *
 * @author Damien Vergnet
 */
public class ColorButton extends JButton {
  private static final long serialVersionUID = 3060407471739094850L;

  /**
   * Creates a button displaying initialized to black.
   */
  public ColorButton() {
    this(Color.BLACK);
  }

  /**
   * Creates a button displaying initialized to the given color.
   */
  public ColorButton(Color color) {
    setPreferredSize(new Dimension(49, 23));
    setColor(color);
  }

  /**
   * @return the current color
   */
  public Color getColor() {
    return ((ColorIcon) getIcon()).getColor();
  }

  /**
   * Sets the color. Cannot be null.
   * 
   * @param color the new color
   */
  public void setColor(Color color) {
    Dimension size = getPreferredSize();
    int w = size.width - 12;
    int h = size.height - 12;
    setIcon(new ColorIcon(new Dimension(w, h), color));
  }

  /**
   * This class displays the color rectangle icon on the button.
   *
   * @author Damien Vergnet
   */
  private class ColorIcon implements Icon {
    private Dimension size;
    private Color color;

    /**
     * Creates an icon.
     * 
     * @param size the size
     * @param color the color
     */
    public ColorIcon(Dimension size, Color color) {
      this.size = size;
      this.color = Objects.requireNonNull(color);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(this.color);
      g2d.fillRect(x, y, this.size.width - 1, this.size.height - 1);
      g2d.setColor(ColorButton.this.isEnabled() ? Color.BLACK : Color.GRAY);
      g2d.drawRect(x, y, this.size.width - 1, this.size.height - 1);
    }

    /**
     * @return the current color
     */
    public Color getColor() {
      return this.color;
    }

    @Override
    public int getIconWidth() {
      return this.size.width;
    }

    @Override
    public int getIconHeight() {
      return this.size.height;
    }
  }
}
