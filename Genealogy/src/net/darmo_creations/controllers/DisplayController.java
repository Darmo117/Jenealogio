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
package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.darmo_creations.gui.components.DisplayPanel;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
public class DisplayController extends MouseAdapter implements ActionListener {
  private static final Pattern SELECT_PATTERN = Pattern.compile("^select:(\\d+)$");

  private DisplayPanel panel;
  private Point mouseLocation;

  public DisplayController(DisplayPanel panel) {
    this.panel = panel;
    this.mouseLocation = new Point();
  }

  /**
   * @return the last location of the mouse
   */
  public Point getMouseLocation() {
    return (Point) this.mouseLocation.clone();
  }

  /**
   * Called when a card panel is selected. Tells the DisplayPanel what card was selected.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    Matcher m = SELECT_PATTERN.matcher(cmd);

    if (m.matches()) {
      this.panel.selectPanel(Integer.parseInt(m.group(1)));
    }
  }

  /**
   * Called the mouse is clicked inside the DisplayPanel. Deselects all cards and checks if a link
   * was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    this.panel.selectPanel(-1);
    this.panel.selectLinkIfHovered();
    if (e.getClickCount() == 2)
      this.panel.editLinkIfHovered();
  }

  /**
   * Repaints the DisplayPanel everytime the mouse moves.
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    this.mouseLocation = e.getPoint();
    this.panel.repaint();
  }
}
