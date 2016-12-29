package net.darmo_creations.gui.dialog.link;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.darmo_creations.controllers.DefaultDialogController;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.Wedding;

public class LinkController extends DefaultDialogController<LinkDialog> implements KeyListener, ListSelectionListener, ItemListener {
  private Family family;
  private boolean updatingCombo;

  public LinkController(LinkDialog dialog) {
    super(dialog);
  }

  public void reset(Wedding wedding, Family family) {
    this.family = family;
    this.updatingCombo = true;
    if (wedding == null) {
      this.dialog.setHusbandCombo(this.family.getPotentialHusbandsForWife(null), null);
      this.dialog.setWifeCombo(this.family.getPotentialWivesForHusband(null), null);
      this.dialog.setChildren(new HashSet<>());
      this.dialog.setAvailableChildren(this.family.getPotentialChildren(null));
    }
    else {
      this.dialog.setHusbandCombo(this.family.getPotentialHusbandsForWife(wedding.getWife()), wedding.getHusband());
      this.dialog.setWifeCombo(this.family.getPotentialWivesForHusband(wedding.getHusband()), wedding.getWife());
      this.dialog.setChildren(wedding.getChildren());
      this.dialog.setAvailableChildren(this.family.getPotentialChildren(wedding));
    }
    this.updatingCombo = false;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    switch (e.getActionCommand()) {
      case "add":
        this.dialog.addSelectedChildren();
        break;
      case "remove":
        this.dialog.removeSelectedChildren();
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    char c = e.getKeyChar();

    if ((c < '0' || c > '9') && c == KeyEvent.VK_BACK_SPACE && c == KeyEvent.VK_DELETE && c == KeyEvent.VK_SLASH) {
      e.consume();
    }
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    if (this.updatingCombo)
      return;

    this.updatingCombo = true;
    String name = ((JComboBox<?>) e.getSource()).getName();

    this.dialog.setValidateButtonEnabled(this.dialog.isHusbandSelected() && this.dialog.isWifeSelected());
    if (name.equals("husband"))
      this.dialog.setWifeCombo(this.family.getPotentialWivesForHusband(this.dialog.getSelectedHusband()), this.dialog.getSelectedWife());
    else if (name.equals("wife"))
      this.dialog.setHusbandCombo(this.family.getPotentialHusbandsForWife(this.dialog.getSelectedWife()), this.dialog.getSelectedHusband());
    this.dialog.setAvailableChildren(this.family.getPotentialChildren(this.dialog.isHusbandSelected() && this.dialog.isWifeSelected() ? this.dialog.getLink().get() : null));
    this.updatingCombo = false;
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      JList<?> list = (JList<?>) e.getSource();

      if (list.getName().equals("children"))
        this.dialog.setDeleteButtonEnabled(e.getFirstIndex() != -1);
      else if (list.getName().equals("available-children"))
        this.dialog.setAddButtonEnabled(e.getFirstIndex() != -1);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
