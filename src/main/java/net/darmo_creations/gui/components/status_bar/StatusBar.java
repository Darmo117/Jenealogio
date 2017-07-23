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
package net.darmo_creations.gui.components.status_bar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * http://java-articles.info/articles/?p=65
 * 
 * @author Gilbert Le Blanc
 * @author Darmo
 */
public class StatusBar extends JPanel {
  private static final long serialVersionUID = 3113718826183202598L;

  private JPanel leftPanel;
  private JPanel rightPanel;
  private JComponent leftComponent;
  private List<JComponent> rightComponents;

  public StatusBar() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(0, 25));

    this.rightComponents = new ArrayList<>();

    this.leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 3));
    this.leftPanel.setOpaque(false);
    add(this.leftPanel, BorderLayout.CENTER);

    this.rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 3));
    this.rightPanel.setOpaque(false);
    add(this.rightPanel, BorderLayout.EAST);
  }

  public void setLeftComponent(JComponent component) {
    this.leftComponent = component;
    this.leftPanel.add(this.leftComponent);
  }

  public JComponent getLeftJComponent() {
    return this.leftComponent;
  }

  public void addRightComponent(JComponent component) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 3));

    panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
    panel.add(component);
    this.rightComponents.add(component);
    this.rightPanel.add(panel);
  }

  public List<JComponent> getRightComponents() {
    return this.rightComponents;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int y = 0;
    g.setColor(new Color(156, 154, 140));
    g.drawLine(0, y, getWidth(), y);
    y++;

    g.setColor(new Color(196, 194, 183));
    g.drawLine(0, y, getWidth(), y);
    y++;

    g.setColor(new Color(218, 215, 201));
    g.drawLine(0, y, getWidth(), y);
    y++;

    g.setColor(new Color(233, 231, 217));
    g.drawLine(0, y, getWidth(), y);

    y = getHeight() - 3;

    g.setColor(new Color(233, 232, 218));
    g.drawLine(0, y, getWidth(), y);
    y++;

    g.setColor(new Color(233, 231, 216));
    g.drawLine(0, y, getWidth(), y);
    y++;

    g.setColor(new Color(221, 221, 220));
    g.drawLine(0, y, getWidth(), y);
  }
}