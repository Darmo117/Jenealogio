package net.darmo_creations.jenealogio;

import java.awt.Color;
import java.io.InputStream;
import java.util.Optional;

import net.darmo_creations.gui_framework.Application;
import net.darmo_creations.gui_framework.config.Language;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.utils.version.Version;

public class Jenealogio implements Application {
  public static final Version CURRENT_VERSION = new Version(1, 5, 0, false);
  /** Version 1.3d */
  public static final Version V1_3D = new Version(1, 3, 0, true);

  /** Extension for tree files */
  public static final String TREE_FILE_EXT = "gtree";
  /** Image file extensions a tree can be exported into */
  public static final String[] IMAGE_FILES_EXTS = {"bmp", "png", "jpg", "jpeg", "gif"};

  @Override
  public void preInit() {
    WritableConfig.registerTag(ConfigTags.CARD_BORDER_COLOR, Color.GRAY);
    WritableConfig.registerTag(ConfigTags.CARD_SELECTED_BORDER_COLOR, Color.BLUE);
    WritableConfig.registerTag(ConfigTags.CARD_SELECTED_BACKGROUND_BORDER_COLOR, Color.BLACK);
    WritableConfig.registerTag(ConfigTags.GENDER_UNKNOWN_COLOR, Color.GRAY);
    WritableConfig.registerTag(ConfigTags.GENDER_MALE_COLOR, new Color(117, 191, 255));
    WritableConfig.registerTag(ConfigTags.GENDER_FEMALE_COLOR, new Color(37, 177, 19));
    WritableConfig.registerTag(ConfigTags.LINK_COLOR, Color.BLACK);
    WritableConfig.registerTag(ConfigTags.LINK_CHILD_COLOR, new Color(70, 128, 255));
    WritableConfig.registerTag(ConfigTags.LINK_ADOPTED_CHILD_COLOR, Color.CYAN.darker());
    WritableConfig.registerTag(ConfigTags.LINK_HOVERED_COLOR, Color.RED);
    WritableConfig.registerTag(ConfigTags.LINK_SELECTED_COLOR, Color.GREEN);
    WritableConfig.registerTag(ConfigTags.SELECTION_BORDER_COLOR, new Color(0, 120, 215, 128));
    WritableConfig.registerTag(ConfigTags.SELECTION_BACKGROUND_COLOR, new Color(185, 213, 241, 128));
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
  public String getIconsLocation() {
    return "/assets/icons/";
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
