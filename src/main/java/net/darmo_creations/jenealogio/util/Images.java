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
package net.darmo_creations.jenealogio.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.darmo_creations.gui_framework.util.ImagesUtil;
import net.darmo_creations.utils.FilesUtil;

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
  public static final ImageIcon EXPORT_IMAGE;
  public static final ImageIcon UNDO;
  public static final ImageIcon REDO;
  public static final ImageIcon ADD_CARD;
  public static final ImageIcon ADD_LINK;
  public static final ImageIcon EDIT;
  public static final ImageIcon DELETE;
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
  public static final ImageIcon GROUP;
  public static final ImageIcon GROUP_LINK;
  public static final ImageIcon USER;
  public static final ImageIcon LINK;
  public static final ImageIcon LINK_BROKEN;
  public static final ImageIcon CHECKING_UPDATES;
  public static final ImageIcon NEW_UPDATE;
  public static final ImageIcon UPDATE_CHECK_FAILED;

  public static final ImageIcon NEW_TREE_BIG;
  public static final ImageIcon OPEN_BIG;
  public static final ImageIcon SAVE_BIG;
  public static final ImageIcon SAVE_AS_BIG;
  public static final ImageIcon UNDO_BIG;
  public static final ImageIcon REDO_BIG;
  public static final ImageIcon ADD_CARD_BIG;
  public static final ImageIcon ADD_LINK_BIG;
  public static final ImageIcon EDIT_BIG;
  public static final ImageIcon DELETE_BIG;
  public static final ImageIcon ARROW_UP_BIG;
  public static final ImageIcon ARROW_DOWN_BIG;

  public static final ImageIcon JENEALOGIO;
  public static final ImageIcon GNU_GPL;

  static {
    NEW_TREE = ImagesUtil.getIcon("/assets/icons/new_tree.png");
    OPEN = ImagesUtil.getIcon("/assets/icons/open.png");
    EDIT_TREE = ImagesUtil.getIcon("/assets/icons/edit_tree.png");
    SAVE = ImagesUtil.getIcon("/assets/icons/save.png");
    SAVE_AS = ImagesUtil.getIcon("/assets/icons/save_as.png");
    EXPORT_IMAGE = ImagesUtil.getIcon("/assets/icons/export_image.png");
    UNDO = ImagesUtil.getIcon("/assets/icons/undo.png");
    REDO = ImagesUtil.getIcon("/assets/icons/redo.png");
    ADD_CARD = ImagesUtil.getIcon("/assets/icons/add_card.png");
    ADD_LINK = ImagesUtil.getIcon("/assets/icons/add_link.png");
    EDIT = ImagesUtil.getIcon("/assets/icons/edit.png");
    DELETE = ImagesUtil.getIcon("/assets/icons/delete.png");
    EXIT = ImagesUtil.getIcon("/assets/icons/exit.png");
    HELP = ImagesUtil.getIcon("/assets/icons/help.png");
    ARROW_UP = ImagesUtil.getIcon("/assets/icons/arrow_up.png");
    ARROW_DOWN = ImagesUtil.getIcon("/assets/icons/arrow_down.png");
    BABY = ImagesUtil.getIcon("/assets/icons/baby.png");
    HEART = ImagesUtil.getIcon("/assets/icons/heart.png");
    HEART_BROKEN = ImagesUtil.getIcon("/assets/icons/heart_broken.png");
    TOMBSTONE = ImagesUtil.getIcon("/assets/icons/tombstone.png");
    COLOR_WHEEL = ImagesUtil.getIcon("/assets/icons/color_wheel.png");
    MALE_SYMBOL = ImagesUtil.getIcon("/assets/icons/male_symbol.png");
    FEMALE_SYMBOL = ImagesUtil.getIcon("/assets/icons/female_symbol.png");
    GROUP = ImagesUtil.getIcon("/assets/icons/group.png");
    GROUP_LINK = ImagesUtil.getIcon("/assets/icons/group_link.png");
    USER = ImagesUtil.getIcon("/assets/icons/user.png");
    LINK = ImagesUtil.getIcon("/assets/icons/link.png");
    LINK_BROKEN = ImagesUtil.getIcon("/assets/icons/link_break.png");
    CHECKING_UPDATES = ImagesUtil.getIcon("/assets/icons/checking_updates.gif");
    NEW_UPDATE = ImagesUtil.getIcon("/assets/icons/update_available.png");
    UPDATE_CHECK_FAILED = ImagesUtil.getIcon("/assets/icons/updates_check_failed.png");

    NEW_TREE_BIG = ImagesUtil.getIcon("/assets/icons/new_tree_32.png");
    OPEN_BIG = ImagesUtil.getIcon("/assets/icons/open_32.png");
    SAVE_BIG = ImagesUtil.getIcon("/assets/icons/save_32.png");
    SAVE_AS_BIG = ImagesUtil.getIcon("/assets/icons/save_as_32.png");
    UNDO_BIG = ImagesUtil.getIcon("/assets/icons/undo_32.png");
    REDO_BIG = ImagesUtil.getIcon("/assets/icons/redo_32.png");
    ADD_CARD_BIG = ImagesUtil.getIcon("/assets/icons/add_card_32.png");
    ADD_LINK_BIG = ImagesUtil.getIcon("/assets/icons/add_link_32.png");
    EDIT_BIG = ImagesUtil.getIcon("/assets/icons/edit_32.png");
    DELETE_BIG = ImagesUtil.getIcon("/assets/icons/delete_32.png");
    ARROW_UP_BIG = ImagesUtil.getIcon("/assets/icons/arrow_up_32.png");
    ARROW_DOWN_BIG = ImagesUtil.getIcon("/assets/icons/arrow_down_32.png");

    JENEALOGIO = ImagesUtil.getIcon("/assets/icons/jenealogio_icon.png");
    GNU_GPL = ImagesUtil.getIcon("/assets/icons/gplv3-127x51.png");
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

  /**
   * Computes the hashcode of an image.
   * 
   * @param img the image
   * @return the hashcode
   */
  public static int hashCode(BufferedImage img) {
    try {
      int result;
      ByteArrayOutputStream barray = new ByteArrayOutputStream();

      ImageIO.write(img, "png", barray);
      barray.flush();
      byte[] ib = barray.toByteArray();
      barray.close();
      result = Arrays.hashCode(ib);

      return result;
    }
    catch (IOException __) {
      return 0;
    }
  }

  /**
   * Writes an image to a file. If the file already exists, it is overwritten.
   * 
   * @param image the image
   * @param file the file
   */
  public static void writeImage(BufferedImage image, String file) throws IOException {
    Optional<String> opt = FilesUtil.getExtension(file);
    String ext = opt.orElseThrow(() -> new IOException("file has no extension"));
    ImageIO.write(image, ext, new File(file));
  }

  private Images() {}
}
