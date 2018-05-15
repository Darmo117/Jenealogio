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

import java.time.Period;
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

  public static Optional<Period> getPeriod(Date start, Date end) {
    if (!start.isIncomplete() && !end.isIncomplete()) {
      int endMonth = end.getMonth();
      int startMonth = start.getMonth();
      int endDay = end.getDate();
      int startDay = start.getDate();
      int years = end.getYear() - start.getYear();
      int months = 0;
      int days = 0;

      if (startMonth > endMonth || startMonth == endMonth && startDay > endDay) {
        years = years == 0 ? 0 : years - 1;
        months = 12 - (startMonth - endMonth);
        if (startDay > endDay) {
          if (months > 0)
            months--;
          days = start.getDaysNbInMonth() - (startDay - endDay);
        }
        else {
          days = endDay - startDay;
        }
      }
      else {
        months = endMonth - startMonth;
        if (startDay > endDay) {
          if (months > 0)
            months--;
          days = start.getDaysNbInMonth() - (startDay - endDay);
        }
        else
          days = endDay - startDay;
      }

      return Optional.of(Period.of(years, months, days));
    }
    else if (start.isYearSet() && start.isMonthSet() && end.isYearSet() && end.isMonthSet()) {
      int endMonth = end.getMonth();
      int startMonth = start.getMonth();
      int years = end.getYear() - start.getYear();
      int months = 0;

      if (startMonth > endMonth) {
        years = years == 0 ? 0 : years - 1;
        months = 12 - (startMonth - endMonth);
      }
      else
        months = endMonth - startMonth;

      return Optional.of(Period.of(years, months, 0));
    }
    else if (start.isYearSet() && end.isYearSet()) {
      return Optional.of(Period.ofYears(end.getYear() - start.getYear()));
    }

    return Optional.empty();
  }

  private CalendarUtil() {}
}
