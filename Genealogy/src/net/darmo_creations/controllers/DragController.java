package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.darmo_creations.gui.components.drag.Dragable;
import net.darmo_creations.gui.components.drag.DragableComponentContainer;

public class DragController<T extends Dragable> extends MouseAdapter {
  public static final int GRID_STEP = 10;

  private DragableComponentContainer<T> handler;
  private T dragable;
  private Point grabPoint;

  public DragController(DragableComponentContainer<T> handler, T dragable) {
    this.handler = handler;
    this.dragable = dragable;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.dragable.doClick();
    this.grabPoint = new Point(e.getX(), e.getY());
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Rectangle bounds = this.dragable.getBounds();
    Rectangle containerBounds = this.handler.getBounds();
    int newX = Math.max(containerBounds.x, Math.min(containerBounds.width - bounds.width, e.getXOnScreen() - getXOffset() - this.grabPoint.x));
    int newY = Math.max(containerBounds.y, Math.min(containerBounds.height - bounds.height, e.getYOnScreen() - getYOffset() - this.grabPoint.y));

    this.dragable.setLocation(new Point((newX / GRID_STEP) * GRID_STEP, (newY / GRID_STEP) * GRID_STEP));
  }

  private int getXOffset() {
    return this.handler.getScrollOffset().x;
  }

  private int getYOffset() {
    return this.handler.getScrollOffset().y;
  }
}
