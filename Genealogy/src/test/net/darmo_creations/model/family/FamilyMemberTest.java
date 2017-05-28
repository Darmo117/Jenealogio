package net.darmo_creations.model.family;

import static org.junit.Assert.*;

import java.time.Period;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.DateBuilder;

public class FamilyMemberTest {
  private FamilyMember p;

  @Before
  public void setUp() throws Exception {
    this.p = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, null, null, null, null, false, null);
  }

  @After
  public void tearDown() throws Exception {
    this.p = null;
  }

  @Test
  public void testFullAgeDaysDifferent() {
    this.p.setBirthDate(getDate(2000, 1, 1));
    this.p.setDeathDate(getDate(2000, 1, 2));
    assertEquals(Period.of(0, 0, 1), this.p.getAge().get());
  }

  @Test
  public void testFullAgeMonthsDifferent() {
    this.p.setBirthDate(getDate(2000, 1, 1));
    this.p.setDeathDate(getDate(2000, 2, 1));
    assertEquals(Period.of(0, 1, 0), this.p.getAge().get());
  }

  @Test
  public void testFullAgeYearsDifferent() {
    this.p.setBirthDate(getDate(2000, 1, 1));
    this.p.setDeathDate(getDate(2001, 1, 1));
    assertEquals(Period.of(1, 0, 0), this.p.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month31Days() {
    this.p.setBirthDate(getDate(2000, 1, 2));
    this.p.setDeathDate(getDate(2000, 2, 1));
    assertEquals(Period.of(0, 0, 30), this.p.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month30Days() {
    this.p.setBirthDate(getDate(2000, 4, 2));
    this.p.setDeathDate(getDate(2000, 5, 1));
    assertEquals(Period.of(0, 0, 29), this.p.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month29Days() {
    this.p.setBirthDate(getDate(2000, 2, 2));
    this.p.setDeathDate(getDate(2000, 3, 1));
    assertEquals(Period.of(0, 0, 28), this.p.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month28Days() {
    this.p.setBirthDate(getDate(2002, 2, 2));
    this.p.setDeathDate(getDate(2002, 3, 1));
    assertEquals(Period.of(0, 0, 27), this.p.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverse() {
    this.p.setBirthDate(getDate(2000, 2, 1));
    this.p.setDeathDate(getDate(2001, 1, 1));
    assertEquals(Period.of(0, 11, 0), this.p.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDays31() {
    this.p.setBirthDate(getDate(2000, 5, 1));
    this.p.setDeathDate(getDate(2001, 4, 2));
    assertEquals(Period.of(0, 11, 1), this.p.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDays30() {
    this.p.setBirthDate(getDate(2000, 4, 1));
    this.p.setDeathDate(getDate(2001, 3, 2));
    assertEquals(Period.of(0, 11, 1), this.p.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDaysInverse31() {
    this.p.setBirthDate(getDate(2000, 5, 2));
    this.p.setDeathDate(getDate(2001, 4, 1));
    assertEquals(Period.of(0, 10, 30), this.p.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDaysInverse30() {
    this.p.setBirthDate(getDate(2000, 4, 2));
    this.p.setDeathDate(getDate(2001, 3, 1));
    assertEquals(Period.of(0, 10, 29), this.p.getAge().get());
  }

  private static Date getDate(int year, int month, int day) {
    DateBuilder builder = new DateBuilder();

    builder.setYear(year);
    builder.setMonth(month);
    builder.setDate(day);

    return builder.getDate();
  }
}
