package net.darmo_creations;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.darmo_creations.gui.MainFrame;

public class Start {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(null, "Impossible de charger le rendu système, utilisation du rendu par défaut.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    new MainFrame().setVisible(true);
  }
}
