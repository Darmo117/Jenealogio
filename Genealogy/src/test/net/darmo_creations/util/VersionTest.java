package net.darmo_creations.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class VersionTest {
  @Test
  public void testConstructorInt() {
    assertEquals(new Version(1, 2, 3, false), new Version(1 << 16 | 2 << 8 | 3));
    assertEquals(new Version(1, 2, 3, true), new Version(1 << 31 | 1 << 16 | 2 << 8 | 3));
  }

  @Test
  public void testGetValue() {
    int value = 1 << 16 | 2 << 8 | 3;
    assertEquals(value, new Version(1, 2, 3, false).getValue());
    assertEquals(value, new Version(1, 2, 3, true).getValue());
  }

  @Test
  public void testGetFullValue() {
    int value = 1 << 16 | 2 << 8 | 3;
    assertEquals(value, new Version(1, 2, 3, false).getFullValue());
    assertEquals(value | 1 << 31, new Version(1, 2, 3, true).getFullValue());
  }

  @Test
  public void testToString() {
    assertEquals("1.2.3", new Version(1, 2, 3, false).toString());
    assertEquals("1.2.3d", new Version(1, 2, 3, true).toString());
    assertEquals("1.2", new Version(1, 2, 0, false).toString());
    assertEquals("1.2d", new Version(1, 2, 0, true).toString());
  }

  @Test
  public void testEqualsNotDev() {
    Version v1 = new Version(1, 2, 3, false);
    Version v2 = new Version(1, 2, 3, false);

    assertEquals(v1, v2);
  }

  @Test
  public void testEqualsDev() {
    Version v1 = new Version(1, 2, 3, true);
    Version v2 = new Version(1, 2, 3, true);

    assertEquals(v1, v2);
  }

  @Test
  public void testCompareToV1EqualsV2() {
    Version v1 = new Version(1, 2, 3, false);
    Version v2 = new Version(1, 2, 3, false);

    assertEquals(0, v1.compareTo(v2));
  }

  @Test
  public void testCompareToV1DevEqualsV2Dev() {
    Version v1 = new Version(1, 2, 3, true);
    Version v2 = new Version(1, 2, 3, true);

    assertEquals(0, v1.compareTo(v2));
  }

  @Test
  public void testCompareToV1BeforeV2() {
    Version v1 = new Version(1, 2, 3, false);
    Version v2 = new Version(1, 2, 4, false);
    assertTrue(v1.compareTo(v2) < 0);
    v1 = new Version(1, 2, 3, true);
    v2 = new Version(1, 2, 4, true);
    assertTrue(v1.compareTo(v2) < 0);
  }

  @Test
  public void testCompareToV1DevBeforeV2() {
    Version v1 = new Version(1, 2, 3, true);
    Version v2 = new Version(1, 2, 3, false);
    assertTrue(v1.compareTo(v2) < 0);
    v1 = new Version(1, 2, 3, true);
    v2 = new Version(1, 2, 4, false);
    assertTrue(v1.compareTo(v2) < 0);
  }

  @Test
  public void testCompareToV1DevBeforeV2Dev() {
    Version v1 = new Version(1, 2, 2, true);
    Version v2 = new Version(1, 2, 3, true);
    assertTrue(v1.compareTo(v2) < 0);
  }

  @Test
  public void testCompareToV1BeforeV2Dev() {
    Version v1 = new Version(1, 2, 2, false);
    Version v2 = new Version(1, 2, 3, true);
    assertTrue(v1.compareTo(v2) < 0);
  }

  @Test
  public void testCompareToV1AfterV2() {
    Version v1 = new Version(1, 2, 4, false);
    Version v2 = new Version(1, 2, 3, false);
    assertTrue(v1.compareTo(v2) > 0);
    v1 = new Version(1, 2, 4, true);
    v2 = new Version(1, 2, 3, true);
    assertTrue(v1.compareTo(v2) > 0);
  }

  @Test
  public void testCompareToV1DevAfterV2() {
    Version v1 = new Version(1, 2, 4, true);
    Version v2 = new Version(1, 2, 3, false);
    assertTrue(v1.compareTo(v2) > 0);
  }

  @Test
  public void testCompareToV1DevAfterV2Dev() {
    Version v1 = new Version(1, 2, 3, true);
    Version v2 = new Version(1, 2, 2, true);
    assertTrue(v1.compareTo(v2) > 0);
  }

  @Test
  public void testCompareToV1AfterV2Dev() {
    Version v1 = new Version(1, 2, 4, false);
    Version v2 = new Version(1, 2, 3, true);
    assertTrue(v1.compareTo(v2) > 0);
  }
}
