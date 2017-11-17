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
package net.darmo_creations.jenealogio.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.OneExtensionFileFilter;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.gui_framework.gui.ApplicationFrame;
import net.darmo_creations.jenealogio.Jenealogio;
import net.darmo_creations.jenealogio.controllers.MainController;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasView;
import net.darmo_creations.jenealogio.gui.components.side_view.SideView;
import net.darmo_creations.jenealogio.gui.dialog.CardDetailsDialog;
import net.darmo_creations.jenealogio.gui.dialog.LinkDetailsDialog;
import net.darmo_creations.jenealogio.gui.dialog.card.CardDialog;
import net.darmo_creations.jenealogio.gui.dialog.link.LinkDialog;
import net.darmo_creations.jenealogio.gui.dialog.options.EditColorsDialog;
import net.darmo_creations.jenealogio.gui.dialog.tree_creation.TreeDialog;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.FilesUtil;
import net.darmo_creations.utils.I18n;

/**
 * The main frame of the application.
 *
 * @author Damien Vergnet
 */
public class MainFrame extends ApplicationFrame<MainController> {
  private static final long serialVersionUID = 2426665404072947885L;

  private JFileChooser treeFileChooser, exportFileChooser;
  private TreeDialog treeDialog;
  private CardDialog cardDialog;
  private CardDetailsDialog cardDetailsDialog;
  private LinkDialog linkDialog;
  private LinkDetailsDialog linkDetailsDialog;
  private EditColorsDialog editColorsDialog;

  private JMenu editMenu;
  private JMenuItem editTreeItem, saveItem, saveAsItem, exportImageItem, undoItem, redoItem, addCardItem, addLinkItem, editItem, deleteItem;
  private JButton saveBtn, saveAsBtn, undoBtn, redoBtn, addCardBtn, editBtn, deleteBtn;
  private JToggleButton addLinkBtn;
  private CanvasView canvasView;
  private SideView sideView;

  public MainFrame(WritableConfig config) {
    super(config, true, true, true, true, new Dimension(800, 600), true);
  }

  @Override
  protected MainController preInit(WritableConfig config) {
    return new MainController(this, config);
  }

  @Override
  protected void initContent(MainController controller, WritableConfig config) {
    this.treeFileChooser = new JFileChooser();
    this.treeFileChooser.setAcceptAllFileFilterUsed(false);
    this.treeFileChooser.setMultiSelectionEnabled(false);
    this.treeFileChooser.setFileFilter(
        new OneExtensionFileFilter(I18n.getLocalizedString("file_type.tree.desc"), Jenealogio.TREE_FILE_EXT));
    this.exportFileChooser = new JFileChooser();
    this.exportFileChooser.setAcceptAllFileFilterUsed(false);
    this.exportFileChooser.setMultiSelectionEnabled(false);
    for (String ext : Jenealogio.IMAGE_FILES_EXTS)
      this.exportFileChooser.addChoosableFileFilter(
          new OneExtensionFileFilter(I18n.toTitleCase(I18n.getLocalizedString("word.image")), ext));
    this.treeDialog = new TreeDialog(this);
    this.cardDialog = new CardDialog(this);
    this.cardDetailsDialog = new CardDetailsDialog(this);
    this.linkDialog = new LinkDialog(this);
    this.linkDetailsDialog = new LinkDetailsDialog(this);
    this.editColorsDialog = new EditColorsDialog(this);

    JSplitPane splitPane = new JSplitPane();
    splitPane.setContinuousLayout(true);

    this.sideView = new SideView();
    splitPane.setLeftComponent(this.sideView);

    JScrollPane canvasPane = new JScrollPane();
    this.canvasView = new CanvasView(canvasPane);
    this.canvasView.addDragAndDropListener(controller);
    canvasPane.setViewportView(this.canvasView);
    splitPane.setRightComponent(canvasPane);

    add(splitPane, BorderLayout.CENTER);
  }

  /**
   * Initializes the menu bar.
   * 
   * @param listeners the action listeners
   * @return the menu bar
   */
  @Override
  protected JMenuBar initJMenuBar(Map<UserEvent.Type, ActionListener> listeners, WritableConfig config) {
    for (EventType type : EventType.values())
      listeners.put(type, e -> {
        System.out.println(type); // DEBUG
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(type));
      });

    JMenuBar menuBar = super.initJMenuBar(listeners, config);
    JMenuItem i;

    // 'Files' menu
    JMenu fileMenu = new JMenu(I18n.getLocalizedString("menu.file.text"));
    fileMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.file"));

    fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.new_tree.text")));
    i.setIcon(Images.NEW_TREE);
    i.setMnemonic(I18n.getLocalizedMnemonic("item.new_tree"));
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
    i.addActionListener(listeners.get(EventType.NEW));

    fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.open.text")));
    i.setIcon(Images.OPEN);
    i.setMnemonic(I18n.getLocalizedMnemonic("item.open"));
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
    i.addActionListener(listeners.get(EventType.OPEN));

    fileMenu.addSeparator();

    fileMenu.add(this.editTreeItem = new JMenuItem(I18n.getLocalizedString("item.edit_tree.text")));
    this.editTreeItem.setIcon(Images.EDIT_TREE);
    this.editTreeItem.setMnemonic(I18n.getLocalizedMnemonic("item.edit_tree"));
    this.editTreeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    this.editTreeItem.addActionListener(listeners.get(EventType.EDIT_TREE));

    fileMenu.add(this.saveItem = new JMenuItem(I18n.getLocalizedString("item.save.text")));
    this.saveItem.setIcon(Images.SAVE);
    this.saveItem.setMnemonic(I18n.getLocalizedMnemonic("item.save"));
    this.saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
    this.saveItem.addActionListener(listeners.get(EventType.SAVE));

    fileMenu.add(this.saveAsItem = new JMenuItem(I18n.getLocalizedString("item.save_as.text")));
    this.saveAsItem.setIcon(Images.SAVE_AS);
    this.saveAsItem.setMnemonic(I18n.getLocalizedMnemonic("item.save_as"));
    this.saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
    this.saveAsItem.addActionListener(listeners.get(EventType.SAVE_AS));

    fileMenu.addSeparator();

    fileMenu.add(this.exportImageItem = new JMenuItem(I18n.getLocalizedString("item.export_image.text")));
    this.exportImageItem.setIcon(Images.EXPORT_IMAGE);
    this.exportImageItem.setMnemonic(I18n.getLocalizedMnemonic("item.export_image"));
    this.exportImageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
    this.exportImageItem.addActionListener(listeners.get(EventType.EXPORT_IMAGE));

    fileMenu.addSeparator();

    fileMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.exit.text")));
    i.setIcon(Images.EXIT);
    i.setMnemonic(I18n.getLocalizedMnemonic("item.exit"));
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
    i.addActionListener(listeners.get(UserEvent.DefaultType.EXITING));

    menuBar.add(fileMenu, 0);

    // 'Edit' menu
    this.editMenu = new JMenu(I18n.getLocalizedString("menu.edit.text"));
    this.editMenu.setMnemonic(I18n.getLocalizedMnemonic("menu.edit"));

    this.editMenu.add(this.undoItem = new JMenuItem(I18n.getLocalizedString("item.undo.text")));
    this.undoItem.setIcon(Images.UNDO);
    this.undoItem.setMnemonic(I18n.getLocalizedMnemonic("item.undo"));
    this.undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
    this.undoItem.addActionListener(listeners.get(EventType.UNDO));

    this.editMenu.add(this.redoItem = new JMenuItem(I18n.getLocalizedString("item.redo.text")));
    this.redoItem.setIcon(Images.REDO);
    this.redoItem.setMnemonic(I18n.getLocalizedMnemonic("item.redo"));
    this.redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
    this.redoItem.addActionListener(listeners.get(EventType.REDO));

    this.editMenu.addSeparator();

    this.editMenu.add(this.addCardItem = new JMenuItem(I18n.getLocalizedString("item.add_card.text")));
    this.addCardItem.setIcon(Images.ADD_CARD);
    this.addCardItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_card"));
    this.addCardItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
    this.addCardItem.addActionListener(listeners.get(EventType.ADD_CARD));

    this.editMenu.add(this.addLinkItem = new JMenuItem(I18n.getLocalizedString("item.add_link.text")));
    this.addLinkItem.setIcon(Images.ADD_LINK);
    this.addLinkItem.setMnemonic(I18n.getLocalizedMnemonic("item.add_link"));
    this.addLinkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
    this.addLinkItem.addActionListener(listeners.get(EventType.ADD_LINK));

    this.editMenu.add(this.editItem = new JMenuItem(I18n.getLocalizedString("item.edit.text")));
    this.editItem.setIcon(Images.EDIT);
    this.editItem.setMnemonic(I18n.getLocalizedMnemonic("item.edit"));
    this.editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
    this.editItem.addActionListener(listeners.get(EventType.EDIT_OBJECT));

    this.editMenu.add(this.deleteItem = new JMenuItem(I18n.getLocalizedString("item.delete.text")));
    this.deleteItem.setIcon(Images.DELETE);
    this.deleteItem.setMnemonic(I18n.getLocalizedMnemonic("item.delete"));
    this.deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    this.deleteItem.addActionListener(listeners.get(EventType.DELETE_OBJECT));

    menuBar.add(this.editMenu, 1);

    // 'Options' menu
    menuBar.getMenu(2).add(i = new JMenuItem(I18n.getLocalizedString("item.colors.text")), 1);
    i.setIcon(Images.COLOR_WHEEL);
    i.setMnemonic(I18n.getLocalizedMnemonic("item.colors"));
    i.addActionListener(listeners.get(EventType.EDIT_COLORS));

    return menuBar;
  }

  /**
   * Initializes the tool bar.
   * 
   * @param listeners the action listeners
   * @return the tool bar
   */
  @Override
  protected JToolBar initJToolBar(Map<UserEvent.Type, ActionListener> listeners) {
    JToolBar toolBar = super.initJToolBar(listeners);
    JButton b;

    toolBar.add(b = new JButton(Images.NEW_TREE_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.new_tree.text") + " (Ctrl+N)");
    b.setFocusable(false);
    b.addActionListener(listeners.get(EventType.NEW));

    toolBar.add(b = new JButton(Images.OPEN_BIG));
    b.setToolTipText(I18n.getLocalizedString("item.open.text") + " (Ctrl+O)");
    b.setFocusable(false);
    b.addActionListener(listeners.get(EventType.OPEN));

    toolBar.addSeparator();

    toolBar.add(this.saveBtn = new JButton(Images.SAVE_BIG));
    this.saveBtn.setToolTipText(I18n.getLocalizedString("item.save.text") + " (Ctrl+S)");
    this.saveBtn.setFocusable(false);
    this.saveBtn.addActionListener(listeners.get(EventType.SAVE));

    toolBar.add(this.saveAsBtn = new JButton(Images.SAVE_AS_BIG));
    this.saveAsBtn.setToolTipText(I18n.getLocalizedString("item.save_as.text") + " (Ctrl+Maj+S)");
    this.saveAsBtn.setFocusable(false);
    this.saveAsBtn.addActionListener(listeners.get(EventType.SAVE_AS));

    toolBar.addSeparator();

    toolBar.add(this.undoBtn = new JButton(Images.UNDO_BIG));
    this.undoBtn.setToolTipText(I18n.getLocalizedString("item.undo.text") + " (Ctrl+Z)");
    this.undoBtn.setFocusable(false);
    this.undoBtn.addActionListener(listeners.get(EventType.UNDO));

    toolBar.add(this.redoBtn = new JButton(Images.REDO_BIG));
    this.redoBtn.setToolTipText(I18n.getLocalizedString("item.redo.text") + " (Ctrl+Y)");
    this.redoBtn.setFocusable(false);
    this.redoBtn.addActionListener(listeners.get(EventType.REDO));

    toolBar.addSeparator();

    toolBar.add(this.addCardBtn = new JButton(Images.ADD_CARD_BIG));
    this.addCardBtn.setToolTipText(I18n.getLocalizedString("item.add_card.text") + " (Ctrl+A)");
    this.addCardBtn.setFocusable(false);
    this.addCardBtn.addActionListener(listeners.get(EventType.ADD_CARD));

    toolBar.add(this.addLinkBtn = new JToggleButton(Images.ADD_LINK_BIG));
    this.addLinkBtn.setToolTipText(I18n.getLocalizedString("item.add_link.text") + " (Ctrl+L)");
    this.addLinkBtn.setFocusable(false);
    this.addLinkBtn.addActionListener(listeners.get(EventType.ADD_LINK));

    toolBar.add(this.editBtn = new JButton(Images.EDIT_BIG));
    this.editBtn.setToolTipText(I18n.getLocalizedString("item.edit.text") + " (Ctrl+E)");
    this.editBtn.setFocusable(false);
    this.editBtn.addActionListener(listeners.get(EventType.EDIT_OBJECT));

    toolBar.add(this.deleteBtn = new JButton(Images.DELETE_BIG));
    this.deleteBtn.setToolTipText(I18n.getLocalizedString("item.delete.text") + " (Supprimer)");
    this.deleteBtn.setFocusable(false);
    this.deleteBtn.addActionListener(listeners.get(EventType.DELETE_OBJECT));

    return toolBar;
  }

  public void setEditTreeEnabled(boolean enabled) {
    this.editTreeItem.setEnabled(enabled);
  }

  public void setExportEnabled(boolean enabled) {
    this.exportImageItem.setEnabled(enabled);
  }

  public void setEditEnabled(boolean enabled) {
    this.editMenu.setEnabled(enabled);
  }

  public void setSaveEnabled(boolean enabled) {
    this.saveItem.setEnabled(enabled);
    this.saveBtn.setEnabled(enabled);
  }

  public void setSaveAsEnabled(boolean enabled) {
    this.saveAsItem.setEnabled(enabled);
    this.saveAsBtn.setEnabled(enabled);
  }

  public void setUndoEnabled(boolean enabled) {
    this.undoItem.setEnabled(enabled);
    this.undoBtn.setEnabled(enabled);
  }

  public void setRedoEnabled(boolean enabled) {
    this.redoItem.setEnabled(enabled);
    this.redoBtn.setEnabled(enabled);
  }

  public void setAddObjectEnabled(boolean enabled) {
    this.addCardItem.setEnabled(enabled);
    this.addLinkItem.setEnabled(enabled);
    this.addCardBtn.setEnabled(enabled);
    this.addLinkBtn.setEnabled(enabled);
  }

  public void setEditObjectEnabled(boolean enabled) {
    this.editItem.setEnabled(enabled);
    this.editBtn.setEnabled(enabled);
  }

  public void setDeleteObjectEnabled(boolean enabled) {
    this.deleteItem.setEnabled(enabled);
    this.deleteBtn.setEnabled(enabled);
  }

  /**
   * Sets the selection state of the "add link" button.
   * 
   * @param selected
   */
  public void setAddLinkSelected(boolean selected) {
    this.addLinkBtn.setSelected(selected);
  }

  /**
   * Resets the tree display.
   */
  public void resetDisplay() {
    this.canvasView.reset();
    this.sideView.reset();
  }

  /**
   * Refreshes the tree display.
   * 
   * @param family the tree
   */
  public void refreshDisplay(Family family, WritableConfig config) {
    this.canvasView.refresh(family, config);
    this.sideView.refresh(family);
  }

  /**
   * Refreshes the tree display using the given cards positions.
   * 
   * @param family the tree
   * @param positions positions for all cards
   */
  public void refreshDisplay(Family family, Map<Long, Point> positions, Map<Long, Dimension> sizes, WritableConfig config) {
    this.canvasView.refresh(family, positions, sizes, config);
    this.sideView.refresh(family);
    this.canvasView.requestFocus();
  }

  /**
   * Returns the selection from the given view.
   * 
   * @param view the view
   * @return the selection
   */
  public Selection getSelection(ViewType view) {
    switch (view) {
      case CANVAS:
        return this.canvasView.getSelection();
      case SIDE:
        return this.sideView.getSelection();
    }

    throw new NullPointerException();
  }

  /**
   * @return the positions of all cards
   */
  public Map<Long, Point> getCardsPositions() {
    return this.canvasView.getCardsPositions();
  }

  /**
   * @return the sizes of all cards
   */
  public Map<Long, Dimension> getCardsSizes() {
    return this.canvasView.getCardsSizes();
  }

  /**
   * @return the display's current middle coordinate
   */
  public Point getDisplayMiddlePoint() {
    JViewport viewport = (JViewport) this.canvasView.getParent();
    Point pos = viewport.getViewPosition();
    Dimension size = viewport.getExtentSize();
    int x = pos.x + size.width / 2;
    int y = pos.y + size.height / 2;

    return new Point(x, y);
  }

  /**
   * Exports the display panel to an image.
   * 
   * @return the image
   */
  public BufferedImage exportToImage() {
    return this.canvasView.exportToImage();
  }

  /**
   * Shows the "open" file chooser.
   * 
   * @return the selected file
   */
  public Optional<File> showOpenFileChooser() {
    this.treeFileChooser.setDialogTitle(I18n.getLocalizedString("dialog.open.title"));
    int choice = this.treeFileChooser.showOpenDialog(this);
    return Optional.ofNullable(choice != JFileChooser.APPROVE_OPTION ? null : this.treeFileChooser.getSelectedFile());
  }

  /**
   * Shows the "save" file chooser. The returned file is is guaranted to have a valid extension.
   * 
   * @return the selected file
   */
  public Optional<File> showSaveFileChooser() {
    this.treeFileChooser.setDialogTitle(I18n.getLocalizedString("dialog.save_as.title"));
    return showSaveFileChooser(this.treeFileChooser);
  }

  /**
   * Shows the "export image" file chooser. The returned file is is guaranted to have a valid
   * extension.
   * 
   * @return the selected file
   */
  public Optional<File> showExportImageFileChooser() {
    this.exportFileChooser.setDialogTitle(I18n.getLocalizedString("dialog.export_image.title"));
    return showSaveFileChooser(this.exportFileChooser);
  }

  /**
   * Shows the save dialog of the given file chooser the returns the selected file if any. The
   * extension is added if the user didn't put it.
   * 
   * @param fileChooser the file chooser
   * @return the selected file
   */
  private Optional<File> showSaveFileChooser(JFileChooser fileChooser) {
    int choice = fileChooser.showSaveDialog(this);

    if (choice == JFileChooser.APPROVE_OPTION) {
      OneExtensionFileFilter filter = (OneExtensionFileFilter) fileChooser.getFileFilter();
      String ext = filter.getExtension();
      String path = fileChooser.getSelectedFile().getAbsolutePath();

      if (!FilesUtil.hasExtension(path, ext)) {
        path += "." + ext;
      }

      return Optional.of(new File(path));
    }

    return Optional.empty();
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
  public Optional<WritableConfig> showEditColorsDialog(WritableConfig config) {
    this.editColorsDialog.setConfig(config.clone());
    this.editColorsDialog.setVisible(true);
    return this.editColorsDialog.getConfig();
  }
}
