package net.darmo_creations.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.darmo_creations.model.Date;

public class I18n {
  private static final Locale DEFAULT_LOCALE = Locale.US;

  private static Locale locale;
  private static ResourceBundle resource;

  public static void init() {
    try {
      List<String> lines = Files.readAllLines(Paths.get(getJarDir() + "config.dat"));
      if (!lines.isEmpty()) {
        String[] s = lines.get(0).split("_");

        locale = new Locale(s[0], s[1]);
      }
    }
    catch (IOException e) {
      locale = DEFAULT_LOCALE;
    }
    resource = ResourceBundle.getBundle("langs/lang", locale);
  }

  public static boolean saveLocale() {
    try {
      List<String> lines = new ArrayList<>();
      lines.add(locale.toString());
      Files.write(Paths.get(getJarDir() + "config.dat"), lines);

      return true;
    }
    catch (IOException e) {
      return false;
    }
  }

  public static String getFormattedDate(Date date) {
    DateTimeFormatter f = DateTimeFormatter.ofPattern(I18n.getLocalizedString("date.format"));
    return f.format(LocalDate.of(date.getYear(), date.getMonth(), date.getDate()));
  }

  public static String getLocalizedString(String unlocalizedString) {
    try {
      return resource.getString(unlocalizedString);
    }
    catch (MissingResourceException e) {
      return unlocalizedString;
    }
  }

  public static String getLocalizedWord(String unlocalizedWord, boolean plural) {
    return getLocalizedString("word." + unlocalizedWord + (plural ? ".plural" : ""));
  }

  public static char getLocalizedMnemonic(String unlocalizedString) {
    return getLocalizedString(unlocalizedString + ".mnemonic").charAt(0);
  }

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
}
