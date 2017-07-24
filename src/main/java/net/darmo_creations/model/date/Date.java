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
  private int year, month, day, nbOfDaysInMonth;
  private boolean yearSet, monthSet, daySet;

  /**
   * Creates a date from a builder.
   * 
   * @param builder the builder
   */
  Date(DateBuilder builder) {
    this.yearSet = builder.isYearSet();
    this.monthSet = builder.isMonthSet();
    this.daySet = builder.isDateSet();

    Calendar calendar = (Calendar) builder.getCalendar().clone();
    if (this.yearSet)
      this.year = calendar.get(Calendar.YEAR);
    if (this.monthSet) {
      this.month = calendar.get(Calendar.MONTH) + 1;
      this.nbOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    if (this.daySet)
      this.day = calendar.get(Calendar.DAY_OF_MONTH);

    if (!this.yearSet && !this.monthSet && !this.daySet)
      throw new IllegalStateException("empty date");
  }

  /**
   * @return true if and only if the year, month and date are known
   */
  public boolean isIncomplete() {
    return !this.yearSet || !this.monthSet || !this.daySet;
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
      return this.year;
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
      return this.month;
    throw new NullPointerException("month not set");
  }

  /**
   * @return the number of days in the current month
   */
  public int getDaysNbInMonth() {
    if (isMonthSet())
      return this.nbOfDaysInMonth;
    throw new NullPointerException("month not set");
  }

  /**
   * @return true if the date is set
   */
  public boolean isDateSet() {
    return this.daySet;
  }

  /**
   * @return the day in the month (1 for the first day)
   */
  public int getDate() {
    if (isDateSet())
      return this.day;
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

    if (getYear() < other.getYear() || getYear() == other.getYear() && getMonth() < other.getMonth()
        || getYear() == other.getYear() && getMonth() == other.getMonth() && getDate() < other.getDate())
      return -1;
    if (getYear() > other.getYear() || getYear() == other.getYear() && getMonth() > other.getMonth()
        || getYear() == other.getYear() && getMonth() == other.getMonth() && getDate() > other.getDate())
      return 1;
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + this.day;
    result = prime * result + (this.daySet ? 1231 : 1237);
    result = prime * result + this.month;
    result = prime * result + (this.monthSet ? 1231 : 1237);
    result = prime * result + this.nbOfDaysInMonth;
    result = prime * result + this.year;
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
    if (this.day != other.day)
      return false;
    if (this.daySet != other.daySet)
      return false;
    if (this.month != other.month)
      return false;
    if (this.monthSet != other.monthSet)
      return false;
    if (this.nbOfDaysInMonth != other.nbOfDaysInMonth)
      return false;
    if (this.year != other.year)
      return false;
    if (this.yearSet != other.yearSet)
      return false;
    return true;
  }

  @Override
  public Date clone() {
    try {
      return (Date) super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
