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
package net.darmo_creations.util;

/**
 * This class represents a version number of the following format:
 * 
 * <pre>
 * $MAJOR.$MINOR[.$PATCH][d]
 * </pre>
 * <ul>
 * <li>{@code $MAJOR}: major version</li>
 * <li>{@code $MINOR}: minor version</li>
 * <li>{@code $PATCH}: patch version (only displayed if not 0)</li>
 * <li>{@code d}: indicates that this is a development version</li>
 * </ul>
 * E.g.: {@code 1.2}, {@code 2.0.6d}, {@code 3.4d}, {@code 1.0.6}…
 *
 * @author Damien Vergnet
 */
public final class Version implements Comparable<Version> {
  /** Current version */
  public static final Version CURRENT_VERSION;

  /** Version 1.3d */
  public static final Version V1_3D = new Version(1, 3, 0, true);

  static {
    CURRENT_VERSION = new Version(1, 4, 1, false);
  }

  private final int major, minor, patch;
  private final boolean indev;

  /**
   * Creates a version.
   * 
   * @param major major version
   * @param minor minor version
   * @param patch patch version
   * @param indev is it in development?
   */
  public Version(int major, int minor, int patch, boolean indev) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.indev = indev;
  }

  /**
   * Creates a version from an integer value.
   * 
   * @param version the value
   */
  public Version(int version) {
    this((version >> 16) & 0xff, (version >> 8) & 0xff, version & 0xff, ((version >> 31) & 1) == 1);
  }

  public int getMajor() {
    return this.major;
  }

  public int getMinor() {
    return this.minor;
  }

  public int getPatch() {
    return this.patch;
  }

  public boolean isIndev() {
    return this.indev;
  }

  /**
   * @return the integer value for this version, including indev bit
   */
  public int getFullValue() {
    return getValue() | (this.indev ? 1 : 0) << 31;
  }

  /**
   * @return the integer value for this version, without indev bit
   */
  public int getValue() {
    return (this.major & 0xff) << 16 | (this.minor & 0xff) << 8 | (this.patch & 0xff);
  }

  /**
   * Tells if this version is before the given one.
   * 
   * @param v another version
   * @return true if this version is strictly before the argument
   */
  public boolean before(Version v) {
    return compareTo(v) < 0;
  }

  /**
   * Tells if this version is after the given one.
   * 
   * @param v another version
   * @return true if this version is strictly after the argument
   */
  public boolean after(Version v) {
    return compareTo(v) > 0;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Version)
      return ((Version) o).compareTo(this) == 0;
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + (this.indev ? 1231 : 1237);
    result = prime * result + this.major;
    result = prime * result + this.minor;
    result = prime * result + this.patch;

    return result;
  }

  @Override
  public int compareTo(Version o) {
    if (Integer.compare(getValue(), o.getValue()) > 0 || o.isIndev() && !isIndev() && Integer.compare(getValue(), o.getValue()) == 0) {
      return 1;
    }
    else if (Integer.compare(getValue(), o.getValue()) < 0 || !o.isIndev() && isIndev() && Integer.compare(getValue(), o.getValue()) == 0) {
      return -1;
    }
    return 0;
  }

  /**
   * Formats this version as {@code major.minor[.patch][d]} (patch is added only if it is not 0).
   * The "d" indicates if the version is in development.
   * 
   * @return the formatted version
   * @see Version
   */
  @Override
  public String toString() {
    String dev = this.indev ? "d" : "";

    if (this.patch != 0)
      return String.format("%d.%d.%d%s", this.major, this.minor, this.patch, dev);
    return String.format("%d.%d%s", this.major, this.minor, dev);
  }
}
