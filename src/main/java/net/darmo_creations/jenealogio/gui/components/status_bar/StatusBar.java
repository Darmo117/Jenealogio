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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * This class implements the missing status bar.
 * 
 * @author Gilbert Le Blanc
 * @author Darmo
 * @see http://java-articles.info/articles/?p=65
 */
public class StatusBar extends JPanel {
  private static final long serialVersionUID = 3113718826183202598L;

  private JPanel leftPanel;
  private JPanel rightPanel;
  private List<JComponent> leftComponents, rightComponents;

  /**
   * Creates a status bar.
   */
  public StatusBar() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(0, 30));

    this.leftComponents = new ArrayList<>();
    this.rightComponents = new ArrayList<>();

    this.leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 3));
    this.leftPanel.setOpaque(false);
    add(this.leftPanel, BorderLayout.CENTER);

    this.rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 5, 3));
    this.rightPanel.setOpaque(false);
    add(this.rightPanel, BorderLayout.EAST);
  }

  /**
   * Adds a component to the left.
   * 
   * @param component the component
   */
  public void addLeftComponent(JComponent component) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

    if (!this.leftComponents.isEmpty())
      panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
    panel.add(component);
    this.leftComponents.add(component);
    this.leftPanel.add(panel);
  }

  /**
   * @return the left components
   */
  public List<JComponent> getLeftComponents() {
    return this.leftComponents;
  }

  /**
   * Adds a component to the right.
   * 
   * @param component the component
   */
  public void addRightComponent(JComponent component) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

    panel.add(new SeparatorPanel(Color.GRAY, Color.WHITE));
    panel.add(component);
    this.rightComponents.add(component);
    this.rightPanel.add(panel);
  }

  /**
   * @return the right components
   */
  public List<JComponent> getRightComponents() {
    return this.rightComponents;
  }
}