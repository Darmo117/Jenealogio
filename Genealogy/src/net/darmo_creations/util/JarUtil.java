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

import java.io.File;
import java.net.URISyntaxException;

import net.darmo_creations.dao.ConfigDao;

public class JarUtil {
  private static String dir;

  public static String getJarDir() {
    return getJarDir(File.separatorChar);
  }

  /**
   * @return the path to the jar
   */
  public static String getJarDir(char separator) {
    if (dir == null) {
      try {
        String path = ConfigDao.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        if (File.separatorChar == '\\') {
          path = path.substring(1); // Removes the first /.
        }

        dir = path;
      }
      catch (URISyntaxException __) {}
    }

    return dir.replace('/', separator);
  }

  public JarUtil() {}
}
