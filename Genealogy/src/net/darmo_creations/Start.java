package net.darmo_creations;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.dao.ConfigDao;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.util.I18n;

public class Start {
  public static final boolean DEBUG = true;

  public static void main(String[] args) {
    GlobalConfig config = ConfigDao.getInstance().load();
    I18n.init(config.getLanguage().getLocale());

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.laf_error.title"),
          JOptionPane.ERROR_MESSAGE);
    }
    new MainFrame(config).setVisible(true);
  }
}
