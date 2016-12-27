package net.darmo_creations.gui.components.drag;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragController<T extends Dragable> extends MouseAdapter {
  private DragableComponentContainer<T> handler;
  private T dragable;

  public DragController(DragableComponentContainer<T> handler, T dragable) {
    this.handler = handler;
    this.dragable = dragable;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.dragable.setSelected(true);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Rectangle bounds = this.dragable.getBounds();
    Rectangle r = this.handler.getBounds();
    int deltaX = e.getXOnScreen() + getXOffset() - bounds.x;
    int deltaY = e.getYOnScreen() + getYOffset() - bounds.y;
    int newX = Math.max(r.x, Math.min(r.width - bounds.width, bounds.x + deltaX));
    int newY = Math.max(r.y, Math.min(r.height - bounds.height, bounds.y + deltaY));

    this.dragable.setLocation(new Point(newX, newY));
  }

  private int getXOffset() {
    return this.handler.getScrollOffset().x;
  }

  private int getYOffset() {
    return this.handler.getScrollOffset().y;
  }
}
