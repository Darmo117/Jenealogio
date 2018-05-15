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
package net.darmo_creations.jenealogio.model.family;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.date.Date;

public class AdoptionListEntryTest {
  private AdoptionListEntry entry;

  @Before
  public void setUp() throws Exception {
    this.entry = new AdoptionListEntry(new FamilyMemberMock(), false, null);
  }

  @Test
  public void testIsAdopted() {
    this.entry.setAdopted(true);
    assertTrue(this.entry.isAdopted());
  }

  @Test
  public void testIsAdoptedDate() {
    this.entry.setAdopted(false);
    this.entry.setAdoptionDate(Date.getDate(2000, 1, 1));
    assertTrue(this.entry.isAdopted());
  }

  @Test
  public void testIsAdoptedDate2() {
    this.entry.setAdoptionDate(Date.getDate(2000, 1, 1));
    this.entry.setAdopted(false);
    assertTrue(this.entry.isAdopted());
  }

  @Test
  public void testIsAdoptedNoDate() {
    this.entry.setAdopted(true);
    this.entry.setAdoptionDate(null);
    assertTrue(this.entry.isAdopted());
  }

  @Test
  public void testIsNotAdopted() {
    this.entry.setAdopted(false);
    assertFalse(this.entry.isAdopted());
  }
}
