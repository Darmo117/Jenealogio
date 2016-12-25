package net.darmo_creations.util;

import javax.swing.ImageIcon;

public final class ImageUtil {
  public static final ImageIcon ARROW_UP;
  public static final ImageIcon ARROW_DOWN;

  static {
    ARROW_UP = new ImageIcon("res/icons/arrow_up.png");
    ARROW_DOWN = new ImageIcon("res/icons/arrow_down.png");
  }

  private ImageUtil() {}
}
