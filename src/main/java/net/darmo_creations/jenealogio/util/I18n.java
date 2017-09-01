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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.darmo_creations.jenealogio.model.date.Date;

/**
 * This class handles internationalization for the whole application.
 * 
 * @author Damien Vergnet
 */
public final class I18n {
  private static ResourceBundle resource;

  /**
   * Loads the preferred locale.<br/>
   * <b>This method must be called before any other from this class.</b>
   */
  public static void init(Locale locale) {
    try {
      InputStream in = I18n.class.getResourceAsStream("/assets/langs/" + locale + ".lang");
      resource = new PropertyResourceBundle(new InputStreamReader(in, StandardCharsets.UTF_8));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns a formatted dated with the localized date pattern.
   * 
   * @param date the date to format
   * @return the formatted date
   */
  public static String getFormattedDate(Date date) {
    String year = date.isYearSet() ? "" + date.getYear() : "????";
    String month = date.isMonthSet() ? "" + date.getMonth() : "??";
    String day = date.isDateSet() ? "" + date.getDate() : "??";

    switch (I18n.getLocalizedString("date.format")) {
      case "D/M/Y":
        return String.format("%2s/%2s/%4s", day, month, year).replace(' ', '0');
      case "M/D/Y":
        return String.format("%2s/%2s/%4s", month, day, year).replace(' ', '0');
      case "Y/M/D":
        return String.format("%4s/%2s/%2s", year, month, day).replace(' ', '0');
    }

    return "format error";
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
    catch (MissingResourceException __) {
      return unlocalizedString;
    }
  }

  /**
   * Returns the localized word corresponding to the given key. If no key was found, the key is
   * returned. No need to specify "word." at the beginning.
   * 
   * @param unlocalizedWord the unlocalized word
   * @param feminine if true, the feminine will be returned
   * @param plural if true, the plural will be returned
   * @return the localized word
   */
  public static String getLocalizedWord(String unlocalizedWord, boolean feminine, boolean plural) {
    return getLocalizedString("word." + unlocalizedWord + (feminine ? ".feminine" : "") + (plural ? ".plural" : ""));
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

  /**
   * Converts the given string to title case.
   * 
   * @param input the string
   * @return the string in title case
   */
  public static String toTitleCase(String input) {
    StringBuilder titleCase = new StringBuilder();
    boolean nextTitleCase = true;

    for (char c : input.toCharArray()) {
      if (Character.isSpaceChar(c)) {
        nextTitleCase = true;
      }
      else if (nextTitleCase) {
        c = Character.toTitleCase(c);
        nextTitleCase = false;
      }

      titleCase.append(c);
    }

    return titleCase.toString();
  }

  private I18n() {}
}
