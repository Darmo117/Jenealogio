package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.darmo_creations.gui.components.draggable.Draggable;
import net.darmo_creations.gui.components.draggable.DraggableComponentContainer;

/**
 * This controller handles dragging events.
 * 
 * @author Damien Vergnet
 *
 * @param <T> a draggable component type
 */
public class DragController<T extends Draggable> extends MouseAdapter {
  /** Grid size in pixels */
  public static final int GRID_STEP = 10;

  private DraggableComponentContainer<T> handler;
  private T dragable;
  /** The point where the mouse grabbed in the panel. */
  private Point grabPoint;

  /**
   * Creates a controller with the given container and component.
   * 
   * @param handler the container
   * @param dragable the component this controller is monitoring
   */
  public DragController(DraggableComponentContainer<T> handler, T dragable) {
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
    if (this.grabPoint == null)
      mousePressed(e);
    int newX = Math.max(containerBounds.x,
        Math.min(containerBounds.width - bounds.width, e.getXOnScreen() - getXOffset() - this.grabPoint.x));
    int newY = Math.max(containerBounds.y,
        Math.min(containerBounds.height - bounds.height, e.getYOnScreen() - getYOffset() - this.grabPoint.y));

    this.dragable.setLocation(new Point((newX / GRID_STEP) * GRID_STEP, (newY / GRID_STEP) * GRID_STEP));
  }

  /**
   * @return the onscreen x offset
   */
  private int getXOffset() {
    return this.handler.getScrollOffset().x;
  }

  /**
   * @return the onscreen y offset
   */
  private int getYOffset() {
    return this.handler.getScrollOffset().y;
  }
}
