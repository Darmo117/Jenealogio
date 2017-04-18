package net.darmo_creations.config;

import java.awt.Color;

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

  @Override
  public Class<Color> getValueClass() {
    return Color.class;
  }

  public static ColorConfigKey fromName(String name) {
    for (ColorConfigKey key : values()) {
      if (key.getName().equals(name))
        return key;
    }
    return null;
  }
}