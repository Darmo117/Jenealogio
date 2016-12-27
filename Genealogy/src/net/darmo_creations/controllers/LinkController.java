package net.darmo_creations.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.darmo_creations.gui.dialog.LinkDialog;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;

public class LinkController extends AbstractDialogController<LinkDialog> implements KeyListener, ListSelectionListener, ItemListener {
  public LinkController(LinkDialog dialog) {
    super(dialog);
  }

  public void reset(Wedding wedding, Set<FamilyMember> potentialHusbands, Set<FamilyMember> potentialWives, Set<FamilyMember> potentialChildren) {
    this.dialog.setHusbandCombo(potentialHusbands, null);
    this.dialog.setWifeCombo(potentialWives, null);
    this.dialog.setAvailableChildren(potentialChildren);
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

    if (!(c >= '0' && c <= '9' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SLASH)) {
      e.consume();
    }
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    this.dialog.setValidateButtonEnabled(this.dialog.isHusbandSelected() && this.dialog.isWifeSelected());
    this.dialog.updateLists();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      JList<FamilyMember> list = (JList<FamilyMember>) e.getSource();

      if (list.getName().equals("children"))
        this.dialog.setDeleteButtonEnabled(e.getFirstIndex() != -1);
      else if (list.getName().equals("available-children"))
        this.dialog.setAddButtonEnabled(e.getFirstIndex() != -1);
      this.dialog.updateLists();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
