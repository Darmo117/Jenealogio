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

import java.util.Calendar;
import java.util.Optional;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.date.DateBuilder;
import net.darmo_creations.utils.I18n;

/**
 * Useful functions to handle dates.
 * 
 * @author Damien Vergnet
 */
public final class CalendarUtil {
  /**
   * Returns the current date.
   */
  public static Date getCurrentDate() {
    Calendar c = net.darmo_creations.gui_framework.util.CalendarUtil.getCurrentDate();
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
    if (date.isPresent()) {
      Date d = date.get();
      String y = d.isYearSet() ? "" + d.getYear() : "????";
      String m = d.isMonthSet() ? "" + d.getMonth() : "??";
      String dd = d.isDateSet() ? "" + d.getDate() : "??";

      return Optional.of(I18n.getFormattedDate(y, m, dd));
    }
    return Optional.empty();
  }

  private CalendarUtil() {}
}
