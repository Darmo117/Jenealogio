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
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.gui_framework.controllers.ApplicationController;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.jenealogio.Jenealogio;
import net.darmo_creations.jenealogio.config.ColorTag;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.dao.FamilyDao;
import net.darmo_creations.jenealogio.events.CardDragEvent;
import net.darmo_creations.jenealogio.events.CardEvent;
import net.darmo_creations.jenealogio.events.CardsSelectionEvent;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.events.LinkEvent;
import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.jenealogio.gui.components.display_panel.DisplayPanel;
import net.darmo_creations.jenealogio.model.FamilyEdit;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.FilesUtil;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.UndoRedoManager;
import net.darmo_creations.utils.events.SubsribeEvent;
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
  /** The currently selected card */
  private FamilyMember lastSelectedCard;
  /** All currently selected cards */
  private List<FamilyMember> selectedCards;
  /** The currently selected link */
  private Relationship selectedLink;
  /** Are we adding a link? */
  private boolean addingLink;

  /** Undo/redo manager */
  private UndoRedoManager<FamilyEdit> undoRedoManager;

  public MainController(MainFrame frame, WritableConfig config) {
    super(frame, config);
    this.familyDao = FamilyDao.instance();
    this.lastSavedEdit = null;
    this.selectedCards = new ArrayList<>();

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
  @SubsribeEvent
  public void onUserEvent(UserEvent e) {
    super.onUserEvent(e);

    if (e.isCanceled()) {
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
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(-1, false));
          toggleAddLink();
          break;
        case EDIT_CARD:
          editCard();
          break;
        case EDIT_LINK:
          editLink();
          break;
        case DELETE_CARD:
          deleteCard();
          break;
        case DELETE_LINK:
          deleteLink();
          break;
        case EDIT_COLORS:
          editColors();
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
      e.setCanceled();
  }

  /**
   * Called when a card is clicked.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onCardClicked(CardEvent.Clicked e) {
    long id = e.getMemberId();
    boolean keepSelection = e.keepPreviousSelection();

    this.selectedLink = null;
    this.frame.updateMenus(this.fileOpen, id >= 0, false, canUndo(), canRedo());
    if (id >= 0) {
      FamilyMember prev = null;

      if (!this.addingLink
          || (this.addingLink && this.lastSelectedCard != null && !this.family.areInRelationship(this.lastSelectedCard.getId(), id)))
        prev = this.lastSelectedCard;

      this.lastSelectedCard = this.family.getMember(id).orElseThrow(IllegalStateException::new);

      if (this.addingLink && prev != null) {
        addLink(prev.getId(), this.lastSelectedCard.getId());
        toggleAddLink();
      }

      if (keepSelection) {
        this.selectedCards.removeIf(m -> m.getId() == this.lastSelectedCard.getId());
        if (prev != null && prev.getId() != this.lastSelectedCard.getId())
          this.selectedCards.add(prev);
      }
      else {
        this.selectedCards.clear();
      }
    }
    else {
      if (this.addingLink)
        toggleAddLink();
      this.lastSelectedCard = null;
      this.selectedCards.clear();
    }
    this.frame.setPanelsSelectedAsBackground(this.selectedCards.stream().map(f -> f.getId()).collect(Collectors.toList()));
  }

  /**
   * Called when a card is double-clicked.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onCardDoubleClicked(CardEvent.DoubleClicked e) {
    this.family.getMember(e.getMemberId()).ifPresent(m -> this.frame.showDetailsDialog(m, this.family.getRelations(m.getId())));
  }

  /**
   * Called when a link is clicked.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onLinkClicked(LinkEvent.Clicked e) {
    Optional<FamilyMember> optM1 = this.family.getMember(e.getPartner1Id());
    Optional<FamilyMember> optM2 = this.family.getMember(e.getPartner2Id());

    if (optM1.isPresent() && optM2.isPresent()) {
      Optional<Relationship> r = this.family.getRelation(optM1.get().getId(), optM2.get().getId());

      if (r.isPresent())
        this.selectedLink = r.get();
    }
    this.frame.updateMenus(this.fileOpen, false, this.selectedLink != null, canUndo(), canRedo());
  }

  /**
   * Called when a link is double-clicked.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onLinkDoubleClicked(LinkEvent.DoubleClicked e) {
    showDetails(e.getPartner1Id(), e.getPartner2Id());
  }

  /**
   * Called when several cards are selected.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onCardsSelected(CardsSelectionEvent e) {
    List<Long> ids = new ArrayList<>();
    for (long l : e.getSelectedPanelsIds())
      ids.add(l);
    this.frame.setPanelsSelectedAsBackground(ids);
  }

  /**
   * Called when a card is going to be dragged.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onCardDragPre(CardDragEvent.Pre e) {
    this.saved = false;
    updateFrameMenus();
  }

  /**
   * Called when a card has been dragged.
   * 
   * @param e the event
   */
  @SubsribeEvent
  public void onCardDragPost(CardDragEvent.Post e) {
    addEdit();
    updateFrameMenus();
  }

  @Override
  public boolean acceptFiles(List<File> files, Component c) {
    if (!(c instanceof DisplayPanel) || files.size() != 1)
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
        if (event.isCanceled())
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
        this.lastSavedEdit = new FamilyEdit(this.family, this.frame.getCardsPositions());
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
      this.frame.refreshDisplay(this.family, this.lastSavedEdit.getLocations(), this.config);
    }
    catch (VersionException ex) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.version_warning.text"));

      if (choice == JOptionPane.YES_OPTION) {
        loadFile(fileName, true);
      }
    }
    catch (IOException | ParseException __) {
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
      FamilyEdit newSave = new FamilyEdit(this.family, this.frame.getCardsPositions());

      this.familyDao.save(this.fileName, newSave);
      this.lastSavedEdit = newSave;

      if (!this.alreadySaved)
        this.alreadySaved = true;
      this.saved = true;
      this.frame.updateSaveMenus(this.saved);

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
      Map<Long, Point> points = this.frame.getCardsPositions();
      points.put(this.family.getGlobalId() - 1, this.frame.getDisplayMiddlePoint());
      this.frame.refreshDisplay(this.family, points, this.config);
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
      refreshFrame();
      updateFrameMenus();
    }
  }

  /**
   * Toggles add link mode.
   */
  private void toggleAddLink() {
    this.addingLink = !this.addingLink;
    this.frame.setAddLinkButtonSelected(this.addingLink);
  }

  /**
   * Opens up "edit card" dialog then updates the model.
   */
  private void editCard() {
    if (this.lastSelectedCard != null) {
      Optional<FamilyMember> member = this.frame.showUpdateCardDialog(this.lastSelectedCard);

      if (member.isPresent()) {
        this.family.updateMember(member.get());
        addEdit();
        this.saved = false;
        updateFrameMenus();
        refreshFrame();
      }
    }
  }

  /**
   * Opens up "edit link" dialog then updates the model.
   */
  private void editLink() {
    if (this.selectedLink != null) {
      Optional<Relationship> relation = this.frame.showUpdateLinkDialog(this.selectedLink, this.family);

      if (relation.isPresent()) {
        this.family.updateRelation(relation.get());
        addEdit();
        this.saved = false;
        updateFrameMenus();
        refreshFrame();
      }
    }
  }

  /**
   * Deletes the selected card(s). Asks the user to confirm the action.
   */
  private void deleteCard() {
    if (this.lastSelectedCard != null || !this.selectedCards.isEmpty()) {
      String key = !this.selectedCards.isEmpty() ? "popup.delete_cards_confirm.text" : "popup.delete_card_confirm.text";
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString(key));

      if (choice == JOptionPane.YES_OPTION) {
        this.family.removeMember(this.lastSelectedCard.getId());
        this.selectedCards.forEach(m -> this.family.removeMember(m.getId()));
        addEdit();
        this.lastSelectedCard = null;
        this.selectedCards.clear();
        this.saved = false;
        updateFrameMenus();
        refreshFrame();
      }
    }
  }

  /**
   * Deletes the selected link. Asks the user to confirm the action.
   */
  private void deleteLink() {
    if (this.selectedLink != null) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.delete_link_confirm.text"));

      if (choice == JOptionPane.YES_OPTION) {
        this.family.removeRelationship(this.selectedLink);
        addEdit();
        this.selectedLink = null;
        this.saved = false;
        updateFrameMenus();
        refreshFrame();
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
        refreshFrame();
    }
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
    this.frame.updateMenus(this.fileOpen, this.lastSelectedCard != null, this.selectedLink != null, canUndo(), canRedo());
    this.frame.updateSaveMenus(this.saved);
  }

  /**
   * Refreshes tree display.
   */
  private void refreshFrame() {
    this.frame.refreshDisplay(this.family, this.config);
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
    this.undoRedoManager.addEdit(new FamilyEdit(this.family, this.frame.getCardsPositions()));
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
    this.frame.refreshDisplay(this.family, edit.getLocations(), this.config);
    updateFrameMenus();
  }

  /**
   * Tells if the user can undo changes.
   */
  private boolean canUndo() {
    return this.undoRedoManager.canUndo();
  }

  /**
   * Tells if the user can redo changes.
   */
  private boolean canRedo() {
    return this.undoRedoManager.canRedo();
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
        if (event.isCanceled())
          e.setCanceled();
      }
      else if (choice == JOptionPane.CLOSED_OPTION)
        e.setCanceled();
    }
  }
}
