package net.darmo_creations.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;

import net.darmo_creations.config.Language;

/**
 * This class holds all images for the app and provides a method to copy images.
 * 
 * @author Damien Vergnet
 */
public final class Images {
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
  public static final ImageIcon BABY;
  public static final ImageIcon HEART;
  public static final ImageIcon CROSS;
  public static final ImageIcon COLOR_WHEEL;
  public static final ImageIcon BOOK;
  public static final ImageIcon BOOK_OPEN;
  public static final ImageIcon PAGE;
  public static final ImageIcon MALE_SYMBOL;
  public static final ImageIcon FEMALE_SYMBOL;
  public static final ImageIcon USER;
  public static final ImageIcon LINK;
  public static final ImageIcon SYNC;
  public static final ImageIcon COLLAPSE;
  public static final ImageIcon HOME;

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
    BABY = new ImageIcon("res/icons/baby.png");
    HEART = new ImageIcon("res/icons/heart.png");
    CROSS = new ImageIcon("res/icons/cross.png");
    COLOR_WHEEL = new ImageIcon("res/icons/color_wheel.png");
    BOOK = new ImageIcon("res/icons/book.png");
    BOOK_OPEN = new ImageIcon("res/icons/book_open.png");
    PAGE = new ImageIcon("res/icons/page.png");
    MALE_SYMBOL = new ImageIcon("res/icons/male_symbol.png");
    FEMALE_SYMBOL = new ImageIcon("res/icons/female_symbol.png");
    USER = new ImageIcon("res/icons/user.png");
    LINK = new ImageIcon("res/icons/link.png");
    SYNC = new ImageIcon("res/icons/sync.png");
    COLLAPSE = new ImageIcon("res/icons/collapse.png");
    HOME = new ImageIcon("res/icons/home.png");

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

  /**
   * Returns the flag icon for the given language.
   * 
   * @param langCode the language
   * @return the icon or null if none were found
   */
  public static ImageIcon getCountryFlag(Language langCode) {
    switch (langCode) {
      case ENGLISH:
        return new ImageIcon("res/icons/flag_great_britain.png");
      case FRENCH:
        return new ImageIcon("res/icons/flag_france.png");
      case ESPERANTO:
        return new ImageIcon("res/icons/flag_esperanto.png");
    }
    return null;
  }

  /**
   * Makes a deep copy of the given image <i>without its properties</i>.
   * 
   * @param image the image to copy.
   * @return the copy image
   */
  public static BufferedImage deepCopy(BufferedImage image) {
    ColorModel colorModel = image.getColorModel();
    boolean alphaPremultiplied = colorModel.isAlphaPremultiplied();
    WritableRaster raster = image.copyData(null);

    return new BufferedImage(colorModel, raster, alphaPremultiplied, null);
  }

  private Images() {}
}
