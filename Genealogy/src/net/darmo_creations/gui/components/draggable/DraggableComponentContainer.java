package net.darmo_creations.gui.components.draggable;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A component implementing this interface can hold draggable components.
 * 
 * @author Damien Vergnet
 *
 * @param <T> type of the inner components
 */
public interface DraggableComponentContainer<T extends Draggable> {
  /**
   * @return the bounds of this container
   */
  Rectangle getBounds();

  /**
   * @return this component's onscreen offset
   */
  Point getScrollOffset();
}
