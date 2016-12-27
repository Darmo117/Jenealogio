package net.darmo_creations.gui.components.drag;

import java.awt.Point;
import java.awt.Rectangle;

public interface Dragable {
  void setLocation(Point p);

  Rectangle getBounds();

  void setSelected(boolean selected);

  boolean isSelected();
}
