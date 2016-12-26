package net.darmo_creations.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;

public final class ImageUtil {
  public static final ImageIcon NEW_TREE;
  public static final ImageIcon OPEN;
  public static final ImageIcon SAVE;
  public static final ImageIcon SAVE_AS;
  public static final ImageIcon ADD_CARD;
  public static final ImageIcon EDIT_CARD;
  public static final ImageIcon DELETE_CARD;
  public static final ImageIcon ADD_LINK;
  public static final ImageIcon EDIT_LINK;
  public static final ImageIcon DELETE_LINK;
  public static final ImageIcon EXIT;
  public static final ImageIcon HELP;
  public static final ImageIcon ARROW_UP;
  public static final ImageIcon ARROW_DOWN;

  public static final ImageIcon NEW_TREE_BIG;
  public static final ImageIcon OPEN_BIG;
  public static final ImageIcon SAVE_BIG;
  public static final ImageIcon SAVE_AS_BIG;
  public static final ImageIcon ADD_CARD_BIG;
  public static final ImageIcon EDIT_CARD_BIG;
  public static final ImageIcon DELETE_CARD_BIG;
  public static final ImageIcon ADD_LINK_BIG;
  public static final ImageIcon EDIT_LINK_BIG;
  public static final ImageIcon DELETE_LINK_BIG;
  public static final ImageIcon ARROW_UP_BIG;
  public static final ImageIcon ARROW_DOWN_BIG;

  static {
    NEW_TREE = new ImageIcon("res/icons/new_tree.png");
    OPEN = new ImageIcon("res/icons/open.png");
    SAVE = new ImageIcon("res/icons/save.png");
    SAVE_AS = new ImageIcon("res/icons/save_as.png");
    ADD_CARD = new ImageIcon("res/icons/add_card.png");
    EDIT_CARD = new ImageIcon("res/icons/edit_card.png");
    DELETE_CARD = new ImageIcon("res/icons/delete_card.png");
    ADD_LINK = new ImageIcon("res/icons/add_link.png");
    EDIT_LINK = new ImageIcon("res/icons/edit_link.png");
    DELETE_LINK = new ImageIcon("res/icons/delete_link.png");
    EXIT = new ImageIcon("res/icons/exit.png");
    HELP = new ImageIcon("res/icons/help.png");
    ARROW_UP = new ImageIcon("res/icons/arrow_up.png");
    ARROW_DOWN = new ImageIcon("res/icons/arrow_down.png");

    NEW_TREE_BIG = new ImageIcon("res/icons/new_tree_32.png");
    OPEN_BIG = new ImageIcon("res/icons/open_32.png");
    SAVE_BIG = new ImageIcon("res/icons/save_32.png");
    SAVE_AS_BIG = new ImageIcon("res/icons/save_as_32.png");
    ADD_CARD_BIG = new ImageIcon("res/icons/add_card_32.png");
    EDIT_CARD_BIG = new ImageIcon("res/icons/edit_card_32.png");
    DELETE_CARD_BIG = new ImageIcon("res/icons/delete_card_32.png");
    ADD_LINK_BIG = new ImageIcon("res/icons/add_link_32.png");
    EDIT_LINK_BIG = new ImageIcon("res/icons/edit_link_32.png");
    DELETE_LINK_BIG = new ImageIcon("res/icons/delete_link_32.png");
    ARROW_UP_BIG = new ImageIcon("res/icons/arrow_up_32.png");
    ARROW_DOWN_BIG = new ImageIcon("res/icons/arrow_down_32.png");
  }

  public static BufferedImage deepCopy(BufferedImage image) {
    ColorModel colorModel = image.getColorModel();
    boolean alphaPremultiplied = colorModel.isAlphaPremultiplied();
    WritableRaster raster = image.copyData(null);

    return new BufferedImage(colorModel, raster, alphaPremultiplied, null);
  }

  private ImageUtil() {}
}
