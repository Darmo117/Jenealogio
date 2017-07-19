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
package net.darmo_creations.model.date;

import java.util.Calendar;

/**
 * This class represents a date that can be incomplete. It uses an instance of
 * {@link java.util.Calendar} to handle data. <b>N.B.</b>: unlike the Calendar class, January is 1
 * not 0, and so on.
 * 
 * @author Damien Vergnet
 */
public final class Date implements Comparable<Date>, Cloneable {
  private Calendar calendar;
  private boolean yearSet, monthSet, dateSet;

  /**
   * Creates a date from a builder.
   * 
   * @param builder the builder
   */
  Date(DateBuilder builder) {
    this.calendar = (Calendar) builder.getCalendar().clone();
    this.yearSet = builder.isYearSet();
    this.monthSet = builder.isMonthSet();
    this.dateSet = builder.isDateSet();
    if (!this.yearSet && !this.monthSet && !this.dateSet)
      throw new IllegalStateException("empty date");
  }

  /**
   * @return true if and only if the year, month and date are known
   */
  public boolean isIncomplete() {
    return !this.yearSet || !this.monthSet || !this.dateSet;
  }

  /**
   * @return true if the year is set
   */
  public boolean isYearSet() {
    return this.yearSet;
  }

  /**
   * @return the year
   */
  public int getYear() {
    if (isYearSet())
      return this.calendar.get(Calendar.YEAR);
    throw new NullPointerException("year not set");
  }

  /**
   * @return true if the month is set
   */
  public boolean isMonthSet() {
    return this.monthSet;
  }

  /**
   * @return the month (1 for January)
   */
  public int getMonth() {
    if (isMonthSet())
      return this.calendar.get(Calendar.MONTH) + 1;
    throw new NullPointerException("month not set");
  }

  /**
   * @return the number of days in the current month
   */
  public int getDaysNbInMonth() {
    if (isMonthSet())
      return this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    throw new NullPointerException("month not set");
  }

  /**
   * @return true if the date is set
   */
  public boolean isDateSet() {
    return this.dateSet;
  }

  /**
   * @return the day in the month (1 for the first day)
   */
  public int getDate() {
    if (isDateSet())
      return this.calendar.get(Calendar.DAY_OF_MONTH);
    throw new NullPointerException("date not set");
  }

  /**
   * Returns whether this date represents a time before the time represented by the specified date.
   * 
   * @param other the date to be compared
   * @return true if the time of this date is before the time represented by {@code date}; false
   *         otherwise
   */
  public boolean before(Date other) {
    return compareTo(other) < 0;
  }

  /**
   * Returns whether this date represents a time after the time represented by the specified date.
   * 
   * @param other the date to be compared
   * @return true if the time of this date is after the time represented by {@code date}; false
   *         otherwise
   */
  public boolean after(Date other) {
    return compareTo(other) > 0;
  }

  @Override
  public int compareTo(Date other) {
    if (isYearSet() && isMonthSet() && other.isYearSet() && other.isMonthSet() && (!isDateSet() || !other.isDateSet())) {
      if (getYear() < other.getYear() || getYear() == other.getYear() && getMonth() < other.getMonth())
        return -1;
      if (getYear() > other.getYear() || getYear() == other.getYear() && getMonth() > other.getMonth())
        return 1;
      return 0;
    }

    if (isYearSet() && other.isYearSet() && (!isMonthSet() || !other.isMonthSet())) {
      if (getYear() < other.getYear())
        return -1;
      if (getYear() > other.getYear())
        return 1;
      return 0;
    }

    return this.calendar.compareTo(other.calendar);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((this.calendar == null) ? 0 : this.calendar.hashCode());
    result = prime * result + (this.dateSet ? 1231 : 1237);
    result = prime * result + (this.monthSet ? 1231 : 1237);
    result = prime * result + (this.yearSet ? 1231 : 1237);

    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Date other = (Date) obj;
    if (this.calendar == null) {
      if (other.calendar != null)
        return false;
    }
    else if (!this.calendar.equals(other.calendar))
      return false;
    if (this.dateSet != other.dateSet)
      return false;
    if (this.monthSet != other.monthSet)
      return false;
    if (this.yearSet != other.yearSet)
      return false;
    return true;
  }

  @Override
  public Date clone() {
    try {
      Date date = (Date) super.clone();

      date.calendar = Calendar.getInstance();
      if (isYearSet())
        date.calendar.set(Calendar.YEAR, getYear());
      if (isMonthSet())
        date.calendar.set(Calendar.MONTH, getMonth() - 1);
      if (isDateSet())
        date.calendar.set(Calendar.DAY_OF_MONTH, getDate());

      return date;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
