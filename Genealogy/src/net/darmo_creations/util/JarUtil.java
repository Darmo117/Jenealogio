package net.darmo_creations.util;

import java.io.File;
import java.net.URISyntaxException;

import net.darmo_creations.dao.ConfigDao;

public class JarUtil {
  private static String dir;

  /**
   * @return the path to the jar
   */
  public static String getJarDir() {
    if (dir == null) {
      try {
        String path = ConfigDao.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        if (File.separatorChar == '\\') {
          path = path.replace('/', '\\');
          path = path.substring(1); // Removes the first /.
        }

        dir = path;
      }
      catch (URISyntaxException __) {}
    }

    return dir;
  }

  public JarUtil() {}
}
