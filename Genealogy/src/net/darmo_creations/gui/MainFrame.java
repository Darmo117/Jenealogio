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
package net.darmo_creations.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.border.MatteBorder;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.config.Language;
import net.darmo_creations.controllers.ExtensionFileFilter;
import net.darmo_creations.controllers.MainController;
import net.darmo_creations.gui.components.DisplayPanel;
import net.darmo_creations.gui.dialog.AboutDialog;
import net.darmo_creations.gui.dialog.CardDetailsDialog;
import net.darmo_creations.gui.dialog.LinkDetailsDialog;
import net.darmo_creations.gui.dialog.card.CardDialog;
import net.darmo_creations.gui.dialog.help.HelpDialog;
import net.darmo_creations.gui.dialog.link.LinkDialog;
import net.darmo_creations.gui.dialog.options.EditColorsDialog;
import net.darmo_creations.gui.dialog.tree_creation.TreeCreationDialog;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Relationship;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;
import net.darmo_creations.util.Version;

/**
 * The main frame of the application.
 *
 * @author Damien Vergnet
 */
public class MainFrame extends JFrame {
  private static final long serialVersionUID = 2426665404072947885L;

  public static final String BASE_TITLE = "Jenealogio " + Version.CURRENT_VERSION;

  private JFileChooser fileChooser;
  private TreeCreationDialog treeCreationDialog;
  private CardDialog cardDialog;
  private CardDetailsDialog cardDetailsDialog;
  private LinkDialog linkDialog;
  private LinkDetailsDialog linkDetailsDialog;
  private EditColorsDialog editColorsDialog;
  private HelpDialog helpDialog;
  private AboutDialog aboutDialog;

  private JMenu editMenu;
  private JMenuItem saveItem, saveAsItem, addCardItem, addLinkItem, editItem, deleteItem;
  private JButton saveBtn, saveAsBtn, addCardBtn, editCardBtn, editLinkBtn, deleteCardBtn, deleteLinkBtn;
  private JToggleButton addLinkBtn;
  private DisplayPanel displayPnl;

  public MainFrame(GlobalConfig config) {
    MainController controller = new MainController(this, config);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(800, 600));
    setIconImage(Images.JENEALOGIO.getImage());

    addWindowListener(controller);

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileFilter(new ExtensionFileFilter(I18n.getLocalizedString("file_type.tree.desc"), "gtree"));
    this.treeCreationDialog = new TreeCreationDialog(this);
    this.cardDialog = new CardDialog(this);
    this.cardDetailsDialog = new CardDetailsDialog(this);
    this.cardDetailsDialog.addObserver(controller);
    this.linkDialog = new LinkDialog(this);
    this.linkDetailsDialog = new LinkDetailsDialog(this);
    this.linkDetailsDialog.addObserver(controller);
    this.editColorsDialog = new EditColorsDialog(this);
    this.helpDialog = new HelpDialog(this, config);
    this.aboutDialog = new AboutDialog(this);

    setJMenuBar(initJMenuBar(controller, config));

    add(getJToolBar(controller), BorderLayout.NORTH);
    this.displayPnl = new DisplayPanel();
    this.displayPnl.addObserver(controller);
    this.displayPnl.addDropHandler(controller);
    add(new JScrollPane(this.displayPnl), BorderLayout.CENTER);

    controller.init();

    pack();
    setLocationRelativeTo(null);
    setExtendedState(MAXIMIZED_BOTH);
  }

  /**
   * Initializes the menu bar.
   * 
   * @param listener the action listener
   * @return the menu bar
   */
  private JMenuBar initJMenuBar(ActionListener listener, GlobalConfig config) {
    JMenuBar menuBar = new JMenuBar();
    JMenuItem i;

    // 'Files' menu
    {
      JMenu fileMenu = new JMenu(I18n.getLocalizedString("menu.file.text"));
      fileMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.file"));
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.new_tree.text")));
      i.setIcon(Images.NEW_TREE);
      i.setActionCommand("new");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.new_tree"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listener);
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.open.text")));
      i.setIcon(Images.OPEN);
      i.setActionCommand("open");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.open"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listener);
      fileMenu.add(this.saveItem = new JMenuItem(I18n.getLocalizedString("item.save.text")));
      this.saveItem.setIcon(Images.SAVE);
      this.saveItem.setActionCommand("save");
      this.saveItem.setMnemonic(I18n.getLocalizedMnemonic("item.save"));
      this.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
      this.saveItem.addActionListener(listener);
      fileMenu.add(this.saveAsItem = new JMenuItem(I18n.getLocalizedString("item.save_as.text")));
      this.saveAsItem.setIcon(Images.SAVE_AS);
      this.saveAsItem.setActionCommand("save-as");
      this.saveAsItem.setMnemonic(I18n.getLocalizedMnemonic("item.save_as"));
      this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
      this.saveAsItem.addActionListener(listener);
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.exit.text")));
      i.setIcon(Images.EXIT);
      i.setActionCommand("exit");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.exit"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
      i.addActionListener(listener);
      menuBar.add(fileMenu);
    }

    // 'Edit' menu
    {
      this.editMenu = new JMenu(I18n.getLocalizedString("menu.edit.text"));
      this.editMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.edit"));
      this.editMenu.add(this.addCardItem = new JMenuItem(I18n.getLocalizedString("item.add_card.text")));
      this.addCardItem.setIcon(Images.ADD_CARD);
      this.addCardItem.setActionCommand("add-card");
      this.addCardItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_card"));
      this.addCardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
      this.addCardItem.addActionListener(listener);
      this.editMenu.add(this.addLinkItem = new JMenuItem(I18n.getLocalizedString("item.add_link.text")));
      this.addLinkItem.setIcon(Images.ADD_LINK);
      this.addLinkItem.setActionCommand("add-link");
      this.addLinkItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_link"));
      this.addLinkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
      this.addLinkItem.addActionListener(listener);
      this.editMenu.add(this.editItem = new JMenuItem());
      this.editItem.setActionCommand("edit");
      this.editItem.setMnemonic(I18n.getLocalizedMnemonic("item.edit"));
      this.editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
      this.editItem.addActionListener(listener);
      this.editMenu.add(this.deleteItem = new JMenuItem());
      this.deleteItem.setActionCommand("delete");
      this.deleteItem.setMnemonic(I18n.getLocalizedMnemonic("item.delete"));
      this.deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
      this.deleteItem.addActionListener(listener);
      menuBar.add(this.editMenu);
    }

    // 'Options' menu
    {
      JMenu optionsMenu = new JMenu(I18n.getLocalizedString("menu.options.text"));
      optionsMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.options"));
      optionsMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.colors.text")));
      i.setIcon(Images.COLOR_WHEEL);
      i.setActionCommand("edit_colors");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.colors"));
      i.addActionListener(listener);
      JMenu langMenu = new JMenu(I18n.getLocalizedString("menu.lang.text"));
      langMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.lang"));
      optionsMenu.add(langMenu);
      ButtonGroup bg = new ButtonGroup();
      for (Language l : Language.values()) {
        langMenu.add(i = new JRadioButtonMenuItem(l.getName()));
        i.setSelected(l == config.getLanguage());
        i.setIcon(Images.getCountryFlag(l));
        i.setActionCommand("lang-" + l.getCode());
        i.addActionListener(listener);
        bg.add(i);
      }
      menuBar.add(optionsMenu);
    }

    // 'Help' menu
    {
      JMenu helpMenu = new JMenu(I18n.getLocalizedString("menu.help.text"));
      helpMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.help"));
      helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.help.text")));
      i.setIcon(Images.HELP);
      i.setActionCommand("help");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.help"));
      i.addActionListener(listener);
      helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.about.text")));
      i.setActionCommand("about");
      i.setMnemonic(I18n.getLocalizedMnemonic("item.about"));
      i.addActionListener(listener);
      menuBar.add(helpMenu);
    }

    return menuBar;
  }

  /**
   * Initializes the tool bar.
   * 
   * @param listener the action listener
   * @return the tool bar
   */
  private JToolBar getJToolBar(ActionListener listener) {
    JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
    toolBar.setFloatable(false);
    toolBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
    JButton b;

    toolBar.add(b = new JButton(Images.NEW_TREE_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.new_tree.text") + " (Ctrl+N)");
    b.setFocusable(false);
    b.setActionCommand("new");
    b.addActionListener(listener);
    toolBar.add(b = new JButton(Images.OPEN_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.open.text") + " (Ctrl+O)");
    b.setFocusable(false);
    b.setActionCommand("open");
    b.addActionListener(listener);
    toolBar.add(this.saveBtn = new JButton(Images.SAVE_BIG));
    this.saveBtn.setToolTipText(I18n.getLocalizedString("item.save.text") + " (Ctrl+S)");
    this.saveBtn.setFocusable(false);
    this.saveBtn.setActionCommand("save");
    this.saveBtn.addActionListener(listener);
    toolBar.add(this.saveAsBtn = new JButton(Images.SAVE_AS_BIG));
    this.saveAsBtn.setToolTipText(I18n.getLocalizedString("item.save_as.text") + " (Ctrl+Maj+S)");
    this.saveAsBtn.setFocusable(false);
    this.saveAsBtn.setActionCommand("save-as");
    this.saveAsBtn.addActionListener(listener);
    toolBar.add(this.addCardBtn = new JButton(Images.ADD_CARD_BIG));
    this.addCardBtn.setToolTipText(I18n.getLocalizedString("item.add_card.text") + " (Ctrl+A)");
    this.addCardBtn.setFocusable(false);
    this.addCardBtn.setActionCommand("add-card");
    this.addCardBtn.addActionListener(listener);
    toolBar.add(this.editCardBtn = new JButton(Images.EDIT_CARD_BIG));
    this.editCardBtn.setToolTipText(I18n.getLocalizedString("item.edit_card.text") + " (Ctrl+E)");
    this.editCardBtn.setFocusable(false);
    this.editCardBtn.setActionCommand("edit");
    this.editCardBtn.addActionListener(listener);
    toolBar.add(this.deleteCardBtn = new JButton(Images.DELETE_CARD_BIG));
    this.deleteCardBtn.setToolTipText(I18n.getLocalizedString("item.delete_card.text") + " (Supprimer)");
    this.deleteCardBtn.setFocusable(false);
    this.deleteCardBtn.setActionCommand("delete");
    this.deleteCardBtn.addActionListener(listener);
    toolBar.add(this.addLinkBtn = new JToggleButton(Images.ADD_LINK_BIG));
    this.addLinkBtn.setToolTipText(I18n.getLocalizedString("item.add_link.text") + " (Ctrl+L)");
    this.addLinkBtn.setFocusable(false);
    this.addLinkBtn.setActionCommand("add-link");
    this.addLinkBtn.addActionListener(listener);
    toolBar.add(this.editLinkBtn = new JButton(Images.EDIT_LINK_BIG));
    this.editLinkBtn.setToolTipText(I18n.getLocalizedString("item.edit_link.text") + " (Ctrl+E)");
    this.editLinkBtn.setFocusable(false);
    this.editLinkBtn.setActionCommand("edit");
    this.editLinkBtn.addActionListener(listener);
    toolBar.add(this.deleteLinkBtn = new JButton(Images.DELETE_LINK_BIG));
    this.deleteLinkBtn.setToolTipText(I18n.getLocalizedString("item.delete_link.text") + " (Supprimer)");
    this.deleteLinkBtn.setFocusable(false);
    this.deleteLinkBtn.setActionCommand("delete");
    this.deleteLinkBtn.addActionListener(listener);

    return toolBar;
  }

  /**
   * Updates the menu bar. The last two arguments cannot be true at the same time or an
   * IllegalStateException will be thrown.
   * 
   * @param fileOpen is a file open?
   * @param cardSelected is a card selected?
   * @param linkSelected is a link selected?
   */
  public void updateMenus(boolean fileOpen, boolean cardSelected, boolean linkSelected) {
    this.saveAsItem.setEnabled(fileOpen);
    this.editMenu.setEnabled(fileOpen);
    this.addCardItem.setEnabled(fileOpen);
    this.addLinkItem.setEnabled(fileOpen);
    this.editItem.setEnabled(fileOpen && (cardSelected || linkSelected));
    this.deleteItem.setEnabled(fileOpen && (cardSelected || linkSelected));

    this.saveAsBtn.setEnabled(fileOpen);
    this.addCardBtn.setEnabled(fileOpen);
    this.addLinkBtn.setEnabled(fileOpen);
    this.editCardBtn.setEnabled(fileOpen && cardSelected);
    this.deleteCardBtn.setEnabled(fileOpen && cardSelected);
    this.editLinkBtn.setEnabled(fileOpen && linkSelected);
    this.deleteLinkBtn.setEnabled(fileOpen && linkSelected);

    if (fileOpen) {
      if (cardSelected && linkSelected)
        throw new IllegalStateException("can't select a card and a link at the same time");
      if (cardSelected) {
        this.editItem.setText(I18n.getLocalizedString("item.edit_card.text"));
        this.editItem.setIcon(Images.EDIT_CARD);
        this.deleteItem.setText(I18n.getLocalizedString("item.delete_card.text"));
        this.deleteItem.setIcon(Images.DELETE_CARD);
      }
      else if (linkSelected) {
        this.editItem.setText(I18n.getLocalizedString("item.edit_link.text"));
        this.editItem.setIcon(Images.EDIT_LINK);
        this.deleteItem.setText(I18n.getLocalizedString("item.delete_link.text"));
        this.deleteItem.setIcon(Images.DELETE_LINK);
      }
      else {
        this.editItem.setText(I18n.getLocalizedString("item.edit.text"));
        this.editItem.setIcon(null);
        this.deleteItem.setText(I18n.getLocalizedString("item.delete.text"));
        this.deleteItem.setIcon(null);
      }
    }
  }

  /**
   * Selects the given panel. All observers will be notified of the event. Giving -1 will deselect
   * all panels.
   * 
   * @param id panel's ID (member's ID)
   */
  public void selectPanel(long id) {
    this.displayPnl.selectPanel(id);
  }

  /**
   * Updates the save buttons.
   * 
   * @param saved is the file saved?
   */
  public void updateSaveMenus(boolean saved) {
    this.saveItem.setEnabled(!saved);
    this.saveBtn.setEnabled(!saved);
  }

  /**
   * Sets the selection state of the "add link" button.
   * 
   * @param selected
   */
  public void setAddLinkButtonSelected(boolean selected) {
    this.addLinkBtn.setSelected(selected);
  }

  /**
   * Resets the tree display.
   */
  public void resetDisplay() {
    this.displayPnl.reset();
  }

  /**
   * Refreshes the tree display.
   * 
   * @param family the tree
   */
  public void refreshDisplay(Family family, GlobalConfig config) {
    this.displayPnl.refresh(family, config);
  }

  /**
   * Refreshes the tree display using the given cards positions.
   * 
   * @param family the tree
   * @param positions positions for all cards
   */
  public void refreshDisplay(Family family, Map<Long, Point> positions, GlobalConfig config) {
    this.displayPnl.refresh(family, positions, config);
  }

  /**
   * @return the positions of all cards
   */
  public Map<Long, Point> getCardsPositions() {
    return this.displayPnl.getCardsPositions();
  }

  /**
   * @return the display's current middle coordinate
   */
  public Point getDisplayMiddlePoint() {
    JViewport viewport = (JViewport) this.displayPnl.getParent();
    Point pos = viewport.getViewPosition();
    Dimension size = viewport.getExtentSize();
    int x = pos.x + size.width / 2;
    int y = pos.y + size.height / 2;

    return new Point(x, y);
  }

  /**
   * Shows the "save" file chooser.
   * 
   * @return the selected file
   */
  public Optional<File> showSaveFileChooser() {
    int choice = this.fileChooser.showSaveDialog(this);
    return Optional.ofNullable(choice != JFileChooser.APPROVE_OPTION ? null : this.fileChooser.getSelectedFile());
  }

  /**
   * Shows the "open" file chooser.
   * 
   * @return the selected file
   */
  public Optional<File> showOpenFileChooser() {
    int choice = this.fileChooser.showOpenDialog(this);
    return Optional.ofNullable(choice != JFileChooser.APPROVE_OPTION ? null : this.fileChooser.getSelectedFile());
  }

  /**
   * Shows the tree creation dialog.
   * 
   * @return the tree name or nothing if the dialog was dismissed/canceled
   */
  public Optional<String> showCreateTreeDialog() {
    this.treeCreationDialog.setCanceled(false);
    this.treeCreationDialog.setVisible(true);
    return this.treeCreationDialog.getTreeName();
  }

  /**
   * Shows the "add card" dialog.
   * 
   * @return the new card or nothing if the dialog was dismissed/canceled
   */
  public Optional<FamilyMember> showAddCardDialog() {
    this.cardDialog.setCard(null);
    this.cardDialog.setVisible(true);
    return this.cardDialog.getCard();
  }

  /**
   * Shows the "add link" dialog.
   * 
   * @param partner1 one partner
   * @param partner2 the other partner
   * @param family the family
   * @return the new link or nothing if the dialog was dismissed/canceled
   */
  public Optional<Relationship> showAddLinkDialog(long partner1, long partner2, Family family) {
    this.linkDialog.addLink(partner1, partner2, family);
    this.linkDialog.setVisible(true);
    return this.linkDialog.getLink();
  }

  /**
   * Shows the "update card" dialog.
   * 
   * @param member the member to update
   * @return the updated card or nothing if the dialog was dismissed/canceled
   */
  public Optional<FamilyMember> showUpdateCardDialog(FamilyMember member) {
    this.cardDialog.setCard(member);
    this.cardDialog.setVisible(true);
    return this.cardDialog.getCard();
  }

  /**
   * Shows the "update link" dialog.
   * 
   * @param relation the link to update
   * @param family the family
   * @return the updated link or nothing if the dialog was dismissed/canceled
   */
  public Optional<Relationship> showUpdateLinkDialog(Relationship relation, Family family) {
    this.linkDialog.updateLink(relation, family);
    this.linkDialog.setVisible(true);
    return this.linkDialog.getLink();
  }

  /**
   * Shows the "card details" dialog.
   * 
   * @param member the member to display
   * @param relations the relations it is part of
   */
  public void showDetailsDialog(FamilyMember member, Set<Relationship> relations) {
    this.cardDetailsDialog.setInfo(member, relations);
    this.cardDetailsDialog.setVisible(true);
  }

  /**
   * Shows the "link details" dialog.
   * 
   * @param member the member to display
   * @param relations the relations it is part of
   */
  public void showDetailsDialog(Relationship relation, Family family) {
    this.linkDetailsDialog.setInfo(relation, family);
    this.linkDetailsDialog.setVisible(true);
  }

  /**
   * Shows the "edit colors" dialog.
   * 
   * @param config the current config
   * @return the new config or nothing if the dialog was dismissed/canceled
   */
  public Optional<GlobalConfig> showEditColorsDialog(GlobalConfig config) {
    this.editColorsDialog.setConfig(config.clone());
    this.editColorsDialog.setVisible(true);
    return this.editColorsDialog.getConfig();
  }

  /**
   * Shows the "help" dialog.
   */
  public void showHelpDialog() {
    this.helpDialog.setVisible(true);
  }

  /**
   * Shows the "about" dialog.
   */
  public void showAboutDialog() {
    this.aboutDialog.setVisible(true);
  }

  /**
   * Shows an error message.
   * 
   * @param message the message
   */
  public void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(this, message, I18n.getLocalizedString("popup.error.title"), JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Shows a confirm dialog.
   * 
   * @param message the message
   * @return an integer indicating the option selected by the user (either YES_OPTION, NO_OPTION,
   *         CANCEL_OPTION or CLOSED_OPTION)
   */
  public int showConfirmDialog(String message) {
    return JOptionPane.showConfirmDialog(this, message, I18n.getLocalizedString("popup.confirm.title"), JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
  }

  /**
   * Shows a warning message.
   * 
   * @param message the message
   */
  public void showWarningDialog(String message) {
    JOptionPane.showMessageDialog(this, message, I18n.getLocalizedString("popup.warning.title"), JOptionPane.WARNING_MESSAGE);
  }
}
