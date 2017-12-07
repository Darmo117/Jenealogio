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
package net.darmo_creations.jenealogio.config;

public class ConfigTags {
  public static final ColorTag GENDER_UNKNOWN_COLOR = new ColorTag("gender_unknown_color");
  public static final ColorTag GENDER_MALE_COLOR = new ColorTag("gender_male_color");
  public static final ColorTag GENDER_FEMALE_COLOR = new ColorTag("gender_female_color");
  public static final ColorTag LINK_COLOR = new ColorTag("link_color");
  public static final ColorTag LINK_CHILD_COLOR = new ColorTag("link_child_color");
  public static final ColorTag LINK_ADOPTED_CHILD_COLOR = new ColorTag("link_adopted_child_color");
  public static final ColorTag LINK_HOVERED_COLOR = new ColorTag("link_hovered_color");
  public static final ColorTag LINK_SELECTED_COLOR = new ColorTag("link_selected_color");
  public static final ColorTag LINK_SELECTED_BACKGROUND_COLOR = new ColorTag("link_selected_background_color");
  public static final ColorTag ZONE_SELECTION_BORDER_COLOR = new ColorTag("panel_selection_border_color");
  public static final ColorTag ZONE_SELECTION_BACKGROUND_COLOR = new ColorTag("panel_selection_background_color");

  /** This array contains all color tags */
  public static final ColorTag[] COLORS_TAGS;

  static {
    COLORS_TAGS = new ColorTag[]{GENDER_UNKNOWN_COLOR, GENDER_MALE_COLOR, GENDER_FEMALE_COLOR, LINK_COLOR, LINK_CHILD_COLOR,
      LINK_ADOPTED_CHILD_COLOR, LINK_HOVERED_COLOR, LINK_SELECTED_COLOR, LINK_SELECTED_BACKGROUND_COLOR, ZONE_SELECTION_BORDER_COLOR,
      ZONE_SELECTION_BACKGROUND_COLOR};
  }
}
