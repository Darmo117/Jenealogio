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
package net.darmo_creations.config;

/**
 * This type of keys is associated with boolean values.
 *
 * @author Damien Vergnet
 */
public enum BooleanConfigKey implements ConfigKey<Boolean> {
  CHECK_UPDATES("check_updates");

  private final String name;

  private BooleanConfigKey(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Returns the key matching the given name.
   * 
   * @param name the name
   * @return the key or null if no key were found
   */
  public static BooleanConfigKey fromName(String name) {
    for (BooleanConfigKey key : values()) {
      if (key.getName().equals(name))
        return key;
    }
    return null;

  }
}
