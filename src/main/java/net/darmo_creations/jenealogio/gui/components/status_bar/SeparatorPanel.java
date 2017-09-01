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
package net.darmo_creations.jenealogio.gui.components.status_bar;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * http://java-articles.info/articles/?p=65
 * 
 * @author Gilbert Le Blanc
 */
class SeparatorPanel extends JPanel {
  private static final long serialVersionUID = 8521990843928995207L;

  private final Color leftColor;
  private final Color rightColor;

  SeparatorPanel(Color leftColor, Color rightColor) {
    this.leftColor = leftColor;
    this.rightColor = rightColor;
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(this.leftColor);
    g.drawLine(0, 0, 0, getHeight());
    g.setColor(this.rightColor);
    g.drawLine(1, 0, 1, getHeight());
  }
}