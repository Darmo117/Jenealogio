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
import java.net.URI;
import java.net.URISyntaxException;

import net.darmo_creations.Start;

/**
 * This class provides utility functions to find Jar path.
 *
 * @author Damien Vergnet
 */
public final class JarUtil {
  private static String dir;

  /**
   * @return the jar's directory
   */
  public static String getJarDir() {
    if (dir == null) {
      String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

      if (File.separatorChar == '\\')
        path = path.substring(1); // Removes the first '/'.
      dir = path;
    }

    return dir.replace('/', File.separatorChar);
  }

  public static URI getJar() throws URISyntaxException {
    return Start.class.getProtectionDomain().getCodeSource().getLocation().toURI();
  }

  public JarUtil() {}
}
