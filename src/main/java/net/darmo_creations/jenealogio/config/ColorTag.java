package net.darmo_creations.jenealogio.config;

import java.awt.Color;

import net.darmo_creations.gui_framework.config.tags.AbstractTag;

public class ColorTag extends AbstractTag<Color> {
  public ColorTag(String name) {
    super(name, Color.class);
  }

  @Override
  protected String serializeValueGeneric(Color value) {
    return "" + value.getRGB();
  }

  @Override
  public Color deserializeValue(String value) {
    return new Color(Integer.parseInt(value));
  }
}
