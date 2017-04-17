package net.darmo_creations.dao;

import java.io.File;
import java.net.URISyntaxException;

import net.darmo_creations.model.GlobalConfig;
import net.darmo_creations.util.I18n;

public class ConfigDao {
  private static ConfigDao instance;

  public static ConfigDao getInstance() {
    if (instance == null)
      instance = new ConfigDao();
    return instance;
  }

  public GlobalConfig load() {
    return new GlobalConfig(); // TODO
  }

  public void save(GlobalConfig config) {
    // TODO
  }

  /**
   * @return the path to the jar
   */
  private static String getJarDir() {
    try {
      String path = I18n.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

      if (File.separatorChar == '\\') {
        path = path.replace('/', '\\');
        path = path.substring(1); // Removes the first /.
      }

      return path;
    }
    catch (URISyntaxException e) {
      return null;
    }
  }

  private ConfigDao() {}
}
