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
package net.darmo_creations.jenealogio;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.utils.version.Version;

public class Jenealogio implements Application {
  public static final Version CURRENT_VERSION = new Version(1, 6, 0, true);
  /** Version 1.3d */
  public static final Version V1_3D = new Version(1, 3, 0, true);

  /** Extension for tree files */
  public static final String TREE_FILE_EXT = "gtree";
  /** Image file extensions a tree can be exported into */
  public static final String[] IMAGE_FILES_EXTS = {"bmp", "png", "jpg", "jpeg", "gif"};

  @Override
  public void preInit() {
    List<Language> l = new ArrayList<>();
    l.add(new Language("English", Locale.US));
    l.add(new Language("Français", Locale.FRANCE));
    l.add(new Language("Esperanto", new Locale("eo")));

    ApplicationRegistry.setLanguages(l);

    WritableConfig.registerTag(ConfigTags.GRID_ENABLED, true);
    WritableConfig.registerTag(ConfigTags.GENDER_UNKNOWN_COLOR, Color.GRAY);
    WritableConfig.registerTag(ConfigTags.GENDER_MALE_COLOR, new Color(117, 191, 255));
    WritableConfig.registerTag(ConfigTags.GENDER_FEMALE_COLOR, new Color(37, 177, 19));
    WritableConfig.registerTag(ConfigTags.LINK_COLOR, Color.GRAY);
    WritableConfig.registerTag(ConfigTags.LINK_CHILD_COLOR, new Color(70, 128, 255));
    WritableConfig.registerTag(ConfigTags.LINK_ADOPTED_CHILD_COLOR, Color.CYAN.darker());
    WritableConfig.registerTag(ConfigTags.ZONE_SELECTION_BORDER_COLOR, new Color(0, 120, 215, 128));
    WritableConfig.registerTag(ConfigTags.ZONE_SELECTION_BACKGROUND_COLOR, new Color(185, 213, 241, 128));
  }

  @Override
  public MainFrame initFrame(WritableConfig config) {
    return new MainFrame(config);
  }

  @Override
  public String getName() {
    return "Jenealogio";
  }

  @Override
  public Version getCurrentVersion() {
    return CURRENT_VERSION;
  }

  @Override
  public InputStream getLanguageFilesStream(Language language) {
    return Jenealogio.class.getResourceAsStream("/assets/langs/" + language.getCode() + ".lang");
  }

  @Override
  public boolean checkUpdates() {
    return true;
  }

  @Override
  public Optional<String> getRssUpdatesLink() {
    return Optional.of("https://github.com/Darmo117/Jenealogio/releases.atom");
  }

  @Override
  public boolean hasHelpDocumentation() {
    return true;
  }

  @Override
  public Optional<String> getHelpDocumentationLink(Language language) {
    return Optional.of("http://darmo-creations.net/products/jenealogio/help-doc/" + language.getCode() + "/");
  }

  @Override
  public boolean hasAboutDialog() {
    return true;
  }

  @Override
  public Optional<String> getAboutFilePath() {
    return Optional.of("/assets/about.html");
  }

  @Override
  public Optional<String> getIcon() {
    return Optional.of("/assets/icons/jenealogio_icon.png");
  }

  @Override
  public Optional<String> getLicenseIcon() {
    return Optional.of("/assets/icons/gplv3-127x51.png");
  }
}
