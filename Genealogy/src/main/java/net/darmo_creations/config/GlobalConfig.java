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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class contains the global configuration for the app.
 *
 * @author Damien Vergnet
 */
public class GlobalConfig implements Cloneable {
  private Language language;
  private Map<ConfigKey<?>, Object> map;

  /**
   * Creates a config with default values for all properties.
   */
  public GlobalConfig() {
    setLanguage(Language.ENGLISH);
    this.map = new HashMap<>();
    setValue(ColorConfigKey.CARD_BORDER, Color.GRAY);
    setValue(ColorConfigKey.CARD_SELECTED_BORDER, Color.BLUE);
    setValue(ColorConfigKey.CARD_SELECTED_BACKGROUND_BORDER, Color.BLACK);
    setValue(ColorConfigKey.GENDER_UNKNOWN, Color.GRAY);
    setValue(ColorConfigKey.GENDER_MALE, new Color(117, 191, 255));
    setValue(ColorConfigKey.GENDER_FEMALE, new Color(37, 177, 19));
    setValue(ColorConfigKey.LINK, Color.BLACK);
    setValue(ColorConfigKey.LINK_CHILD, Color.BLUE);
    setValue(ColorConfigKey.LINK_HOVERED, Color.RED);
    setValue(ColorConfigKey.LINK_SELECTED, Color.GREEN);
    setValue(ColorConfigKey.SELECTION_BORDER, new Color(0, 120, 215, 128));
    setValue(ColorConfigKey.SELECTION_BACKGROUND, new Color(185, 213, 241, 128));
  }

  /**
   * @return the current language
   */
  public Language getLanguage() {
    return this.language;
  }

  /**
   * Sets the language. Cannot be null.
   * 
   * @param language the new language
   */
  public void setLanguage(Language language) {
    this.language = Objects.requireNonNull(language);
  }

  /**
   * Returns the value for the given key. If the key is null or no value was found, null is
   * returned.
   * 
   * @param key the key
   * @return the value or null
   */
  @SuppressWarnings("unchecked")
  public <T> T getValue(ConfigKey<T> key) {
    return key == null ? null : (T) this.map.get(key);
  }

  /**
   * Sets the value for the given key.
   * 
   * @param key the key
   * @param value the associated value
   */
  public <T> void setValue(ConfigKey<T> key, T value) {
    this.map.put(key, value);
  }

  /**
   * Makes a clone of this config. It should be noted that the new options map is only a shallow
   * copy of the current one.
   */
  @Override
  public GlobalConfig clone() {
    try {
      GlobalConfig config = (GlobalConfig) super.clone();
      config.map = new HashMap<>(this.map);
      return config;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
