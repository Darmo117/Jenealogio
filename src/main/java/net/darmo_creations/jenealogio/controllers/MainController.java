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
package net.darmo_creations.jenealogio.controllers;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JOptionPane;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.config.tags.BooleanTag;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.jenealogio.Jenealogio;
import net.darmo_creations.jenealogio.config.ColorTag;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.dao.FamilyDao;
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.events.FocusChangeEvent;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.events.ViewEditEvent;
import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasView;
import net.darmo_creations.jenealogio.model.CardState;
import net.darmo_creations.jenealogio.model.FamilyEdit;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.FilesUtil;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.UndoRedoManager;
import net.darmo_creations.utils.events.SubscribeEvent;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropListener;
import net.darmo_creations.utils.version.VersionException;

/**
 * This is controller handles events from the MainFrame class.
 * 
 * @author Damien Vergnet
 */
public class MainController extends ApplicationController<MainFrame> implements DragAndDropListener {
  /** Main DAO */
  private final FamilyDao familyDao;

  private ViewType currentView;
  /** The family (model) */
  private Family family;
  /** Last save */
  private FamilyEdit lastSavedEdit;
  /** Is a file open? */
  private boolean fileOpen;
  /** Has the file already been saved? */
  private boolean alreadySaved;
  /** Has the file been saved since the last modifications? */
  private boolean saved;
  /** The name of the last saved file */
  private String fileName;
  /** Are we adding a link? */
  private boolean addingLink;

  /** Undo/redo manager */
  private UndoRedoManager<FamilyEdit> undoRedoManager;

  public MainController(MainFrame frame, WritableConfig config) {
    super(frame, config);
    this.familyDao = FamilyDao.instance();
    this.lastSavedEdit = null;

    this.undoRedoManager = new UndoRedoManager<>();
  }

  /**
   * Initializes the controller.
   */
  @Override
  public void init() {
    super.init();
    this.fileOpen = false;
    this.alreadySaved = false;
    this.saved = true;
    updateFrameMenus();
  }

  /**
   * Called when a UserEvent is fired.
   * 
   * @param e the event
   */
  @Override
  @SubscribeEvent
  public void onUserEvent(UserEvent e) {
    super.onUserEvent(e);

    if (e.isCancelled()) {
      return;
    }

    if (e.getType() == UserEvent.DefaultType.EXIT) {
      checkExit(e);
    }

    if (e.getType() instanceof EventType) {
      switch ((EventType) e.getType()) {
        case NEW:
          newFile();
          break;
        case EDIT_TREE:
          editTree();
          break;
        case OPEN:
          open(null);
          break;
        case SAVE:
          boolean ok;
          if (this.alreadySaved)
            ok = save();
          else
            ok = saveAs();
          if (!ok)
            handleSaveError(e);
          updateFrameMenus();
          break;
        case SAVE_AS:
          if (!saveAs())
            handleSaveError(e);
          updateFrameMenus();
          break;
        case UNDO:
          undo();
          break;
        case REDO:
          redo();
          break;
        case ADD_CARD:
          addMember();
          break;
        case ADD_LINK:
          toggleAddLink();
          break;
        case EDIT_OBJECT:
          editObject();
          break;
        case DELETE_OBJECT:
          deleteObjects();
          break;
        case EDIT_COLORS:
          editColors();
          break;
        case TOGGLE_GRID:
          toggleGrid();
          break;
        case EXPORT_IMAGE:
          exportImage();
          break;
      }
    }
  }

  private void handleSaveError(UserEvent e) {
    int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.save_file_error.text"));
    if (choice == JOptionPane.NO_OPTION)
      e.setCancelled();
  }

  @SubscribeEvent
  public void onFocusChanged(FocusChangeEvent e) {
    this.currentView = e.getView();
    updateFrameMenus();
  }

  /**
   * Called when a card is double-clicked.
   * 
   * @param e the event
   */
  @SubscribeEvent
  public void onCardDoubleClicked(CardDoubleClickEvent e) {
    this.family.getMember(e.getMemberId()).ifPresent(m -> this.frame.showDetailsDialog(m, this.family.getRelations(m.getId())));
  }

  /**
   * Called when the selection inside the active view changes.
   * 
   * @param e the event
   */
  @SubscribeEvent
  public void onSelectionChanged(SelectionChangeEvent e) {
    Selection old = e.getLastSelection();
    Selection current = e.getNewSelection();

    if (this.addingLink) {
      if (old.getMembers().size() == 1 && current.getMembers().size() == 1) {
        List<Long> previous = old.getMembers();
        List<Long> cur = current.getMembers();

        if (this.family.areInRelation(previous.get(0), cur.get(0))) {
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.already_in_relationship.text"));
        }
        else if (previous.size() == 1 && current.size() == 1 && !this.family.areInRelation(previous.get(0), cur.get(0))) {
          addLink(previous.get(0), cur.get(0));
        }
        toggleAddLink();
      }
    }

    updateFrameMenus();
  }

  @SubscribeEvent
  public void onViewEdit(ViewEditEvent e) {
    this.saved = false;
    addEdit();
    updateFrameMenus();
  }

  /**
   * Called when a link is double-clicked.
   * 
   * @param e the event
   */
  @SubscribeEvent
  public void onLinkDoubleClicked(LinkDoubleClickEvent e) {
    showDetails(e.getPartner1Id(), e.getPartner2Id());
  }

  @Override
  public boolean acceptFiles(List<File> files, Component c) {
    if (!(c instanceof CanvasView) || files.size() != 1)
      return false;
    return FilesUtil.hasExtension(files.get(0), Jenealogio.TREE_FILE_EXT);
  }

  @Override
  public void importFiles(List<File> files) {
    open(files.get(0));
  }

  /**
   * Checks that the file has been saved. If not, the user is asked if they want to save.
   * 
   * @return true if the file has been saved or the user doesn't want to; false if the user canceled
   *         or an error occured
   */
  private boolean checkSaved() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.save_confirm.text"), JOptionPane.YES_NO_CANCEL_OPTION);

      if (choice == JOptionPane.YES_OPTION) {
        UserEvent event = new UserEvent(EventType.SAVE);
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(event);
        if (event.isCancelled())
          return false;
      }
      else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
        return false;
    }

    return true;
  }

  /**
   * Creates a new file. Asks the user if the current file is not saved.
   */
  private void newFile() {
    if (checkSaved()) {
      Optional<String> name = this.frame.showCreateTreeDialog();

      if (name.isPresent()) {
        this.undoRedoManager.clear();
        this.family = new Family(name.get());
        this.fileOpen = true;
        this.alreadySaved = false;
        this.saved = false;
        this.frame.resetDisplay();
        this.lastSavedEdit = new FamilyEdit(this.family, this.frame.getCardsStates());
        addEdit();
        updateFrameMenus();
      }
    }
  }

  /**
   * Opens the "edit tree" dialog and updates the tree.
   */
  private void editTree() {
    Optional<String> opt = this.frame.showEditTreeDialog(this.family.getName());

    if (opt.isPresent()) {
      this.family.setName(opt.get());
      this.saved = false;
      addEdit();
      updateFrameMenus();
    }
  }

  /**
   * Opens a file. Asks the user if the current file is not saved.
   * 
   * @param file if not null, this method will attempt to open this file instead of asking the user;
   *          save state is still checked
   */
  private void open(final File file) {
    if (checkSaved()) {
      Optional<File> opt;

      if (file == null) {
        opt = this.frame.showOpenFileChooser();
      }
      else {
        opt = Optional.of(file);
      }

      if (opt.isPresent()) {
        loadFile(opt.get().getAbsolutePath(), false);
      }
    }
  }

  /**
   * Loads the file with the given name.
   * 
   * @param fileName file's name
   * @param ignoreVersion if true, any version mismatch will be ignored
   * @see FamilyDao#load(String, Map, boolean)
   */
  private void loadFile(String fileName, boolean ignoreVersion) {
    try {
      this.undoRedoManager.clear();
      this.lastSavedEdit = this.familyDao.load(fileName, ignoreVersion);
      this.family = this.lastSavedEdit.getFamily();
      this.undoRedoManager.addEdit(this.lastSavedEdit);
      this.fileName = fileName;
      this.fileOpen = true;
      this.alreadySaved = true;
      this.saved = true;
      this.frame.resetDisplay();
      this.frame.refreshDisplay(this.family, this.lastSavedEdit.getStates(), this.config);
    }
    catch (VersionException ex) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.version_warning.text"));

      if (choice == JOptionPane.YES_OPTION) {
        loadFile(fileName, true);
      }
    }
    catch (IOException | ParseException ex) {
      this.frame.showErrorDialog(I18n.getLocalizedString("popup.open_file_error.text"));
    }
    updateFrameMenus();
  }

  /**
   * Saves the file as another file.
   * 
   * @return true if and only if save was successful or cancelled
   */
  private boolean saveAs() {
    boolean exit = false;

    while (!exit) {
      Optional<File> opt = this.frame.showSaveFileChooser();

      if (opt.isPresent()) {
        this.fileName = opt.get().getAbsolutePath();
        if (Files.exists(Paths.get(this.fileName))) {
          int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.file_already_exists.text"));

          if (choice == JOptionPane.YES_OPTION)
            exit = true;
        }
        else
          exit = true;
      }
      else
        return true;
    }

    return save();
  }

  /**
   * Saves the current file.
   * 
   * @return true if and only if the save was successful
   */
  private boolean save() {
    if (this.fileName == null)
      return true;

    try {
      FamilyEdit newSave = new FamilyEdit(this.family, this.frame.getCardsStates());

      this.familyDao.save(this.fileName, newSave);
      this.lastSavedEdit = newSave;

      if (!this.alreadySaved)
        this.alreadySaved = true;
      this.saved = true;
      updateFrameMenus();

      return true;
    }
    catch (IOException ex) {
      return false;
    }
  }

  /**
   * Opens up the "add card" dialog then adds the new card to the model.
   */
  private void addMember() {
    Optional<FamilyMember> member = this.frame.showAddCardDialog();

    if (member.isPresent()) {
      this.saved = false;
      this.family.addMember(member.get());
      Map<Long, CardState> cardsStates = this.frame.getCardsStates();
      cardsStates.put(this.family.getGlobalId() - 1, new CardState(this.frame.getDisplayMiddlePoint(), null));
      this.frame.getView(this.currentView).deselectAll();
      this.frame.refreshDisplay(this.family, cardsStates, this.config);
      addEdit();
      updateFrameMenus();
    }
  }

  /**
   * Opens up the "add link" dialog then adds the new link to the model.
   */
  private void addLink(long partner1, long partner2) {
    Optional<Relationship> optWedding = this.frame.showAddLinkDialog(partner1, partner2, this.family);

    if (optWedding.isPresent()) {
      this.family.addRelation(optWedding.get());
      this.saved = false;
      addEdit();
      refreshFrame(true);
      updateFrameMenus();
    }
  }

  /**
   * Toggles add link mode.
   */
  private void toggleAddLink() {
    this.addingLink = !this.addingLink;
    this.frame.setAddLinkSelected(this.addingLink);
    this.frame.deselectAll();
  }

  /**
   * Opens up and "edit" dialog then updates the model.
   */
  private void editObject() {
    Selection selection = this.frame.getView(this.currentView).getSelection();

    if (selection.size() == 1) {
      if (!selection.getMembers().isEmpty()) {
        Optional<FamilyMember> opt = this.family.getMember(selection.getMembers().get(0));

        if (opt.isPresent()) {
          Optional<FamilyMember> member = this.frame.showUpdateCardDialog(opt.get());

          if (member.isPresent()) {
            this.family.updateMember(member.get());
            addEdit();
            this.saved = false;
            refreshFrame(false);
            updateFrameMenus();
          }
        }
        else
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.nonexistant_object.text"));
      }
      else if (!selection.getRelations().isEmpty()) {
        Pair<Long, Long> link = selection.getRelations().get(0);
        Optional<Relationship> opt = this.family.getRelation(link.getValue1(), link.getValue2());

        if (opt.isPresent()) {
          Optional<Relationship> relation = this.frame.showUpdateLinkDialog(opt.get(), this.family);

          if (relation.isPresent()) {
            this.family.updateRelation(relation.get());
            addEdit();
            this.saved = false;
            refreshFrame(false);
            updateFrameMenus();
          }
        }
        else
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.nonexistant_object.text"));
      }
    }
  }

  /**
   * Deletes the selected object(s). Asks the user to confirm the action.
   */
  private void deleteObjects() {
    Selection selection = this.frame.getView(this.currentView).getSelection();

    if (!selection.isEmpty()) {
      String key = selection.size() > 1 ? "popup.delete_objects_confirm.text" : "popup.delete_object_confirm.text";
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString(key));

      if (choice == JOptionPane.YES_OPTION) {
        selection.getRelations().forEach(r -> this.family.removeRelation(r.getValue1(), r.getValue2()));
        selection.getMembers().forEach(id -> this.family.removeMember(id));

        this.saved = false;

        addEdit();
        refreshFrame(true);
        updateFrameMenus();
      }
    }
  }

  /**
   * Opens the "edit colors" dialog and updates the config.
   */
  private void editColors() {
    Optional<WritableConfig> opt = this.frame.showEditColorsDialog(this.config);

    if (opt.isPresent()) {
      WritableConfig c = opt.get();

      for (ColorTag tag : ConfigTags.COLORS_TAGS) {
        this.config.setValue(tag, c.getValue(tag));
      }

      if (this.fileOpen)
        refreshFrame(false);
    }
  }

  private void toggleGrid() {
    BooleanTag tag = ConfigTags.GRID_ENABLED;
    this.config.setValue(tag, !this.config.getValue(tag));
    if (this.fileOpen)
      refreshFrame(false);
  }

  /**
   * Exports the tree as an image.
   */
  private void exportImage() {
    boolean exit = false;
    String path = null;

    while (!exit) {
      Optional<File> opt = this.frame.showExportImageFileChooser();

      if (opt.isPresent()) {
        path = opt.get().getAbsolutePath();

        if (Files.exists(Paths.get(path))) {
          int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.file_already_exists.text"));

          if (choice == JOptionPane.YES_OPTION)
            exit = true;
        }
        else
          exit = true;
      }
      else
        return;
    }

    try {
      Images.writeImage(this.frame.exportToImage(), path);
      JOptionPane.showMessageDialog(this.frame, I18n.getLocalizedString("popup.image_export_success.text"),
          I18n.getLocalizedString("popup.image_export_success.title"), JOptionPane.INFORMATION_MESSAGE);
    }
    catch (IOException ex) {
      this.frame.showErrorDialog(I18n.getLocalizedString("popup.image_export_error.text"));
    }
  }

  /**
   * Updates main frame menus and title.
   */
  private void updateFrameMenus() {
    String title = this.family != null ? " - " + (this.saved ? "" : "*") + this.family.getName() : "";
    this.frame.setTitle(this.frame.getBaseTitle() + title);
    this.frame.setEditTreeEnabled(this.fileOpen);
    this.frame.setExportEnabled(this.fileOpen);
    this.frame.setEditEnabled(this.fileOpen);
    this.frame.setSaveEnabled(this.fileOpen && !this.saved);
    this.frame.setSaveAsEnabled(this.fileOpen);
    this.frame.setUndoEnabled(this.undoRedoManager.canUndo());
    this.frame.setRedoEnabled(this.undoRedoManager.canRedo());
    this.frame.setAddObjectEnabled(this.fileOpen);
    int selectedNb = this.currentView != null ? this.frame.getView(this.currentView).getSelection().size() : 0;
    this.frame.setEditObjectEnabled(selectedNb == 1);
    this.frame.setDeleteObjectEnabled(selectedNb > 0);
  }

  /**
   * Refreshes the display.
   */
  private void refreshFrame(boolean deselectAll) {
    this.frame.refreshDisplay(this.family, this.config);
    if (deselectAll)
      this.frame.getView(this.currentView).deselectAll();
  }

  /**
   * Opens up the "link details" dialog.
   * 
   * @param id1 the first ID
   * @param id2 the second ID
   */
  private void showDetails(long id1, long id2) {
    this.family.getRelation(id1, id2).ifPresent(r -> this.frame.showDetailsDialog(r, this.family));
  }

  /**
   * Adds the current family object (after cloning it) to the undo manager.
   */
  private void addEdit() {
    this.undoRedoManager.addEdit(new FamilyEdit(this.family, this.frame.getCardsStates()));
  }

  /**
   * Performs an undo action.
   */
  private void undo() {
    if (this.undoRedoManager.canUndo()) {
      this.undoRedoManager.undo();
      undoOrRedo();
    }
  }

  /**
   * Performs a redo action.
   */
  private void redo() {
    if (this.undoRedoManager.canRedo()) {
      this.undoRedoManager.redo();
      undoOrRedo();
    }
  }

  /**
   * Method used by undo() and redo().
   */
  private void undoOrRedo() {
    FamilyEdit edit = this.undoRedoManager.getEdit();
    if (edit.equals(this.lastSavedEdit))
      this.saved = true;
    else
      this.saved = false;
    this.family = edit.getFamily();
    this.frame.getView(this.currentView).deselectAll();
    this.frame.refreshDisplay(this.family, edit.getStates(), this.config);
    updateFrameMenus();
  }

  /**
   * Checks if the current file has been saved before exiting.
   * 
   * @param e the exit event
   */
  private void checkExit(UserEvent e) {
    if (!this.saved) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.save_confirm.text"));

      if (choice == JOptionPane.YES_OPTION) {
        UserEvent event = new UserEvent(EventType.SAVE);
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(event);
        if (event.isCancelled())
          e.setCancelled();
      }
      else if (choice == JOptionPane.CLOSED_OPTION)
        e.setCancelled();
    }
  }
}
