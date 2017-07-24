/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
  public static final ImageIcon EDIT_TREE;
  public static final ImageIcon SAVE;
  public static final ImageIcon SAVE_AS;
  public static final ImageIcon UNDO;
  public static final ImageIcon REDO;
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
  public static final ImageIcon HEART_BROKEN;
  public static final ImageIcon TOMBSTONE;
  public static final ImageIcon COLOR_WHEEL;
  public static final ImageIcon MALE_SYMBOL;
  public static final ImageIcon FEMALE_SYMBOL;
  public static final ImageIcon USER;
  public static final ImageIcon LINK;

  public static final ImageIcon NEW_TREE_BIG;
  public static final ImageIcon OPEN_BIG;
  public static final ImageIcon SAVE_BIG;
  public static final ImageIcon SAVE_AS_BIG;
  public static final ImageIcon UNDO_BIG;
  public static final ImageIcon REDO_BIG;
  public static final ImageIcon ADD_CARD_BIG;
  public static final ImageIcon EDIT_CARD_BIG;
  public static final ImageIcon DELETE_CARD_BIG;
  public static final ImageIcon ADD_LINK_BIG;
  public static final ImageIcon EDIT_LINK_BIG;
  public static final ImageIcon DELETE_LINK_BIG;
  public static final ImageIcon ARROW_UP_BIG;
  public static final ImageIcon ARROW_DOWN_BIG;

  public static final ImageIcon JENEALOGIO;
  public static final ImageIcon GNU_GPL;

  static {
    NEW_TREE = getIcon("/assets/icons/new_tree.png");
    OPEN = getIcon("/assets/icons/open.png");
    EDIT_TREE = getIcon("/assets/icons/edit_tree.png");
    SAVE = getIcon("/assets/icons/save.png");
    SAVE_AS = getIcon("/assets/icons/save_as.png");
    UNDO = getIcon("/assets/icons/undo.png");
    REDO = getIcon("/assets/icons/redo.png");
    ADD_CARD = getIcon("/assets/icons/add_card.png");
    EDIT_CARD = getIcon("/assets/icons/edit_card.png");
    DELETE_CARD = getIcon("/assets/icons/delete_card.png");
    ADD_LINK = getIcon("/assets/icons/add_link.png");
    EDIT_LINK = getIcon("/assets/icons/edit_link.png");
    DELETE_LINK = getIcon("/assets/icons/delete_link.png");
    EXIT = getIcon("/assets/icons/exit.png");
    HELP = getIcon("/assets/icons/help.png");
    ARROW_UP = getIcon("/assets/icons/arrow_up.png");
    ARROW_DOWN = getIcon("/assets/icons/arrow_down.png");
    BABY = getIcon("/assets/icons/baby.png");
    HEART = getIcon("/assets/icons/heart.png");
    HEART_BROKEN = getIcon("/assets/icons/heart_broken.png");
    TOMBSTONE = getIcon("/assets/icons/tombstone.png");
    COLOR_WHEEL = getIcon("/assets/icons/color_wheel.png");
    MALE_SYMBOL = getIcon("/assets/icons/male_symbol.png");
    FEMALE_SYMBOL = getIcon("/assets/icons/female_symbol.png");
    USER = getIcon("/assets/icons/user.png");
    LINK = getIcon("/assets/icons/link.png");

    NEW_TREE_BIG = getIcon("/assets/icons/new_tree_32.png");
    OPEN_BIG = getIcon("/assets/icons/open_32.png");
    SAVE_BIG = getIcon("/assets/icons/save_32.png");
    SAVE_AS_BIG = getIcon("/assets/icons/save_as_32.png");
    UNDO_BIG = getIcon("/assets/icons/undo_32.png");
    REDO_BIG = getIcon("/assets/icons/redo_32.png");
    ADD_CARD_BIG = getIcon("/assets/icons/add_card_32.png");
    EDIT_CARD_BIG = getIcon("/assets/icons/edit_card_32.png");
    DELETE_CARD_BIG = getIcon("/assets/icons/delete_card_32.png");
    ADD_LINK_BIG = getIcon("/assets/icons/add_link_32.png");
    EDIT_LINK_BIG = getIcon("/assets/icons/edit_link_32.png");
    DELETE_LINK_BIG = getIcon("/assets/icons/delete_link_32.png");
    ARROW_UP_BIG = getIcon("/assets/icons/arrow_up_32.png");
    ARROW_DOWN_BIG = getIcon("/assets/icons/arrow_down_32.png");

    JENEALOGIO = getIcon("/assets/icons/jenealogio_icon.png");
    GNU_GPL = getIcon("/assets/icons/gplv3-127x51.png");
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
        return getIcon("/assets/icons/flag_great_britain.png");
      case FRENCH:
        return getIcon("/assets/icons/flag_france.png");
      case ESPERANTO:
        return getIcon("/assets/icons/flag_esperanto.png");
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

  private static ImageIcon getIcon(String path) {
    return new ImageIcon(Images.class.getResource(path));
  }

  private Images() {}
}
