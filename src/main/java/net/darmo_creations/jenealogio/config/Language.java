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
package net.darmo_creations.jenealogio.config;

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
   * @return a copy of the corresponding locale
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