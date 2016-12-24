package net.darmo_creations.gui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import net.darmo_creations.controller.ExtensionFileFilter;
import net.darmo_creations.controller.MainController;
import net.darmo_creations.gui.dialog.CardDialog;
import net.darmo_creations.gui.dialog.LinkDialog;
import net.darmo_creations.model.Family;
import net.darmo_creations.model.FamilyMember;
import net.darmo_creations.model.Wedding;

public class MainFrame extends JFrame {
  private static final long serialVersionUID = 2426665404072947885L;

  private JMenu editMenu, displayMenu;
  private JMenuItem saveItem, saveAsItem, addCardItem, addLinkItem, modifyItem, deleteItem;

  private JFileChooser fileChooser;
  private CardDialog cardDialog;
  private LinkDialog linkDialog;

  public MainFrame() {
    MainController controller = new MainController(this);

    setTitle("Généalogie");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setJMenuBar(initJMenuBar(controller));
    addWindowListener(controller);

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileFilter(new ExtensionFileFilter("Arbre généalogique", "tree"));
    this.cardDialog = new CardDialog(this);
    this.linkDialog = new LinkDialog(this);

    controller.init();

    pack();
    setLocationRelativeTo(null);
    setExtendedState(MAXIMIZED_BOTH);
  }

  private JMenuBar initJMenuBar(ActionListener listener) {
    JMenuBar menuBar = new JMenuBar();
    JMenuItem i;

    // Menu 'Fichier'
    {
      JMenu fileMenu = new JMenu("Fichier");
      fileMenu.setMnemonic('f');
      fileMenu.add(i = new JMenuItem("Nouvel arbre"));
      i.setActionCommand("new");
      i.setMnemonic('n');
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listener);
      fileMenu.add(i = new JMenuItem("Ouvrir..."));
      i.setActionCommand("open");
      i.setMnemonic('n');
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listener);
      fileMenu.add(this.saveItem = new JMenuItem("Enregistrer"));
      this.saveItem.setActionCommand("save");
      this.saveItem.setMnemonic('e');
      this.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
      this.saveItem.addActionListener(listener);
      fileMenu.add(this.saveAsItem = new JMenuItem("Enregistrer sous..."));
      this.saveAsItem.setActionCommand("save-as");
      this.saveAsItem.setMnemonic('s');
      this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
      this.saveAsItem.addActionListener(listener);
      fileMenu.add(i = new JMenuItem("Quitter"));
      i.setActionCommand("exit");
      i.setMnemonic('Q');
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
      i.addActionListener(listener);
      menuBar.add(fileMenu);
    }

    // Menu 'Édition'
    {
      this.editMenu = new JMenu("Édition");
      this.editMenu.setMnemonic('é');
      this.editMenu.add(this.addCardItem = new JMenuItem("Ajouter une fiche..."));
      this.addCardItem.setActionCommand("add-card");
      this.addCardItem.setMnemonic('a');
      this.addCardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
      this.addCardItem.addActionListener(listener);
      this.editMenu.add(this.addLinkItem = new JMenuItem("Ajouter un lien..."));
      this.addLinkItem.setActionCommand("add-link");
      this.addLinkItem.setMnemonic('l');
      this.addLinkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
      this.addLinkItem.addActionListener(listener);
      this.editMenu.add(this.modifyItem = new JMenuItem());
      this.modifyItem.setActionCommand("update");
      this.modifyItem.setMnemonic('m');
      this.modifyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK));
      this.modifyItem.addActionListener(listener);
      this.editMenu.add(this.deleteItem = new JMenuItem());
      this.deleteItem.setActionCommand("delete");
      this.deleteItem.setMnemonic('s');
      this.deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
      this.deleteItem.addActionListener(listener);
      menuBar.add(this.editMenu);
    }

    // Menu 'Affichage'
    {
      this.displayMenu = new JMenu("Affichage");
      this.displayMenu.setMnemonic('a');
      ButtonGroup g = new ButtonGroup();
      this.displayMenu.add(i = new JRadioButtonMenuItem("Affichage détaillé", true));
      i.setActionCommand("display-detailled");
      i.setMnemonic('d');
      i.addActionListener(listener);
      g.add(i);
      this.displayMenu.add(i = new JRadioButtonMenuItem("Affichage simplifié"));
      i.setActionCommand("display-simplified");
      i.setMnemonic('s');
      i.addActionListener(listener);
      g.add(i);
      menuBar.add(this.displayMenu);
    }

    // Menu 'Aide'
    {
      JMenu helpMenu = new JMenu("Aide");
      helpMenu.setMnemonic('i');
      helpMenu.add(i = new JMenuItem("À propos..."));
      i.setActionCommand("about");
      i.setMnemonic('à');
      i.addActionListener(listener);
      menuBar.add(helpMenu);
    }

    return menuBar;
  }

  public void updateMenus(boolean fileOpen, boolean cardSelected, boolean linkSelected) {
    this.saveItem.setEnabled(fileOpen);
    this.saveAsItem.setEnabled(fileOpen);
    // TEMP
    this.editMenu.setEnabled(true || fileOpen);
    this.addCardItem.setEnabled(true || fileOpen);
    this.addLinkItem.setEnabled(true || fileOpen);
    if (fileOpen) {
      if (cardSelected && linkSelected)
        throw new IllegalStateException("can't select a card and a link at the same time");
      if (cardSelected) {
        this.modifyItem.setText("Modifier la fiche...");
        this.deleteItem.setText("Supprimer la fiche");
      }
      else if (linkSelected) {
        this.modifyItem.setText("Modifier le lien...");
        this.deleteItem.setText("Supprimer le lien");
      }
      this.modifyItem.setEnabled(cardSelected || linkSelected);
      this.deleteItem.setEnabled(cardSelected || linkSelected);
    }
    this.displayMenu.setEnabled(fileOpen);
  }

  public void displayFamily(Family family) {
    // TODO
  }

  public File showSaveFileChooser() {
    this.fileChooser.showSaveDialog(this);
    return this.fileChooser.getSelectedFile();
  }

  public File showOpenFileChooser() {
    this.fileChooser.showOpenDialog(this);
    return this.fileChooser.getSelectedFile();
  }

  public Optional<FamilyMember> showAddCardDialog(List<FamilyMember> men, List<FamilyMember> women) {
    this.cardDialog.setCard(null, men, women);
    this.cardDialog.setVisible(true);
    return this.cardDialog.getCard();
  }

  public Optional<Wedding> showAddLinkDialog(List<FamilyMember> men, List<FamilyMember> women, List<FamilyMember> children) {
    this.linkDialog.setLink(null, men, women, children);
    this.linkDialog.setVisible(true);
    return this.linkDialog.getLink();
  }

  public Optional<FamilyMember> showUpdateCard(FamilyMember member, List<FamilyMember> men, List<FamilyMember> women) {
    this.cardDialog.setCard(member, men, women);
    this.cardDialog.setVisible(true);
    return this.cardDialog.getCard();
  }

  public Optional<Wedding> showUpdateLink(Wedding wedding, List<FamilyMember> men, List<FamilyMember> women, List<FamilyMember> children) {
    this.linkDialog.setLink(wedding, men, women, children);
    this.linkDialog.setVisible(true);
    return this.linkDialog.getLink();
  }

  public void showAboutDialog() {
    // TODO
  }

  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
  }

  public int showConfirmDialog(String message) {
    return JOptionPane.showConfirmDialog(this, message, "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
  }
}
