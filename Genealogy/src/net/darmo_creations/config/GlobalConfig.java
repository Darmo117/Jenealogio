package net.darmo_creations.config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class GlobalConfig implements Cloneable {
  private Language language;
  private Map<ConfigKey<?>, Object> map;

  public GlobalConfig() {
    setLanguage(Language.ENGLISH);
    this.map = new HashMap<>();
    setValue(ColorConfigKey.CARD_BORDER, Color.GRAY);
    setValue(ColorConfigKey.CARD_SELECTED_BORDER, Color.BLUE);
    setValue(ColorConfigKey.GENDER_UNKNOWN, Color.GRAY);
    setValue(ColorConfigKey.GENDER_MALE, new Color(117, 191, 255));
    setValue(ColorConfigKey.GENDER_FEMALE, new Color(37, 177, 19));
    setValue(ColorConfigKey.LINK, Color.BLACK);
    setValue(ColorConfigKey.LINK_CHILD, Color.BLUE);
    setValue(ColorConfigKey.LINK_HOVERED, Color.RED);
    setValue(ColorConfigKey.LINK_SELECTED, Color.GREEN);
  }

  public Language getLanguage() {
    return this.language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  @SuppressWarnings("unchecked")
  public <T> T getValue(ConfigKey<T> key) {
    return key == null ? null : (T) this.map.get(key);
  }

  public <T> void setValue(ConfigKey<T> key, T value) {
    this.map.put(key, value);
  }

  @Override
  public GlobalConfig clone() {
    try {
      return (GlobalConfig) super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
