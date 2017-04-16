package net.darmo_creations.gui.components.draggable;

import java.awt.Point;
import java.awt.Rectangle;

public interface Draggable {
  void setLocation(Point p);

  Rectangle getBounds();

  void doClick();
}
