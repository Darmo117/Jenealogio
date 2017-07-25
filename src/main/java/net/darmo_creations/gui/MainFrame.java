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
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.event.MouseInputAdapter;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.config.Language;
import net.darmo_creations.controllers.ExtensionFileFilter;
import net.darmo_creations.controllers.MainController;
import net.darmo_creations.events.ChangeLanguageEvent;
import net.darmo_creations.events.EventsDispatcher;
import net.darmo_creations.events.UserEvent;
import net.darmo_creations.gui.components.display_panel.DisplayPanel;
import net.darmo_creations.gui.components.status_bar.StatusBar;
import net.darmo_creations.gui.dialog.AboutDialog;
import net.darmo_creations.gui.dialog.CardDetailsDialog;
import net.darmo_creations.gui.dialog.LinkDetailsDialog;
import net.darmo_creations.gui.dialog.card.CardDialog;
import net.darmo_creations.gui.dialog.link.LinkDialog;
import net.darmo_creations.gui.dialog.options.EditColorsDialog;
import net.darmo_creations.gui.dialog.tree_creation.TreeDialog;
import net.darmo_creations.gui.dialog.update.UpdateDialog;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Relationship;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;
import net.darmo_creations.util.Nullable;
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
  private TreeDialog treeDialog;
  private CardDialog cardDialog;
  private CardDetailsDialog cardDetailsDialog;
  private LinkDialog linkDialog;
  private LinkDetailsDialog linkDetailsDialog;
  private EditColorsDialog editColorsDialog;
  private AboutDialog aboutDialog;
  private UpdateDialog updateDialog;

  private JMenu editMenu;
  private JMenuItem editTreeItem, saveItem, saveAsItem, undoItem, redoItem, addCardItem, addLinkItem, editItem, deleteItem;
  private JCheckBoxMenuItem checkUpdatesItem;
  private JButton saveBtn, saveAsBtn, undoBtn, redoBtn, addCardBtn, editCardBtn, editLinkBtn, deleteCardBtn, deleteLinkBtn;
  private JToggleButton addLinkBtn;
  private DisplayPanel displayPnl;
  private StatusBar statusBar;
  private JLabel updateLbl;

  private Map<UserEvent.Type, ActionListener> listeners;

  public MainFrame(GlobalConfig config) {
    MainController controller = new MainController(this, config);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(800, 600));
    setIconImage(Images.JENEALOGIO.getImage());

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        EventsDispatcher.EVENT_BUS.dispatchEvent(new UserEvent(UserEvent.Type.EXIT));
      }
    });

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileFilter(new ExtensionFileFilter(I18n.getLocalizedString("file_type.tree.desc"), "gtree"));
    this.treeDialog = new TreeDialog(this);
    this.cardDialog = new CardDialog(this);
    this.cardDetailsDialog = new CardDetailsDialog(this);
    this.linkDialog = new LinkDialog(this);
    this.linkDetailsDialog = new LinkDetailsDialog(this);
    this.editColorsDialog = new EditColorsDialog(this);
    this.aboutDialog = new AboutDialog(this);
    this.updateDialog = new UpdateDialog(this);

    this.listeners = new HashMap<>();
    for (UserEvent.Type type : UserEvent.Type.values())
      this.listeners.put(type, e -> EventsDispatcher.EVENT_BUS.dispatchEvent(new UserEvent(type)));

    setJMenuBar(initJMenuBar(this.listeners, config));

    add(getJToolBar(this.listeners), BorderLayout.NORTH);

    JScrollPane scrollPane = new JScrollPane();
    this.displayPnl = new DisplayPanel(scrollPane);
    this.displayPnl.addDropHandler(controller);
    scrollPane.setViewportView(this.displayPnl);
    add(scrollPane, BorderLayout.CENTER);

    this.statusBar = new StatusBar();
    add(this.statusBar, BorderLayout.SOUTH);

    this.updateLbl = new JLabel();
    this.updateLbl.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        EventsDispatcher.EVENT_BUS.dispatchEvent(new UserEvent(UserEvent.Type.OPEN_UPDATE));
      }
    });
    this.statusBar.addRightComponent(this.updateLbl);

    EventsDispatcher.EVENT_BUS.register(controller);
    EventsDispatcher.EVENT_BUS.register(this.displayPnl);

    controller.init();

    pack();
    setLocationRelativeTo(null);
    setExtendedState(MAXIMIZED_BOTH);
  }

  /**
   * Initializes the menu bar.
   * 
   * @param listeners the action listeners
   * @return the menu bar
   */
  private JMenuBar initJMenuBar(Map<UserEvent.Type, ActionListener> listeners, GlobalConfig config) {
    JMenuBar menuBar = new JMenuBar();
    JMenuItem i;

    // 'Files' menu
    {
      JMenu fileMenu = new JMenu(I18n.getLocalizedString("menu.file.text"));
      fileMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.file"));
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.new_tree.text")));
      i.setIcon(Images.NEW_TREE);
      i.setMnemonic(I18n.getLocalizedMnemonic("item.new_tree"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listeners.get(UserEvent.Type.NEW));
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.open.text")));
      i.setIcon(Images.OPEN);
      i.setMnemonic(I18n.getLocalizedMnemonic("item.open"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
      i.addActionListener(listeners.get(UserEvent.Type.OPEN));
      fileMenu.add(this.editTreeItem = new JMenuItem(I18n.getLocalizedString("item.edit_tree.text")));
      this.editTreeItem.setIcon(Images.EDIT_TREE);
      this.editTreeItem.setMnemonic(I18n.getLocalizedMnemonic("item.edit_tree"));
      this.editTreeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
      this.editTreeItem.addActionListener(listeners.get(UserEvent.Type.EDIT_TREE));
      fileMenu.add(this.saveItem = new JMenuItem(I18n.getLocalizedString("item.save.text")));
      this.saveItem.setIcon(Images.SAVE);
      this.saveItem.setMnemonic(I18n.getLocalizedMnemonic("item.save"));
      this.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
      this.saveItem.addActionListener(listeners.get(UserEvent.Type.SAVE));
      fileMenu.add(this.saveAsItem = new JMenuItem(I18n.getLocalizedString("item.save_as.text")));
      this.saveAsItem.setIcon(Images.SAVE_AS);
      this.saveAsItem.setMnemonic(I18n.getLocalizedMnemonic("item.save_as"));
      this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
      this.saveAsItem.addActionListener(listeners.get(UserEvent.Type.SAVE_AS));
      fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.exit.text")));
      i.setIcon(Images.EXIT);
      i.setMnemonic(I18n.getLocalizedMnemonic("item.exit"));
      i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
      i.addActionListener(listeners.get(UserEvent.Type.EXIT));
      menuBar.add(fileMenu);
    }

    // 'Edit' menu
    {
      this.editMenu = new JMenu(I18n.getLocalizedString("menu.edit.text"));
      this.editMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.edit"));
      this.editMenu.add(this.undoItem = new JMenuItem(I18n.getLocalizedString("item.undo.text")));
      this.undoItem.setIcon(Images.UNDO);
      this.undoItem.setMnemonic(I18n.getLocalizedMnemonic("item.undo"));
      this.undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
      this.undoItem.addActionListener(listeners.get(UserEvent.Type.UNDO));
      this.editMenu.add(this.redoItem = new JMenuItem(I18n.getLocalizedString("item.redo.text")));
      this.redoItem.setIcon(Images.REDO);
      this.redoItem.setMnemonic(I18n.getLocalizedMnemonic("item.redo"));
      this.redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
      this.redoItem.addActionListener(listeners.get(UserEvent.Type.REDO));
      this.editMenu.add(this.addCardItem = new JMenuItem(I18n.getLocalizedString("item.add_card.text")));
      this.addCardItem.setIcon(Images.ADD_CARD);
      this.addCardItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_card"));
      this.addCardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
      this.addCardItem.addActionListener(listeners.get(UserEvent.Type.ADD_CARD));
      this.editMenu.add(this.addLinkItem = new JMenuItem(I18n.getLocalizedString("item.add_link.text")));
      this.addLinkItem.setIcon(Images.ADD_LINK);
      this.addLinkItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_link"));
      this.addLinkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
      this.addLinkItem.addActionListener(listeners.get(UserEvent.Type.ADD_LINK));
      this.editMenu.add(this.editItem = new JMenuItem());
      this.editItem.setMnemonic(I18n.getLocalizedMnemonic("item.edit"));
      this.editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
      this.editMenu.add(this.deleteItem = new JMenuItem());
      this.deleteItem.setMnemonic(I18n.getLocalizedMnemonic("item.delete"));
      this.deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
      menuBar.add(this.editMenu);
    }

    // 'Options' menu
    {
      JMenu optionsMenu = new JMenu(I18n.getLocalizedString("menu.options.text"));
      optionsMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.options"));
      optionsMenu.add(this.checkUpdatesItem = new JCheckBoxMenuItem(I18n.getLocalizedString("item.check_updates.text")));
      this.checkUpdatesItem.setMnemonic(I18n.getLocalizedMnemonic("item.check_updates"));
      this.checkUpdatesItem.addActionListener(listeners.get(UserEvent.Type.TOGGLE_CHECK_UPDATES));
      optionsMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.colors.text")));
      i.setIcon(Images.COLOR_WHEEL);
      i.setMnemonic(I18n.getLocalizedMnemonic("item.colors"));
      i.addActionListener(listeners.get(UserEvent.Type.EDIT_COLORS));
      JMenu langMenu = new JMenu(I18n.getLocalizedString("menu.lang.text"));
      langMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.lang"));
      optionsMenu.add(langMenu);
      ButtonGroup bg = new ButtonGroup();
      for (Language l : Language.values()) {
        langMenu.add(i = new JRadioButtonMenuItem(l.getName()));
        i.setSelected(l == config.getLanguage());
        i.setIcon(Images.getCountryFlag(l));
        i.addActionListener(e -> EventsDispatcher.EVENT_BUS.dispatchEvent(new ChangeLanguageEvent(l)));
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
      i.setMnemonic(I18n.getLocalizedMnemonic("item.help"));
      i.addActionListener(listeners.get(UserEvent.Type.HELP));
      helpMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.about.text")));
      i.setMnemonic(I18n.getLocalizedMnemonic("item.about"));
      i.addActionListener(listeners.get(UserEvent.Type.ABOUT));
      menuBar.add(helpMenu);
    }

    return menuBar;
  }

  /**
   * Initializes the tool bar.
   * 
   * @param listeners the action listeners
   * @return the tool bar
   */
  private JToolBar getJToolBar(Map<UserEvent.Type, ActionListener> listeners) {
    JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
    toolBar.setFloatable(false);
    toolBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
    JButton b;

    toolBar.add(b = new JButton(Images.NEW_TREE_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.new_tree.text") + " (Ctrl+N)");
    b.setFocusable(false);
    b.addActionListener(listeners.get(UserEvent.Type.NEW));
    toolBar.add(b = new JButton(Images.OPEN_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.open.text") + " (Ctrl+O)");
    b.setFocusable(false);
    b.addActionListener(listeners.get(UserEvent.Type.OPEN));
    toolBar.add(this.saveBtn = new JButton(Images.SAVE_BIG));
    this.saveBtn.setToolTipText(I18n.getLocalizedString("item.save.text") + " (Ctrl+S)");
    this.saveBtn.setFocusable(false);
    this.saveBtn.addActionListener(listeners.get(UserEvent.Type.SAVE));
    toolBar.add(this.saveAsBtn = new JButton(Images.SAVE_AS_BIG));
    this.saveAsBtn.setToolTipText(I18n.getLocalizedString("item.save_as.text") + " (Ctrl+Maj+S)");
    this.saveAsBtn.setFocusable(false);
    this.saveAsBtn.addActionListener(listeners.get(UserEvent.Type.SAVE_AS));
    toolBar.add(this.undoBtn = new JButton(Images.UNDO_BIG));
    this.undoBtn.setToolTipText(I18n.getLocalizedString("item.undo.text") + " (Ctrl+Z)");
    this.undoBtn.setFocusable(false);
    this.undoBtn.addActionListener(listeners.get(UserEvent.Type.UNDO));
    toolBar.add(this.redoBtn = new JButton(Images.REDO_BIG));
    this.redoBtn.setToolTipText(I18n.getLocalizedString("item.redo.text") + " (Ctrl+Y)");
    this.redoBtn.setFocusable(false);
    this.redoBtn.addActionListener(listeners.get(UserEvent.Type.REDO));
    toolBar.add(this.addCardBtn = new JButton(Images.ADD_CARD_BIG));
    this.addCardBtn.setToolTipText(I18n.getLocalizedString("item.add_card.text") + " (Ctrl+A)");
    this.addCardBtn.setFocusable(false);
    this.addCardBtn.addActionListener(listeners.get(UserEvent.Type.ADD_CARD));
    toolBar.add(this.editCardBtn = new JButton(Images.EDIT_CARD_BIG));
    this.editCardBtn.setToolTipText(I18n.getLocalizedString("item.edit_card.text") + " (Ctrl+E)");
    this.editCardBtn.setFocusable(false);
    this.editCardBtn.addActionListener(listeners.get(UserEvent.Type.EDIT_CARD));
    toolBar.add(this.deleteCardBtn = new JButton(Images.DELETE_CARD_BIG));
    this.deleteCardBtn.setToolTipText(I18n.getLocalizedString("item.delete_card.text") + " (Supprimer)");
    this.deleteCardBtn.setFocusable(false);
    this.deleteCardBtn.addActionListener(listeners.get(UserEvent.Type.DELETE_CARD));
    toolBar.add(this.addLinkBtn = new JToggleButton(Images.ADD_LINK_BIG));
    this.addLinkBtn.setToolTipText(I18n.getLocalizedString("item.add_link.text") + " (Ctrl+L)");
    this.addLinkBtn.setFocusable(false);
    this.addLinkBtn.addActionListener(listeners.get(UserEvent.Type.ADD_LINK));
    toolBar.add(this.editLinkBtn = new JButton(Images.EDIT_LINK_BIG));
    this.editLinkBtn.setToolTipText(I18n.getLocalizedString("item.edit_link.text") + " (Ctrl+E)");
    this.editLinkBtn.setFocusable(false);
    this.editLinkBtn.addActionListener(listeners.get(UserEvent.Type.EDIT_LINK));
    toolBar.add(this.deleteLinkBtn = new JButton(Images.DELETE_LINK_BIG));
    this.deleteLinkBtn.setToolTipText(I18n.getLocalizedString("item.delete_link.text") + " (Supprimer)");
    this.deleteLinkBtn.setFocusable(false);
    this.deleteLinkBtn.addActionListener(listeners.get(UserEvent.Type.DELETE_LINK));

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
  public void updateMenus(boolean fileOpen, boolean cardSelected, boolean linkSelected, boolean canUndo, boolean canRedo) {
    this.editTreeItem.setEnabled(fileOpen);
    this.saveAsItem.setEnabled(fileOpen);
    this.undoItem.setEnabled(canUndo);
    this.redoItem.setEnabled(canRedo);
    this.editMenu.setEnabled(fileOpen);
    this.addCardItem.setEnabled(fileOpen);
    this.addLinkItem.setEnabled(fileOpen);
    this.editItem.setEnabled(fileOpen && (cardSelected || linkSelected));
    this.deleteItem.setEnabled(fileOpen && (cardSelected || linkSelected));

    this.saveAsBtn.setEnabled(fileOpen);
    this.undoBtn.setEnabled(canUndo);
    this.redoBtn.setEnabled(canRedo);
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
        this.editItem.removeActionListener(this.listeners.get(UserEvent.Type.EDIT_LINK));
        this.editItem.addActionListener(this.listeners.get(UserEvent.Type.EDIT_CARD));
        this.deleteItem.setText(I18n.getLocalizedString("item.delete_card.text"));
        this.deleteItem.setIcon(Images.DELETE_CARD);
        this.deleteItem.removeActionListener(this.listeners.get(UserEvent.Type.DELETE_LINK));
        this.deleteItem.addActionListener(this.listeners.get(UserEvent.Type.DELETE_CARD));
      }
      else if (linkSelected) {
        this.editItem.setText(I18n.getLocalizedString("item.edit_link.text"));
        this.editItem.setIcon(Images.EDIT_LINK);
        this.editItem.removeActionListener(this.listeners.get(UserEvent.Type.EDIT_CARD));
        this.editItem.addActionListener(this.listeners.get(UserEvent.Type.EDIT_LINK));
        this.deleteItem.setText(I18n.getLocalizedString("item.delete_link.text"));
        this.deleteItem.setIcon(Images.DELETE_LINK);
        this.deleteItem.removeActionListener(this.listeners.get(UserEvent.Type.DELETE_CARD));
        this.deleteItem.addActionListener(this.listeners.get(UserEvent.Type.DELETE_LINK));
      }
      else {
        this.editItem.setText(I18n.getLocalizedString("item.edit.text"));
        this.editItem.setIcon(null);
        this.editItem.removeActionListener(this.listeners.get(UserEvent.Type.EDIT_CARD));
        this.editItem.removeActionListener(this.listeners.get(UserEvent.Type.EDIT_LINK));
        this.deleteItem.setText(I18n.getLocalizedString("item.delete.text"));
        this.deleteItem.setIcon(null);
        this.deleteItem.removeActionListener(this.listeners.get(UserEvent.Type.DELETE_CARD));
        this.deleteItem.removeActionListener(this.listeners.get(UserEvent.Type.DELETE_LINK));
      }
    }
  }

  /**
   * Selects the given panels as background.
   * 
   * @param ids panels' IDs
   */
  public void setPanelsSelectedAsBackground(List<Long> ids) {
    this.displayPnl.selectPanelsAsBackground(ids);
  }

  /**
   * @return true if the check updates item is seleted
   */
  public boolean isCheckUpdatesItemSelected() {
    return this.checkUpdatesItem.isSelected();
  }

  /**
   * Sets the selection of the check updates item.
   */
  public void setCheckUpdatesItemSelected(boolean selected) {
    this.checkUpdatesItem.setSelected(selected);
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

  public static final int UPDATES_BLOCKED = 0;
  public static final int CHECKING_UPDATES = 1;
  public static final int NEW_UPDATE = 2;
  public static final int NO_UPDATE = 3;
  public static final int UPDATES_CHECK_FAILED = 4;

  /**
   * Sets the text and icon of the updates status.
   * 
   * @param mode one of these values: {@link #UPDATES_BLOCKED}, {@link #CHECKING_UPDATES},
   *          {@link #NEW_UPDATE}, {@link #UPDATES_CHECK_FAILED}, {@link #NO_UPDATE}
   * @param str an optional string to append to the end of the status
   */
  public void setUpdateLabelText(int mode, @Nullable String str) {
    Icon icon = null;
    String s = null;

    switch (mode) {
      case UPDATES_BLOCKED:
        icon = Images.UPDATE_CHECK_FAILED;
        s = I18n.getLocalizedString("label.updates_check_blocked.text");
        break;
      case CHECKING_UPDATES:
        icon = Images.CHECKING_UPDATES;
        s = I18n.getLocalizedString("label.checking_updates.text");
        break;
      case NEW_UPDATE:
        icon = Images.NEW_UPDATE;
        s = I18n.getLocalizedString("label.update_available.text");
        break;
      case NO_UPDATE:
        icon = null;
        s = I18n.getLocalizedString("label.no_update.text");
        break;
      case UPDATES_CHECK_FAILED:
        icon = Images.UPDATE_CHECK_FAILED;
        s = I18n.getLocalizedString("label.update_check_failed.text");
        break;
    }

    if (s != null && str != null) {
      s += str;
    }

    this.updateLbl.setIcon(icon);
    this.updateLbl.setText(s);
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
    this.treeDialog.setInfo(null);
    this.treeDialog.setCanceled(false);
    this.treeDialog.setVisible(true);

    return this.treeDialog.getTreeName();
  }

  /**
   * Shows the tree edit dialog.
   * 
   * @param currentName tree's current name
   * @return the tree name or nothing if the dialog was dismissed/canceled
   */
  public Optional<String> showEditTreeDialog(String currentName) {
    this.treeDialog.setInfo(currentName);
    this.treeDialog.setCanceled(false);
    this.treeDialog.setVisible(true);

    return this.treeDialog.getTreeName();
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
   * Shows the "about" dialog.
   */
  public void showAboutDialog() {
    this.aboutDialog.setVisible(true);
  }

  /**
   * Shows the update dialog.
   * 
   * @param version update's version
   * @param link update's link
   * @param changelog update's changelog
   */
  public void showUpdateDialog(Version version, String link, String changelog) {
    this.updateDialog.setInfo(version, link, changelog);
    this.updateDialog.setVisible(true);
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
    return showConfirmDialog(message, JOptionPane.YES_NO_CANCEL_OPTION);
  }

  /**
   * Shows a confirm dialog.
   * 
   * @param message the message
   * @param optionType an integer designating the options available on the dialog: YES_NO_OPTION,
   *          YES_NO_CANCEL_OPTION, or OK_CANCEL_OPTION
   * @return an integer indicating the option selected by the user (either YES_OPTION, NO_OPTION,
   *         CANCEL_OPTION or CLOSED_OPTION)
   */
  public int showConfirmDialog(String message, int optionType) {
    return JOptionPane.showConfirmDialog(this, message, I18n.getLocalizedString("popup.confirm.title"), optionType,
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
