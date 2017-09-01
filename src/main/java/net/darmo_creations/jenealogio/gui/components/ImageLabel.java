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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * An image label can display images and resizes them to fit inside its bounds.
 *
 * @author Damien Vergnet
 */
public class ImageLabel extends JLabel {
  private static final long serialVersionUID = 3710328037282006238L;

  /**
   * Creates a label for the given image.
   * 
   * @param image the image to display
   */
  public ImageLabel(ImageIcon image) {
    super(image);
  }

  @Override
  protected void paintComponent(Graphics g) {
    ImageIcon icon = (ImageIcon) getIcon();

    if (icon != null) {
      Graphics2D g2 = (Graphics2D) g;
      Dimension size = getSize();

      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(icon.getImage(), 0, 0, size.width, size.height, null);
      g2.dispose();
    }

    super.paintComponent(g);
  }
}
