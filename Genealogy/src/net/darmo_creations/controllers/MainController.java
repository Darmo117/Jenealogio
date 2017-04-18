package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import net.darmo_creations.dao.ConfigDao;
import net.darmo_creations.dao.FamilyDao;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.model.GlobalConfig;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Observable;
import net.darmo_creations.util.Observer;

/**
 * This is controller handles events from the MainFrame class.
 * 
 * @author Damien Vergnet
 */
public class MainController extends WindowAdapter implements ActionListener, Observer {
  private static final Pattern DOUBLE_CLICK_MEMBER_PATTERN = Pattern.compile("double-click:member-(\\d+)");
  private static final Pattern DOUBLE_CLICK_WEDDING_PATTERN = Pattern.compile("double-click:link-(\\d+)-(\\d+)");
  private static final Pattern CLICK_WEDDING_PATTERN = Pattern.compile("click:link-(\\d+)-(\\d+)");
  private static final Pattern CHANGE_LANG_PATTERN = Pattern.compile("lang-(\\w+)");

  /** Frame being monitored. */
  private final MainFrame frame;
  /** Main DAO. */
  private final FamilyDao familyDao;

  private GlobalConfig config;
  /** The family (model). */
  private Family family;
  /** Is a file open? */
  private boolean fileOpen;
  /** Has the file already been saved? */
  private boolean alreadySaved;
  /** Has the file been saved since the last modifications? */
  private boolean saved;
  /** The name of the last saved file. */
  private String fileName;
  /** The currently selected card. */
  private FamilyMember selectedCard;
  /** The currently selected link. */
  private Wedding selectedLink;
  /** Are we adding a link? */
  private boolean addingLink;

  public MainController(MainFrame frame, GlobalConfig config) {
    this.frame = frame;
    this.config = config;
    this.familyDao = FamilyDao.instance();
  }

  /**
   * Initializes the controller.
   */
  public void init() {
    this.fileOpen = false;
    this.alreadySaved = false;
    this.saved = false;
    updateFrameMenus();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "new":
        newFile();
        break;
      case "open":
        open();
        break;
      case "save":
        boolean ok;
        if (this.alreadySaved)
          ok = save();
        else
          ok = saveAs();
        if (!ok)
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.save_file_error.text"));
        break;
      case "save-as":
        if (!saveAs())
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.save_file_error.text"));
        break;
      case "add-card":
        addMember();
        break;
      case "add-link":
        toggleAddLink();
        break;
      case "edit":
        edit();
        break;
      case "delete":
        delete();
        break;
      case "edit_colors":
        editColors();
        break;
      case "about":
        this.frame.showAboutDialog();
        break;
      case "exit":
        exit();
        break;
    }

    Matcher m = CHANGE_LANG_PATTERN.matcher(e.getActionCommand());

    if (m.matches()) {
      this.config.setLocale(I18n.Language.fromCode(m.group(1)).getLocale());
      this.frame.showWarningDialog(I18n.getLocalizedString("popup.change_language_warning.text"));
    }
  }

  @Override
  public void windowClosing(WindowEvent e) {
    exit();
  }

  @Override
  public void update(Observable obs, Object o) {
    if (o instanceof Long) {
      long id = (Long) o;

      this.frame.updateMenus(this.fileOpen, id >= 0, false);
      if (id >= 0) {
        FamilyMember prev = null;

        if (this.addingLink && this.selectedCard != null && !this.family.isMarried(this.selectedCard))
          prev = this.selectedCard;

        this.selectedCard = this.family.getMember(id).orElseThrow(IllegalStateException::new);

        if (prev != null && !prev.equals(this.selectedCard) && !this.family.isMarried(this.selectedCard)) {
          addLink(prev, this.selectedCard);
          toggleAddLink();
        }
      }
      else {
        if (this.addingLink)
          toggleAddLink();
        this.selectedCard = null;
      }
      this.selectedLink = null;
    }
    else if (o instanceof String) {
      String s = (String) o;
      Matcher m = DOUBLE_CLICK_MEMBER_PATTERN.matcher(s);
      Matcher m1 = DOUBLE_CLICK_WEDDING_PATTERN.matcher(s);
      Matcher m2 = CLICK_WEDDING_PATTERN.matcher(s);

      if (m.matches())
        showDetails(Long.parseLong(m.group(1)));
      else if (m1.matches())
        edit();
      else if (m2.matches()) {
        Optional<FamilyMember> optM = this.family.getMember(Long.parseLong(m2.group(1)));
        if (optM.isPresent()) {
          Optional<Wedding> w = this.family.getWedding(optM.get());
          if (w.isPresent())
            this.selectedLink = w.get();
        }
        this.frame.updateMenus(this.fileOpen, false, this.selectedLink != null);
      }
      else if (s.equals("edit"))
        edit();
    }
  }

  /**
   * Creates a new file. Asks the user if the current file is not saved.
   */
  private void newFile() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.save_confirm.text"));

      if (choice == JOptionPane.YES_OPTION)
        save();
      else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
        return;
    }

    Optional<String> name = this.frame.showCreateTreeDialog();

    if (name.isPresent()) {
      this.family = new Family(name.get());
      this.fileOpen = true;
      this.alreadySaved = false;
      this.saved = false;
      this.frame.resetDisplay();
      updateFrameMenus();
    }
  }

  /**
   * Opens a file. Asks the user if the current file is not saved.
   */
  private void open() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.open_confirm.text"));
      if (choice != JOptionPane.YES_OPTION)
        return;
    }

    Optional<File> opt = this.frame.showOpenFileChooser();

    if (opt.isPresent()) {
      this.fileName = opt.get().getAbsolutePath();
      try {
        Map<Long, Point> positions = new HashMap<>();

        this.family = this.familyDao.load(this.fileName, positions);
        this.fileOpen = true;
        this.alreadySaved = true;
        this.saved = true;
        this.frame.resetDisplay();
        this.frame.refreshDisplay(this.family, positions, this.config);
      }
      catch (IOException | ParseException __) {
        this.frame.showErrorDialog(I18n.getLocalizedString("popup.open_file_error.text"));
      }
      updateFrameMenus();
    }
  }

  /**
   * Saves the file as another file.
   * 
   * @return true if and only if save was successful
   */
  private boolean saveAs() {
    Optional<File> opt = this.frame.showSaveFileChooser();

    if (opt.isPresent()) {
      String path = opt.get().getAbsolutePath();

      if (!path.endsWith(".gtree"))
        path += ".gtree";
      this.fileName = path;

      return save();
    }

    return true;
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
      Map<Long, Point> points = this.frame.getCardsPositions();
      this.familyDao.save(this.fileName, this.family, points);

      if (!this.alreadySaved)
        this.alreadySaved = true;
      this.saved = true;
      return true;
    }
    catch (IOException __) {
      this.frame.showErrorDialog(I18n.getLocalizedString("popup.save_file_error.text"));
      return false;
    }
  }

  /**
   * Opens up the "add card" dialog then adds the new card to the model.
   */
  private void addMember() {
    Optional<FamilyMember> member = this.frame.showAddCardDialog();

    if (member.isPresent()) {
      this.family.addMember(member.get());
      refreshFrame();
    }
  }

  /**
   * Opens up the "add link" dialog then adds the new link to the model.
   */
  private void addLink(FamilyMember spouse1, FamilyMember spouse2) {
    Set<FamilyMember> all = this.family.getAllMembers();
    all.remove(spouse1);
    all.remove(spouse2);
    Optional<Wedding> optWedding = this.frame.showAddLinkDialog(spouse1, spouse2, all);
    optWedding.ifPresent(w -> this.family.addWedding(w));
    this.frame.refreshDisplay(this.family, this.config);
  }

  /**
   * Toggles add link mode.
   */
  private void toggleAddLink() {
    this.addingLink = !this.addingLink;
    this.frame.setAddLinkButtonSelected(this.addingLink);
  }

  /**
   * Opens up "edit card" or "edit link" dialog then updates the model.
   */
  private void edit() {
    if (this.selectedCard != null) {
      Optional<FamilyMember> member = this.frame.showUpdateCardDialog(this.selectedCard);

      if (member.isPresent()) {
        this.family.updateMember(member.get());
        refreshFrame();
      }
    }
    else if (this.selectedLink != null) {
      Optional<Wedding> wedding = this.frame.showUpdateLinkDialog(this.selectedLink, this.family.getPotentialChildren(this.selectedLink));

      if (wedding.isPresent()) {
        this.family.updateWedding(wedding.get());
        refreshFrame();
      }
    }
  }

  /**
   * Deletes the selected card or link. Asks the user to confirm the action.
   */
  private void delete() {
    if (this.selectedCard != null) {
      if (this.frame.showConfirmDialog(I18n.getLocalizedString("popup.delete_card_confirm.text")) == JOptionPane.OK_OPTION) {
        this.family.removeMember(this.selectedCard);
        this.selectedCard = null;
        updateFrameMenus();
        refreshFrame();
      }
    }
    else if (this.selectedLink != null) {
      if (this.frame.showConfirmDialog(I18n.getLocalizedString("popup.delete_link_confirm.text")) == JOptionPane.OK_OPTION) {
        this.family.removeWedding(this.selectedLink);
        this.selectedLink = null;
        updateFrameMenus();
        refreshFrame();
      }
    }
  }

  private void editColors() {
    Optional<GlobalConfig> opt = this.frame.showEditColorsDialog(this.config);

    if (opt.isPresent()) {
      this.config = opt.get();
      if (this.fileOpen)
        this.frame.refreshDisplay(this.family, this.config);
    }
  }

  /**
   * Exits the application. The user is asked to save if the file is not.
   */
  private void exit() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.save_confirm.text"));

      if (choice == JOptionPane.YES_OPTION) {
        boolean ok;

        if (this.alreadySaved)
          ok = save();
        else
          ok = saveAs();
        if (!ok) {
          this.frame.showErrorDialog(I18n.getLocalizedString("popup.save_file_error.text"));
          return;
        }
      }
      else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
        return;
    }

    ConfigDao.getInstance().save(this.config);
    this.frame.dispose();
  }

  /**
   * Updates main frame menus.
   */
  private void updateFrameMenus() {
    this.frame.setTitle(MainFrame.BASE_TITLE + (this.family != null ? " - " + this.family.getName() : ""));
    this.frame.updateMenus(this.fileOpen, this.selectedCard != null, this.selectedLink != null);
  }

  /**
   * Refreshes tree display.
   */
  private void refreshFrame() {
    this.frame.refreshDisplay(this.family, this.config);
  }

  /**
   * Opens up the "card details" dialog.
   * 
   * @param id the member ID
   */
  private void showDetails(long id) {
    this.family.getMember(id).ifPresent(m -> this.frame.showDetailsDialog(m, this.family.getWedding(m).orElse(null)));
  }
}
