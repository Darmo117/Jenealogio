package net.darmo_creations.jenealogio.model.family;

import static org.junit.Assert.*;

import java.time.Period;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.date.DateBuilder;
import net.darmo_creations.jenealogio.util.Images;

public class FamilyMemberTest {
  private FamilyMember m1, m2;

  @Before
  public void setUp() throws Exception {
    this.m1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, null, null, null, null, false, null);
    DateBuilder builder = new DateBuilder();
    builder.setYear(1932);
    builder.setMonth(12);
    builder.setDate(2);
    Date birth = builder.getDate();
    builder.setYear(1999);
    builder.setDate(16);
    Date death = builder.getDate();
    this.m2 = new FamilyMember(1, ImageIO.read(getClass().getResourceAsStream("/net/darmo_creations/jenealogio/model/family/lena.bmp")),
        "Smith", null, "Richard", "Oliver", Gender.MAN, birth, "London", death, "Manchester", true, "Blop!");
  }

  @After
  public void tearDown() throws Exception {
    this.m1 = null;
    this.m2 = null;
    System.gc();
  }

  @Test
  public void testEquals() {
    assertEquals(this.m2, this.m2.clone());
  }

  @Test
  public void testHashcode() {
    assertEquals(this.m2.hashCode(), this.m2.clone().hashCode());
  }

  @Test
  public void testImageHashcode() {
    assertEquals(Images.hashCode(this.m2.getImage().get()), Images.hashCode(this.m2.clone().getImage().get()));
  }

  @Test
  public void testFullAgeDaysDifferent() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    this.m1.setDeathDate(getDate(2000, 1, 2));
    assertEquals(Period.of(0, 0, 1), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeMonthsDifferent() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    this.m1.setDeathDate(getDate(2000, 2, 1));
    assertEquals(Period.of(0, 1, 0), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeYearsDifferent() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    this.m1.setDeathDate(getDate(2001, 1, 1));
    assertEquals(Period.of(1, 0, 0), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month31Days() {
    this.m1.setBirthDate(getDate(2000, 1, 2));
    this.m1.setDeathDate(getDate(2000, 2, 1));
    assertEquals(Period.of(0, 0, 30), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month30Days() {
    this.m1.setBirthDate(getDate(2000, 4, 2));
    this.m1.setDeathDate(getDate(2000, 5, 1));
    assertEquals(Period.of(0, 0, 29), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month29Days() {
    this.m1.setBirthDate(getDate(2000, 2, 2));
    this.m1.setDeathDate(getDate(2000, 3, 1));
    assertEquals(Period.of(0, 0, 28), this.m1.getAge().get());
  }

  @Test
  public void testFullAgeLessThan1Month28Days() {
    this.m1.setBirthDate(getDate(2002, 2, 2));
    this.m1.setDeathDate(getDate(2002, 3, 1));
    assertEquals(Period.of(0, 0, 27), this.m1.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverse() {
    this.m1.setBirthDate(getDate(2000, 2, 1));
    this.m1.setDeathDate(getDate(2001, 1, 1));
    assertEquals(Period.of(0, 11, 0), this.m1.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDays31() {
    this.m1.setBirthDate(getDate(2000, 5, 1));
    this.m1.setDeathDate(getDate(2001, 4, 2));
    assertEquals(Period.of(0, 11, 1), this.m1.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDays30() {
    this.m1.setBirthDate(getDate(2000, 4, 1));
    this.m1.setDeathDate(getDate(2001, 3, 2));
    assertEquals(Period.of(0, 11, 1), this.m1.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDaysInverse31() {
    this.m1.setBirthDate(getDate(2000, 5, 2));
    this.m1.setDeathDate(getDate(2001, 4, 1));
    assertEquals(Period.of(0, 10, 30), this.m1.getAge().get());
  }

  @Test
  public void testAgeFullMonthInverseDaysInverse30() {
    this.m1.setBirthDate(getDate(2000, 4, 2));
    this.m1.setDeathDate(getDate(2001, 3, 1));
    assertEquals(Period.of(0, 10, 29), this.m1.getAge().get());
  }

  @Test
  public void testYoungerThan() {
    this.m1.setBirthDate(getDate(2000, 1, 2));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, getDate(2000, 1, 1), null, null, null, false, null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOlderThan() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, getDate(2000, 1, 2), null, null, null, false, null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testSameAge() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, getDate(2000, 1, 1), null, null, null, false, null);
    Optional<Integer> opt = this.m1.compareBirthdays(p1);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testNoBirthDate() {
    this.m1.setBirthDate(getDate(2000, 1, 1));
    FamilyMember p1 = new FamilyMember(null, null, null, null, null, Gender.UNKNOW, null, null, null, null, false, null);
    assertFalse(this.m1.compareBirthdays(p1).isPresent());
  }

  @Test
  public void testOnlyYearsBefore() {
    this.m1.setBirthDate(getDate(2000, null, null));
    this.m2.setBirthDate(getDate(2001, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOnlyYearsSame() {
    this.m1.setBirthDate(getDate(2000, null, null));
    this.m2.setBirthDate(getDate(2000, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testOnlyYearsAfter() {
    this.m1.setBirthDate(getDate(2001, null, null));
    this.m2.setBirthDate(getDate(2000, null, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(getDate(2000, 1, null));
    this.m2.setBirthDate(getDate(2001, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsSameDaysNull() {
    this.m1.setBirthDate(getDate(2000, 1, null));
    this.m2.setBirthDate(getDate(2001, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsBeforeAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(getDate(2000, 2, null));
    this.m2.setBirthDate(getDate(2001, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(getDate(2000, 1, null));
    this.m2.setBirthDate(getDate(2000, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsSameDaysNull() {
    this.m1.setBirthDate(getDate(2000, 1, null));
    this.m2.setBirthDate(getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsSameAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(getDate(2000, 2, null));
    this.m2.setBirthDate(getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(getDate(2001, 1, null));
    this.m2.setBirthDate(getDate(2000, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsSameDaysNull() {
    this.m1.setBirthDate(getDate(2001, 1, null));
    this.m2.setBirthDate(getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsAfterAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(getDate(2001, 2, null));
    this.m2.setBirthDate(getDate(2000, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsBeforeDaysNull() {
    this.m1.setBirthDate(getDate(null, 1, null));
    this.m2.setBirthDate(getDate(null, 2, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsSameDaysNull() {
    this.m1.setBirthDate(getDate(null, 1, null));
    this.m2.setBirthDate(getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsAfterDaysNull() {
    this.m1.setBirthDate(getDate(null, 2, null));
    this.m2.setBirthDate(getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysBefore() {
    this.m1.setBirthDate(getDate(null, null, 1));
    this.m2.setBirthDate(getDate(null, null, 2));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(-1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysSame() {
    this.m1.setBirthDate(getDate(null, null, 1));
    this.m2.setBirthDate(getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testYearsNullAndMonthsNullDaysAfter() {
    this.m1.setBirthDate(getDate(null, null, 2));
    this.m2.setBirthDate(getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(1, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testNotComparableDates1() {
    this.m1.setBirthDate(getDate(null, 1, 1));
    this.m2.setBirthDate(getDate(null, null, 1));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  @Test
  public void testNotComparableDates2() {
    this.m1.setBirthDate(getDate(2000, 1, null));
    this.m2.setBirthDate(getDate(null, 1, null));
    Optional<Integer> opt = this.m1.compareBirthdays(this.m2);
    if (opt.isPresent())
      assertEquals(0, (int) opt.get());
    else
      fail("empty result");
  }

  private static Date getDate(Integer year, Integer month, Integer day) {
    DateBuilder builder = new DateBuilder();

    if (year != null)
      builder.setYear(year);
    if (month != null)
      builder.setMonth(month);
    if (day != null)
      builder.setDate(day);

    return builder.getDate();
  }
}
