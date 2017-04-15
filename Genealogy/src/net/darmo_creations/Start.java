package net.darmo_creations;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.util.I18n;

public class Start {
  public static void main(String[] args) {
    I18n.init();

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, I18n.getLocalizedString("popup.laf_error.text"), I18n.getLocalizedString("popup.laf_error.title"),
          JOptionPane.ERROR_MESSAGE);
    }
    new MainFrame().setVisible(true);
  }
}
