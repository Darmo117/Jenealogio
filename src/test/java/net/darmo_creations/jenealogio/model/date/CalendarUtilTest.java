/*
 * Copyright © 2018 Damien Vergnet
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
package net.darmo_creations.jenealogio.model.date;

import static org.junit.Assert.*;

import java.time.Period;

import org.junit.Test;

import net.darmo_creations.jenealogio.util.CalendarUtil;

public class CalendarUtilTest {
  @Test
  public void testFullPeriodDaysDifferent() {
    Date start = Date.getDate(2000, 1, 1);
    Date end = Date.getDate(2000, 1, 2);
    assertEquals(Period.of(0, 0, 1), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodMonthsDifferent() {
    Date start = Date.getDate(2000, 1, 1);
    Date end = Date.getDate(2000, 2, 1);
    assertEquals(Period.of(0, 1, 0), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodYearsDifferent() {
    Date start = Date.getDate(2000, 1, 1);
    Date end = Date.getDate(2001, 1, 1);
    assertEquals(Period.of(1, 0, 0), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodLessThan1Month31Days() {
    Date start = Date.getDate(2000, 1, 2);
    Date end = Date.getDate(2000, 2, 1);
    assertEquals(Period.of(0, 0, 30), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodLessThan1Month30Days() {
    Date start = Date.getDate(2000, 4, 2);
    Date end = Date.getDate(2000, 5, 1);
    assertEquals(Period.of(0, 0, 29), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodLessThan1Month29Days() {
    Date start = Date.getDate(2000, 2, 2);
    Date end = Date.getDate(2000, 3, 1);
    assertEquals(Period.of(0, 0, 28), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testFullPeriodLessThan1Month28Days() {
    Date start = Date.getDate(2002, 2, 2);
    Date end = Date.getDate(2002, 3, 1);
    assertEquals(Period.of(0, 0, 27), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testPeriodFullMonthInverse() {
    Date start = Date.getDate(2000, 2, 1);
    Date end = Date.getDate(2001, 1, 1);
    assertEquals(Period.of(0, 11, 0), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testPeriodFullMonthInverseDays31() {
    Date start = Date.getDate(2000, 5, 1);
    Date end = Date.getDate(2001, 4, 2);
    assertEquals(Period.of(0, 11, 1), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testPeriodFullMonthInverseDays30() {
    Date start = Date.getDate(2000, 4, 1);
    Date end = Date.getDate(2001, 3, 2);
    assertEquals(Period.of(0, 11, 1), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testPeriodFullMonthInverseDaysInverse31() {
    Date start = Date.getDate(2000, 5, 2);
    Date end = Date.getDate(2001, 4, 1);
    assertEquals(Period.of(0, 10, 30), CalendarUtil.getPeriod(start, end).get());
  }

  @Test
  public void testPeriodFullMonthInverseDaysInverse30() {
    Date start = Date.getDate(2000, 4, 2);
    Date end = Date.getDate(2001, 3, 1);
    assertEquals(Period.of(0, 10, 29), CalendarUtil.getPeriod(start, end).get());
  }

}
