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

import java.awt.Color;

/**
 * This type of keys are associated with the {@code Color} class.
 *
 * @author Damien Vergnet
 */
public enum ColorConfigKey implements ConfigKey<Color> {
  CARD_BORDER("card_border"),
  CARD_SELECTED_BORDER("card_selected_border"),
  GENDER_UNKNOWN("gender_unknown"),
  GENDER_MALE("gender_male"),
  GENDER_FEMALE("gender_female"),
  LINK("link"),
  LINK_CHILD("link_child"),
  LINK_HOVERED("link_hovered"),
  LINK_SELECTED("link_selected");

  private final String name;

  private ColorConfigKey(String name) {
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
  public static ColorConfigKey fromName(String name) {
    for (ColorConfigKey key : values()) {
      if (key.getName().equals(name))
        return key;
    }
    return null;
  }
}