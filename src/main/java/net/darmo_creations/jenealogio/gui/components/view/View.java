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
package net.darmo_creations.jenealogio.gui.components.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import net.darmo_creations.jenealogio.util.Selection;

public abstract class View extends JPanel {
  private static final long serialVersionUID = -3403502478246185990L;

  private static final Color FOCUSED = new Color(180, 180, 180);
  private static final Color NOT_FOCUSED = new Color(220, 220, 220);

  private JPanel topPanel, buttonsPnl;
  private JScrollPane scrollPane;
  protected ViewController controller;

  /**
   * Initializes the view.
   */
  public View(String name, ViewController controller) {
    super(new BorderLayout());

    this.controller = controller;
    this.controller.setView(this);
    addFocusListener(this.controller);

    this.scrollPane = new JScrollPane();
    this.scrollPane.addFocusListener(this.controller);

    JLabel topLbl = new JLabel(name);
    topLbl.addMouseListener(this.controller);

    this.buttonsPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
    this.buttonsPnl.setOpaque(false);

    this.topPanel = new JPanel(new BorderLayout());
    this.topPanel.setBackground(NOT_FOCUSED);
    this.topPanel.setBorder(new CompoundBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
    this.topPanel.add(topLbl, BorderLayout.WEST);
    this.topPanel.add(this.buttonsPnl, BorderLayout.EAST);

    add(this.topPanel, BorderLayout.NORTH);
    add(this.scrollPane, BorderLayout.CENTER);
  }

  public void setTopFocused(boolean isFocused) {
    this.topPanel.setBackground(isFocused ? FOCUSED : NOT_FOCUSED);
  }

  public void addButton(AbstractButton b) {
    this.buttonsPnl.add(b);
  }

  /**
   * @return the coordinates of the view's middle point
   */
  public Point getDisplayMiddlePoint() {
    JViewport viewport = this.scrollPane.getViewport();
    Point pos = viewport.getViewPosition();
    Dimension size = viewport.getExtentSize();
    int x = pos.x + size.width / 2;
    int y = pos.y + size.height / 2;

    return new Point(x, y);
  }

  /**
   * Sets the viewport.
   * 
   * @param viewport the viewport
   */
  public void setViewport(JComponent viewport) {
    this.scrollPane.setViewportView(viewport);
  }

  public Rectangle getViewportVisibleRect() {
    return ((JComponent) this.scrollPane.getViewport().getView()).getVisibleRect();
  }

  /**
   * Returns the current selection.
   */
  public abstract Selection getSelection();

  /**
   * Deselects all components.
   */
  public abstract void deselectAll();

  /**
   * @return the vertical scrollbar's value
   */
  public int getVerticalScroll() {
    return this.scrollPane.getVerticalScrollBar().getValue();
  }

  /**
   * Sets the vertical scrollbar's value
   * 
   * @param value the new value
   */
  public void setVerticalScroll(int value) {
    this.scrollPane.getVerticalScrollBar().setValue(value);
  }

  /**
   * @return the horizontal scrollbar's value
   */
  public int getHorizontalScroll() {
    return this.scrollPane.getHorizontalScrollBar().getValue();
  }

  /**
   * Sets the horizontal scrollbar's value
   * 
   * @param value the new value
   */
  public void setHorizontalScroll(int value) {
    this.scrollPane.getHorizontalScrollBar().setValue(value);
  }
}
