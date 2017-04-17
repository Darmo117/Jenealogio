package net.darmo_creations.gui.components.draggable;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A component can implement this interface to become draggable.
 *
 * @author Damien Vergnet
 */
public interface Draggable {
  /**
   * Sets the location of this component.
   * 
   * @param p the new location
   */
  void setLocation(Point p);

  /**
   * @return the bounds as a rectangle
   */
  Rectangle getBounds();

  /**
   * Performs a click action.
   */
  void doClick();
}
