package net.darmo_creations.gui.components.draggable;

import java.awt.Point;
import java.awt.Rectangle;

public interface DraggableComponentContainer<T extends Draggable> {
  Rectangle getBounds();

  Point getScrollOffset();
}
