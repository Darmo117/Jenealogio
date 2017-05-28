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

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.DateBuilder;

/**
 * Useful functions to handle dates.
 * 
 * @author Damien Vergnet
 */
public class CalendarUtil {
  /**
   * @return the current date
   */
  public static Date getCurrentDate() {
    long offset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
    // Avoid null if no ID found
    String id = "";

    for (String i : TimeZone.getAvailableIDs()) {
      TimeZone tz = TimeZone.getTimeZone(i);

      if (tz.getRawOffset() == offset) {
        id = i;
        break;
      }
    }
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone(id), Locale.FRENCH);

    DateBuilder builder = new DateBuilder();
    builder.setYear(c.get(Calendar.YEAR));
    builder.setMonth(c.get(Calendar.MONTH) + 1);
    builder.setDate(c.get(Calendar.DAY_OF_MONTH));

    return builder.getDate();
  }

  /**
   * Formats the given date.
   * 
   * @param date the date
   * @return the formatted date or nothing if there is no date
   */
  public static Optional<String> formatDate(Optional<Date> date) {
    if (date.isPresent())
      return Optional.of(I18n.getFormattedDate(date.get()));
    return Optional.empty();
  }

  private CalendarUtil() {}
}
