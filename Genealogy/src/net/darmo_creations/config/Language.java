package net.darmo_creations.config;

import java.util.Locale;

/**
 * This enum lists all available languages.
 *
 * @author Damien Vergnet
 */
public enum Language {
  ENGLISH("English", "en_US", Locale.US),
  FRENCH("Français", "fr_FR", Locale.FRANCE),
  ESPERANTO("Esperanto", "eo", new Locale("eo"));

  private final String name;
  private final String code;
  private final Locale locale;

  private Language(String name, String code, Locale locale) {
    this.name = name;
    this.code = code;
    this.locale = locale;
  }

  /**
   * @return the name written in the language
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return language code
   */
  public String getCode() {
    return this.code;
  }

  /**
   * @return corresponding locale
   */
  public Locale getLocale() {
    return (Locale) this.locale.clone();
  }

  @Override
  public String toString() {
    return getName();
  }

  /**
   * Returns the value matching the code.
   * 
   * @param code language code
   * @return the matching value
   */
  public static Language fromCode(String code) {
    for (Language l : values()) {
      if (l.getCode().equals(code))
        return l;
    }
    return null;
  }
}