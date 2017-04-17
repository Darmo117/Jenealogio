package net.darmo_creations.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.darmo_creations.model.Date;

/**
 * This class handles internationalization for the whole application.
 * 
 * @author Damien Vergnet
 */
public class I18n {
  private static ResourceBundle resource;

  /**
   * Loads the preferred locale.<br/>
   * <b>This method must be called before any other from this class.</b>
   */
  public static void init(Locale locale) {
    resource = ResourceBundle.getBundle("langs/lang", locale);
  }

  /**
   * Returns a formatted dated with the localized date pattern.
   * 
   * @param date the date to format
   * @return the formatted date
   */
  public static String getFormattedDate(Date date) {
    DateTimeFormatter f = DateTimeFormatter.ofPattern(I18n.getLocalizedString("date.format"));
    return f.format(LocalDate.of(date.getYear(), date.getMonth(), date.getDate()));
  }

  /**
   * Returns the localized string corresponding to the given key. If no key was found, the key is
   * returned.
   * 
   * @param unlocalizedString the unlocalized string
   * @return the localized string
   */
  public static String getLocalizedString(String unlocalizedString) {
    try {
      return resource.getString(unlocalizedString);
    }
    catch (MissingResourceException e) {
      return unlocalizedString;
    }
  }

  /**
   * Returns the localized word corresponding to the given key. If no key was found, the key is
   * returned. No need to specify "word." at the beginning.
   * 
   * @param unlocalizedWord the unlocalized word
   * @param plural if true, the plural will be returned
   * @return the localized word
   */
  public static String getLocalizedWord(String unlocalizedWord, boolean plural) {
    return getLocalizedString("word." + unlocalizedWord + (plural ? ".plural" : ""));
  }

  /**
   * Returns the localized mnemonic for the given key. If no key was found, '\0' (null character)
   * will be returned. No need to specify ".mnemonic" in the key.
   * 
   * @param unlocalizedString the key
   * @return the localized mnemonic
   */
  public static char getLocalizedMnemonic(String unlocalizedString) {
    String s = getLocalizedString(unlocalizedString + ".mnemonic");
    if (s.length() == 1)
      return s.charAt(0);
    return '\0';
  }

  public static enum Language {
    ENGLISH("English", "en_US", Locale.US),
    FRENCH("Français", "fr_FR", Locale.FRANCE);

    private final String name;
    private final String code;
    private final Locale locale;

    private Language(String name, String code, Locale locale) {
      this.name = name;
      this.code = code;
      this.locale = locale;
    }

    public String getName() {
      return this.name;
    }

    public String getCode() {
      return this.code;
    }

    public Locale getLocale() {
      return this.locale;
    }

    @Override
    public String toString() {
      return getName();
    }

    public static Language fromCode(String code) {
      for (Language l : values()) {
        if (l.getCode().equals(code))
          return l;
      }
      return null;
    }
  }
}
