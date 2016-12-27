package net.darmo_creations.gui.components.drag;

import java.awt.Point;
import java.awt.Rectangle;

public interface DragableComponentContainer<T extends Dragable> {
  Rectangle getBounds();

  Point getScrollOffset();
}
