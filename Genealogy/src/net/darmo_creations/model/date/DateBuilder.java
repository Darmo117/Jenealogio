package net.darmo_creations.model.date;

import java.util.Calendar;

/**
 * This builder constructs a {@link Date} object.
 *
 * @author Damien Vergnet
 */
public final class DateBuilder {
  private Calendar calendar;
  private boolean yearSet, monthSet, dateSet;

  /**
   * Creates a builder.
   */
  public DateBuilder() {
    this.calendar = Calendar.getInstance();
    this.yearSet = false;
    this.monthSet = false;
    this.dateSet = false;
  }

  /**
   * Sets the year.
   * 
   * @param year the year
   */
  public void setYear(int year) {
    this.calendar.set(Calendar.YEAR, year);
    this.yearSet = true;
  }

  /**
   * Sets the month.
   * 
   * @param month the month
   */
  public void setMonth(int month) {
    this.calendar.set(Calendar.MONTH, month - 1);
    this.monthSet = true;
  }

  /**
   * Sets the date.
   * 
   * @param date the date
   */
  public void setDate(int date) {
    this.calendar.set(Calendar.DAY_OF_MONTH, date);
    this.dateSet = true;
  }

  /**
   * @return the constructed date or null if nothing has been set
   */
  public Date getDate() {
    if (this.yearSet || this.monthSet || this.dateSet)
      return new Date(this);
    return null;
  }

  Calendar getCalendar() {
    return this.calendar;
  }

  boolean isYearSet() {
    return this.yearSet;
  }

  boolean isMonthSet() {
    return this.monthSet;
  }

  boolean isDateSet() {
    return this.dateSet;
  }
}
