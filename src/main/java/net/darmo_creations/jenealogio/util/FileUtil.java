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

import java.io.File;
import java.util.Optional;

/**
 * This class provides useful methods to handle files.
 *
 * @author Damien Vergnet
 */
public final class FileUtil {
  /** Extension for tree files. */
  public static final String TREE_FILE_EXT = "gtree";
  /** Image file extensions a tree can be exported into. */
  public static final String[] IMAGE_FILES_EXTS = {"bmp", "png", "jpg", "jpeg", "gif"};

  /**
   * Returns the extension for the given file.
   * 
   * @param file the file
   * @return the extension if any
   */
  public static Optional<String> getExtension(String file) {
    return getExtension(new File(file));
  }

  /**
   * Returns the extension for the given file.
   * 
   * @param file the file
   * @return the extension if any
   */
  public static Optional<String> getExtension(File file) {
    if (file.isDirectory())
      return Optional.empty();

    String name = file.getName();
    String ext = name.substring(name.lastIndexOf('.') + 1);

    return Optional.of(ext);
  }

  /**
   * Tells if the given file has the specified extension.
   * 
   * @param file the file
   * @param ext the extension
   * @return true if the file has the same extension
   */
  public static boolean hasExtension(String file, String ext) {
    return hasExtension(new File(file), ext);
  }

  /**
   * Tells if the given file has the specified extension.
   * 
   * @param file the file
   * @param ext the extension
   * @return true if the file has the same extension
   */
  public static boolean hasExtension(File file, String ext) {
    return ext.equalsIgnoreCase(getExtension(file).orElse(""));
  }

  private FileUtil() {}
}
