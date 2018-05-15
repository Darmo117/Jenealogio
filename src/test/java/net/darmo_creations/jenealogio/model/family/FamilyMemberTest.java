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

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.date.Date;

public class FamilyMemberTest {
  private FamilyMember m1, m2;

  @Before
  public void setUp() throws Exception {
    this.m1 = new FamilyMemberMock();
    this.m2 = new FamilyMember(1, null, "Smith", null, "Richard", "Oliver", Gender.MAN, null, "London", null, "Manchester", true, "Blop!");
  }

  @Test
  public void testClone() {
    assertEquals(this.m2, this.m2.clone());
  }

  @Test
  public void testHashcode() {
    assertEquals(this.m2.hashCode(), this.m2.clone().hashCode());
  }

  @Test
  public void testImageNull() {
    this.m1.setImage(null);
    assertFalse(this.m1.getImage().isPresent());
  }

  @Test
  public void testEmptyFamilyName_EmptyString() {
    this.m1.setFamilyName("");
    assertFalse(this.m1.getFamilyName().isPresent());
  }

  @Test
  public void testEmptyUseName_EmptyString() {
    this.m1.setUseName("");
    assertFalse(this.m1.getUseName().isPresent());
  }

  @Test
  public void testEmptyFirstName_EmptyString() {
    this.m1.setFirstName("");
    assertFalse(this.m1.getFirstName().isPresent());
  }

  @Test
  public void testEmptyOtherNames_EmptyString() {
    this.m1.setOtherNames("");
    assertFalse(this.m1.getOtherNames().isPresent());
  }

  @Test
  public void testEmptyFamilyName_BlankString() {
    this.m1.setFamilyName(" ");
    assertFalse(this.m1.getFamilyName().isPresent());
  }

  @Test
  public void testEmptyUseName_BlankString() {
    this.m1.setUseName(" ");
    assertFalse(this.m1.getUseName().isPresent());
  }

  @Test
  public void testEmptyFirstName_BlankString() {
    this.m1.setFirstName(" ");
    assertFalse(this.m1.getFirstName().isPresent());
  }

  @Test
  public void testEmptyOtherNames_BlankString() {
    this.m1.setOtherNames(" ");
    assertFalse(this.m1.getOtherNames().isPresent());
  }

  @Test
  public void testIsMan() {
    this.m1.setGender(Gender.MAN);
    assertTrue(this.m1.isMan());
  }

  @Test
  public void testIsWoman() {
    this.m1.setGender(Gender.WOMAN);
    assertTrue(this.m1.isWoman());
  }

  @Test
  public void testIsUnknown() {
    this.m1.setGender(Gender.UNKNOW);
    assertFalse(this.m1.isMan());
    assertFalse(this.m1.isWoman());
  }

  @Test(expected = NullPointerException.class)
  public void testNullGenderError() {
    this.m1.setGender(null);
  }

  @Test
  public void testBirthDate() {
    this.m1.setBirthDate(Date.getDate(2000, 12, 12));
    assertTrue(this.m1.getBirthDate().isPresent());
  }

  @Test
  public void testNoBirthDate() {
    this.m1.setBirthDate(null);
    assertFalse(this.m1.getBirthDate().isPresent());
  }

  @Test
  public void testBirthLocation() {
    this.m1.setBirthLocation("Toulouse");
    assertTrue(this.m1.getBirthLocation().isPresent());
  }

  @Test
  public void testNoBirthLocation() {
    this.m1.setBirthLocation(null);
    assertFalse(this.m1.getBirthLocation().isPresent());
  }

  @Test
  public void testEmptyBirthLocation() {
    this.m1.setBirthLocation("");
    assertFalse(this.m1.getBirthLocation().isPresent());
  }

  @Test
  public void testBlankBirthLocation() {
    this.m1.setBirthLocation(" ");
    assertFalse(this.m1.getBirthLocation().isPresent());
  }

  @Test
  public void testDeathDate() {
    this.m1.setDeathDate(Date.getDate(2000, 12, 12));
    assertTrue(this.m1.getDeathDate().isPresent());
  }

  @Test
  public void testNoDeathDate() {
    this.m1.setDeathDate(null);
    assertFalse(this.m1.getDeathDate().isPresent());
  }

  @Test
  public void testDeathLocation() {
    this.m1.setDeathLocation("Toulouse");
    assertTrue(this.m1.getDeathLocation().isPresent());
  }

  @Test
  public void testNoDeathLocation() {
    this.m1.setDeathLocation(null);
    assertFalse(this.m1.getDeathLocation().isPresent());
  }

  @Test
  public void testEmptyDeathLocation() {
    this.m1.setDeathLocation("");
    assertFalse(this.m1.getDeathLocation().isPresent());
  }

  @Test
  public void testBlankDeathLocation() {
    this.m1.setDeathLocation(" ");
    assertFalse(this.m1.getDeathLocation().isPresent());
  }

  @Test
  public void testDead() {
    this.m1.setDead(true);
    assertTrue(this.m1.isDead());
  }

  @Test
  public void testNotDead() {
    this.m1.setDead(false);
    assertFalse(this.m1.isDead());
  }

  @Test
  public void testDeadWithDate() {
    this.m1.setDead(false);
    this.m1.setDeathDate(Date.getDate(2000, 1, 1));
    assertTrue(this.m1.isDead());
  }

  @Test
  public void testDeadWithLocation() {
    this.m1.setDead(false);
    this.m1.setDeathLocation("Toulouse");
    assertTrue(this.m1.isDead());
  }

  @Test
  public void checkDatesOrderOK() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 1));
    this.m1.setDeathDate(Date.getDate(2000, 1, 2));
  }

  @Test(expected = IllegalStateException.class)
  public void checkDatesOrderKO_BirthThenDeath() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 2));
    this.m1.setDeathDate(Date.getDate(2000, 1, 1));
  }

  @Test(expected = IllegalStateException.class)
  public void checkDatesOrderKO_DeathThenBirth() {
    this.m1.setDeathDate(Date.getDate(2000, 1, 1));
    this.m1.setBirthDate(Date.getDate(2000, 1, 2));
  }

  @Test
  public void testNullComment() {
    this.m1.setComment(null);
    assertFalse(this.m1.getComment().isPresent());
  }

  @Test
  public void testEmptyComment() {
    this.m1.setComment("");
    assertFalse(this.m1.getComment().isPresent());
  }

  @Test
  public void testBlankComment() {
    this.m1.setComment(" ");
    assertFalse(this.m1.getComment().isPresent());
  }

  @Test
  public void testYoungerThan() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 2));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, Date.getDate(2000, 1, 1), null, null, null, false,
        null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOlderThan() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, Date.getDate(2000, 1, 2), null, null, null, false,
        null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testSameAge() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, Date.getDate(2000, 1, 1), null, null, null, false,
        null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testAgeNoBirthDate() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, null, null, null, null, false, null);
    assertFalse(this.m1.compareBirthdays(p1).isPresent());
  }

  @Test
  public void testOnlyYearsBefore() {
    this.m1.setBirthDate(Date.getDate(2000, null, null));
    this.m2.setBirthDate(Date.getDate(2001, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOnlyYearsSame() {
    this.m1.setBirthDate(Date.getDate(2000, null, null));
    this.m2.setBirthDate(Date.getDate(2000, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOnlyYearsAfter() {
    this.m1.setBirthDate(Date.getDate(2001, null, null));
    this.m2.setBirthDate(Date.getDate(2000, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 1, null));
    this.m2.setBirthDate(Date.getDate(2001, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsSameDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 1, null));
    this.m2.setBirthDate(Date.getDate(2001, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 2, null));
    this.m2.setBirthDate(Date.getDate(2001, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 1, null));
    this.m2.setBirthDate(Date.getDate(2000, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsSameDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 1, null));
    this.m2.setBirthDate(Date.getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(Date.getDate(2000, 2, null));
    this.m2.setBirthDate(Date.getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(Date.getDate(2001, 1, null));
    this.m2.setBirthDate(Date.getDate(2000, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsSameDaysNull() {
    this.m1.setBirthDate(Date.getDate(2001, 1, null));
    this.m2.setBirthDate(Date.getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(Date.getDate(2001, 2, null));
    this.m2.setBirthDate(Date.getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(Date.getDate(null, 1, null));
    this.m2.setBirthDate(Date.getDate(null, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsSameDaysNull() {
    this.m1.setBirthDate(Date.getDate(null, 1, null));
    this.m2.setBirthDate(Date.getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(Date.getDate(null, 2, null));
    this.m2.setBirthDate(Date.getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysBefore() {
    this.m1.setBirthDate(Date.getDate(null, null, 1));
    this.m2.setBirthDate(Date.getDate(null, null, 2));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysSame() {
    this.m1.setBirthDate(Date.getDate(null, null, 1));
    this.m2.setBirthDate(Date.getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysAfter() {
    this.m1.setBirthDate(Date.getDate(null, null, 2));
    this.m2.setBirthDate(Date.getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testNotComparableDates1() {
    this.m1.setBirthDate(Date.getDate(null, 1, 1));
    this.m2.setBirthDate(Date.getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testNotComparableDates2() {
    this.m1.setBirthDate(Date.getDate(2000, 1, null));
    this.m2.setBirthDate(Date.getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test(expected = IllegalStateException.class)
  public void test() {
    this.m1.setBirthDate(Date.getDate(2000, 1, 2));
    this.m1.setDeathDate(Date.getDate(200, 1, 1));
  }
}
