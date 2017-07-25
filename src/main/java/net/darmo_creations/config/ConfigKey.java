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
 * {@code ConfigKey}s are used by the {@code GlobalConfig} class.
 * 
 * @author Damien Vergnet
 *
 * @param <T> the type of the associated values
 */
public interface ConfigKey<T> {
  /**
   * @return this key's name
   */
  String getName();

  /**
   * Returns the key matching the given name.
   * 
   * @param name the name
   * @param keyClass the class of the returned key
   * @return the key or null if no key were found
   */
  static <U extends Enum<U> & ConfigKey<?>> U fromName(String name, Class<U> keyClass) {
    for (U value : keyClass.getEnumConstants()) {
      if (value.getName().equals(name))
        return value;
    }
    return null;
  }
}
