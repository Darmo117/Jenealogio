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

import java.util.Optional;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.utils.I18n;

/**
 * Useful functions to handle dates.
 * 
 * @author Damien Vergnet
 */
public final class CalendarUtil {
  /**
   * Formats the given date.
   * 
   * @param date the date
   * @return the formatted date or nothing if there is no date
   */
  public static Optional<String> formatDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      String y = d.isYearSet() ? "" + d.getYear() : null;
      String m = d.isMonthSet() ? "" + d.getMonth() : null;
      String dd = d.isDateSet() ? "" + d.getDate() : null;

      return Optional.of(I18n.getFormattedDate(y, m, dd));
    }
    return Optional.empty();
  }

  private CalendarUtil() {}
}
