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
package net.darmo_creations.model.family;

/**
 * This enumeration represents the gender of a person, plus an unknown gender constant.Each value
 * has a name and a code.
 * 
 * @author Damien Vergnet
 */
public enum Gender {
  UNKNOW("word.unknown", ""),
  MAN("word.man", "M"),
  WOMAN("word.woman", "F");

  private final String name;
  private final String code;

  private Gender(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public boolean isMan() {
    return this == MAN;
  }

  public boolean isWoman() {
    return this == WOMAN;
  }

  /**
   * Returns the code for a value.<br/>
   * Values are:
   * <ul>
   * <li>UNKNOWN: "" (empty string)</li>
   * <li>MAN: "M"</li>
   * <li>WOMAN: "F"</li>
   * </ul>
   * 
   * @return the code
   */
  public String getCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Returns the value for the given code. See {@link #getCode()} documentation for the allowed
   * values.
   * 
   * @param code the code
   * @return the gender or null if none match the code
   */
  public static Gender fromCode(String code) {
    for (Gender g : Gender.values()) {
      if (g.getCode().equals(code))
        return g;
    }
    return null;
  }
}
