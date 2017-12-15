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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TooManyListenersException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.components.view.View;
import net.darmo_creations.jenealogio.model.CardState;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropListener;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropTarget;
import net.darmo_creations.utils.swing.drag_and_drop.DropTargetHandler;

/**
 * This view displays the family tree.
 *
 * @author Damien Vergnet
 */
public class CanvasView extends View implements DragAndDropTarget {
  private static final long serialVersionUID = 8747904983365363275L;

  private static final Dimension DEFAULT_SIZE = new Dimension(4000, 4000);

  private WritableConfig config;
  private DropTarget dropTarget;

  private Canvas canvas;
  private boolean resizing;

  public CanvasView() {
    super(I18n.getLocalizedString("label.canvas.text"), new CanvasViewController());

    this.canvas = new Canvas();
    this.canvas.setLayout(null);
    this.canvas.setPreferredSize(DEFAULT_SIZE);
    this.canvas.addMouseListener(this.controller);
    this.canvas.addMouseMotionListener(this.controller);
    this.canvas.addFocusListener(this.controller);

    ComponentDragController dragController = new ComponentDragController(this);
    this.canvas.addMouseListener(dragController);
    this.canvas.addMouseMotionListener(dragController);

    setViewport(this.canvas);

    for (Component c : getComponents()) {
      if (c instanceof JScrollPane) {
        JScrollPane s = (JScrollPane) c;
        s.getHorizontalScrollBar().setUnitIncrement(16);
        s.getVerticalScrollBar().setUnitIncrement(16);
      }
    }

    ApplicationRegistry.EVENTS_BUS.register(this.controller);
    this.dropTarget = new DropTarget(this.canvas, DnDConstants.ACTION_COPY_OR_MOVE, null);
  }

  /**
   * Adds a drag-and-drop listener.
   * 
   * @param handler the new handler
   */
  @Override
  public void addDragAndDropListener(DragAndDropListener l) {
    try {
      this.dropTarget.addDropTargetListener(new DropTargetHandler(l, this));
    }
    catch (TooManyListenersException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Resets the panel. All internal components are destroyed.
   */
  public void reset() {
    getController().reset();
    this.canvas.removeAll();
    this.canvas.setPreferredSize(DEFAULT_SIZE);
    revalidate();
    repaint();
  }

  /**
   * Refreshes the display from the given model.
   * 
   * @param family the model
   */
  public void refresh(Family family, WritableConfig config) {
    refresh(family, Collections.emptyMap(), config);
  }

  /**
   * Refreshes the display from the given model and updates positions of panels specified in the
   * map.
   * 
   * @param family the model
   * @param cardsStates states for all cards
   * @param config the config
   */
  public void refresh(Family family, Map<Long, CardState> cardsStates, WritableConfig config) {
    this.config = config;
    getController().refresh(family, cardsStates);
    revalidate();
    repaint();
  }

  @Override
  public void deselectAll() {
    getController().deselectAll();
  }

  @Override
  public Selection getSelection() {
    return getController().getSelection();
  }

  /**
   * @return the states of all panels
   */
  public Map<Long, CardState> getCardsStates() {
    return getController().getCardsStates();
  }

  /**
   * Exports this panel to an image.
   * 
   * @return this panel as an image
   */
  public BufferedImage exportToImage() {
    BufferedImage image = new BufferedImage(this.canvas.getWidth(), this.canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
    this.canvas.paint(image.createGraphics());

    Point p1 = getTopLeftPoint();
    Point p2 = getBottomRightPoint();

    return image.getSubimage(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
  }

  /**
   * Called when a card is dragged.
   */
  void cardDragged(long id, Point translation, Point mouseLocation) {
    getController().cardDragged(translation, mouseLocation);
  }

  boolean isResizing() {
    return this.resizing;
  }

  void resizing(boolean resizing) {
    this.resizing = resizing;
  }

  Rectangle getCanvasVisibleRect() {
    return this.canvas.getVisibleRect();
  }

  Rectangle getCanvasBounds() {
    return this.canvas.getBounds();
  }

  void setCanvasPreferredSize(Dimension size) {
    this.canvas.setPreferredSize(size);
    this.canvas.revalidate();
  }

  Point getCanvasOffset() {
    return this.canvas.getLocationOnScreen();
  }

  Optional<FamilyMemberPanel> getHoveredPanel(Point mouseLocation) {
    return getController().getHoveredPanel(mouseLocation);
  }

  @Override
  public void scrollRectToVisible(Rectangle aRect) {
    this.canvas.scrollRectToVisible(aRect);
  }

  /**
   * @return the topmost leftmost point of the tree
   */
  private Point getTopLeftPoint() {
    Point point = new Point(this.canvas.getBounds().width, this.canvas.getBounds().height);

    getController().getPanels().forEach(p -> {
      Point l = p.getLocation();
      point.x = Math.min(point.x, l.x);
      point.y = Math.min(point.y, l.y);
    });

    return point;
  }

  /**
   * @return the bottommost rightmost point of the tree
   */
  private Point getBottomRightPoint() {
    Point point = new Point();

    getController().getPanels().forEach(p -> {
      Rectangle r = p.getBounds();
      point.x = Math.max(point.x, r.x + r.width);
      point.y = Math.max(point.y, r.y + r.height);
    });

    return point;
  }

  private CanvasViewController getController() {
    return (CanvasViewController) this.controller;
  }

  private class Canvas extends JPanel {
    private static final long serialVersionUID = -7308185736594294332L;

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      if (CanvasView.this.config != null) {
        Optional<Rectangle> selection = CanvasView.this.getController().getSelectionRectangle();

        if (selection.isPresent()) {
          Rectangle r = selection.get();

          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.ZONE_SELECTION_BACKGROUND_COLOR));
          g2d.fillRect(r.x, r.y, r.width, r.height);
          g2d.setColor(CanvasView.this.config.getValue(ConfigTags.ZONE_SELECTION_BORDER_COLOR));
          g2d.drawRect(r.x, r.y, r.width, r.height);
        }

        getController().getLinks().forEach(l -> l.paintComponent(g2d, CanvasView.this.config));
        getController().getPanels().forEach(p -> p.paintComponent(g2d, CanvasView.this.config));
      }
    }
  }
}
