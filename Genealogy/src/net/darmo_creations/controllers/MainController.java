package net.darmo_creations.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JOptionPane;

import net.darmo_creations.dao.FamilyDao;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.Observer;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.model.family.Wedding;

public class MainController extends WindowAdapter implements ActionListener, Observer {
  private final MainFrame frame;

  private final FamilyDao familyDao;

  private Family family;
  private boolean fileOpen;
  private boolean alreadySaved;
  private boolean saved;
  private String fileName;
  private FamilyMember selectedCard;
  private Wedding selectedLink;

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
        if (this.alreadySaved) {
          save();
          break;
        }
      case "save-as":
        File file = this.frame.showSaveFileChooser();
        if (file != null)
          saveAs(file.getAbsolutePath());
        break;
      case "add-card":
        addMember();
        break;
      case "add-link":
        addLink();
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
      if (id >= 0)
        this.selectedCard = this.family.getMember(id).orElseThrow(IllegalStateException::new);
      else
        this.selectedCard = null;
    }
  }

  private void newFile() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog("Voulez-vous sauvegarder ?");

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
      this.family.addMember(new FamilyMember(null, "Smith", "John", Gender.MAN, new Date(1913, 5, 7), "Londres", new Date(1982, 3, 29), "Paris"));
      this.frame.refreshDisplay(this.family);
    }
  }

  private void open() {
    File file = this.frame.showOpenFileChooser();

    if (file != null) {
      this.fileName = file.getAbsolutePath();
      try {
        this.family = this.familyDao.open(this.fileName);
        this.fileOpen = true;
        this.alreadySaved = true;
        this.saved = true;
        this.frame.refreshDisplay(this.family);
      }
      catch (IOException e) {
        this.frame.showErrorDialog("Une erreur est survenue pendant l'ouverture du fichier.");
      }
      updateFrameMenus();
    }
  }

  private void saveAs(String name) {
    this.fileName = name;
    save();
  }

  private void save() {
    if (this.fileName == null)
      return;

    try {
      this.familyDao.save(this.family);

      if (!this.alreadySaved)
        this.alreadySaved = true;
      this.saved = true;
    }
    catch (IOException e) {
      this.frame.showErrorDialog("Une erreur est survenue pendant la sauvegarde !");
    }
  }

  private void addMember() {
    Optional<FamilyMember> member = this.frame.showAddCardDialog();

    if (member.isPresent()) {
      this.family.addMember(member.get());
      refreshFrame();
    }
  }

  private void addLink() {
    // @f0
    Optional<Wedding> wedding = this.frame.showAddLinkDialog(
        this.family.getPotentialHusbands(null),
        this.family.getPotentialWives(null),
        this.family.getPotentialChildren(null));
    // @f1

    if (wedding.isPresent()) {
      this.family.addWedding(wedding.get());
      refreshFrame();
    }
  }

  private void edit() {
    if (this.selectedCard != null) {
      Optional<FamilyMember> member = this.frame.showUpdateCard(this.selectedCard);

      if (member.isPresent()) {
        this.family.updateMember(member.get());
        refreshFrame();
      }
    }
    else if (this.selectedLink != null) {
      // @f0
      Optional<Wedding> wedding = this.frame.showUpdateLink(
          this.selectedLink,
          this.family.getPotentialHusbands(this.selectedLink.getHusband()),
          this.family.getPotentialWives(this.selectedLink.getHusband()),
          this.family.getPotentialChildren(this.selectedLink));
      // @f1

      if (wedding.isPresent()) {
        this.family.updateWedding(wedding.get());
        refreshFrame();
      }
    }
  }

  private void delete() {
    if (this.selectedCard != null) {
      if (this.frame.showConfirmDialog("Êtes-vous sûr de vouloir supprimer cette fiche ?") == JOptionPane.OK_OPTION) {
        this.family.removeMember(this.selectedCard);
        this.selectedCard = null;
        updateFrameMenus();
        refreshFrame();
      }
    }
    else if (this.selectedLink != null) {
      if (this.frame.showConfirmDialog("Êtes-vous sûr de vouloir supprimer de lien ?") == JOptionPane.OK_OPTION) {
        this.family.removeWedding(this.selectedLink);
        this.selectedLink = null;
        updateFrameMenus();
        refreshFrame();
      }
    }
  }

  private void exit() {
    if (this.fileOpen && !this.saved) {
      int choice = this.frame.showConfirmDialog("Voulez-vous sauvegarder ?");

      if (choice == JOptionPane.YES_OPTION)
        save();
      else if (choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
        return;
    }

    this.frame.dispose();
  }

  private void updateFrameMenus() {
    this.frame.setTitle(MainFrame.BASE_TITLE + (this.family != null ? " - " + this.family.getName() : ""));
    this.frame.updateMenus(this.fileOpen, this.selectedCard != null, this.selectedLink != null);
  }

  private void refreshFrame() {
    this.frame.refreshDisplay(this.family);
  }
}