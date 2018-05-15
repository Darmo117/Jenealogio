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

import org.junit.Before;
import org.junit.Test;

public class DateBuilderTest {
  private DateBuilder b;

  @Before
  public void setUp() throws Exception {
    this.b = new DateBuilder();
  }

  @Test
  public void testSetYear() {
    this.b.setYear(2000);
    assertTrue(this.b.isYearSet());
  }

  @Test
  public void testSetMonth() {
    this.b.setMonth(12);
    assertTrue(this.b.isMonthSet());
  }

  @Test
  public void testSetDay() {
    this.b.setDate(21);
    assertTrue(this.b.isDateSet());
  }

  @Test
  public void testGetFullDate() {
    this.b.setYear(2000);
    this.b.setMonth(12);
    this.b.setDate(21);
    Date date = this.b.getDate();
    assertEquals(2000, date.getYear());
    assertEquals(12, date.getMonth());
    assertEquals(21, date.getDate());
  }

  @Test(expected = NullPointerException.class)
  public void testGetDateNoYear() {
    this.b.setMonth(12);
    this.b.setDate(21);
    this.b.getDate().getYear();
  }

  @Test(expected = NullPointerException.class)
  public void testGetDateNoMonth() {
    this.b.setYear(2000);
    this.b.setDate(21);
    this.b.getDate().getMonth();
  }

  @Test(expected = NullPointerException.class)
  public void testGetDateNoDay() {
    this.b.setYear(2000);
    this.b.setMonth(12);
    this.b.getDate().getDate();
  }

  @Test(expected = IllegalStateException.class)
  public void testNoDate() {
    this.b.getDate();
  }
}
