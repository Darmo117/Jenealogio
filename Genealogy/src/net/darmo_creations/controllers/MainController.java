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

import net.darmo_creations.dao.FamilyDao;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Observer;

public class MainController extends WindowAdapter implements ActionListener, Observer {
  private static final Pattern DOUBLE_CLICK_MEMBER_PATTERN = Pattern.compile("double-click:member-(\\d+)");
  private static final Pattern DOUBLE_CLICK_WEDDING_PATTERN = Pattern.compile("double-click:link-(\\d+)-(\\d+)");
  private static final Pattern CLICK_WEDDING_PATTERN = Pattern.compile("click:link-(\\d+)-(\\d+)");

  private final MainFrame frame;

  private final FamilyDao familyDao;

  private Family family;
  private boolean fileOpen;
  private boolean alreadySaved;
  private boolean saved;
  private String fileName;
  private FamilyMember selectedCard;
  private Wedding selectedLink;
  private boolean addingLink;

  public MainController(MainFrame frame) {
    this.frame = frame;
    this.familyDao = FamilyDao.instance();
  }

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
      case "about":
        this.frame.showAboutDialog();
        break;
      case "exit":
        exit();
        break;
    }
  }

  @Override
  public void windowClosing(WindowEvent e) {
    exit();
  }

  @Override
  public void update(Object o) {
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

      // TEMP
      this.family.addMember(
          new FamilyMember(null, "Smith", "John", Gender.MAN, new Date(1913, 5, 7), "Londres", new Date(1982, 3, 29), "Paris"));
      this.family.addMember(
          new FamilyMember(null, "Smith", "Johanna", Gender.WOMAN, new Date(1913, 5, 7), "Londres", new Date(1982, 3, 29), "Paris"));
      this.family.addMember(
          new FamilyMember(null, "Paul", "John", Gender.MAN, new Date(1913, 5, 7), "Londres", new Date(1982, 3, 29), "Paris"));
      this.family.addMember(
          new FamilyMember(null, "Paul", "Johanna", Gender.WOMAN, new Date(1913, 5, 7), "Londres", new Date(1982, 3, 29), "Paris"));
      this.frame.refreshDisplay(this.family);
    }
  }

  private void open() {
    if (this.fileOpen) {
      int choice = this.frame.showConfirmDialog(I18n.getLocalizedString("popup.open_confirm.text"));

      if (choice != JOptionPane.YES_OPTION)
        return;
    }

    File file = this.frame.showOpenFileChooser();

    if (file != null) {
      this.fileName = file.getAbsolutePath();
      try {
        Map<Long, Point> positions = new HashMap<>();

        this.family = this.familyDao.open(this.fileName, positions);
        this.fileOpen = true;
        this.alreadySaved = true;
        this.saved = true;
        this.frame.resetDisplay();
        this.frame.refreshDisplay(this.family, positions);
      }
      catch (IOException | ParseException __) {
        this.frame.showErrorDialog(I18n.getLocalizedString("popup.open_file_error.text"));
      }
      updateFrameMenus();
    }
  }

  private boolean saveAs() {
    File file = this.frame.showSaveFileChooser();

    if (file != null) {
      String path = file.getAbsolutePath();

      if (!path.endsWith(".gtree"))
        path += ".gtree";
      this.fileName = path;

      return save();
    }

    return true;
  }

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

  private void addMember() {
    Optional<FamilyMember> member = this.frame.showAddCardDialog();

    if (member.isPresent()) {
      this.family.addMember(member.get());
      refreshFrame();
    }
  }

  private void addLink(FamilyMember spouse1, FamilyMember spouse2) {
    Set<FamilyMember> all = this.family.getAllMembers();
    all.remove(spouse1);
    all.remove(spouse2);
    Optional<Wedding> optWedding = this.frame.showAddLinkDialog(spouse1, spouse2, all);
    optWedding.ifPresent(w -> this.family.addWedding(w));
    this.frame.refreshDisplay(this.family);
  }

  private void toggleAddLink() {
    this.addingLink = !this.addingLink;
    this.frame.setAddLinkButtonSelected(this.addingLink);
  }

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

    I18n.saveLocale();
    this.frame.dispose();
  }

  private void updateFrameMenus() {
    this.frame.setTitle(MainFrame.BASE_TITLE + (this.family != null ? " - " + this.family.getName() : ""));
    this.frame.updateMenus(this.fileOpen, this.selectedCard != null, this.selectedLink != null);
  }

  private void refreshFrame() {
    this.frame.refreshDisplay(this.family);
  }

  private void showDetails(long id) {
    this.family.getMember(id).ifPresent(m -> this.frame.showDetailsDialog(m, this.family.getWedding(m).orElse(null)));
  }
}
