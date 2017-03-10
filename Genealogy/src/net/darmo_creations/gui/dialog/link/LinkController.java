package net.darmo_creations.gui.dialog.link;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.darmo_creations.controllers.DefaultDialogController;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;

public class LinkController extends DefaultDialogController<LinkDialog> implements KeyListener, ListSelectionListener {
  private FamilyMember spouse1, spouse2;

  public LinkController(LinkDialog dialog) {
    super(dialog);
  }

  public void reset(FamilyMember spouse1, FamilyMember spouse2, Set<FamilyMember> children) {
    reset(new Wedding(null, null, spouse1, spouse2), children);
  }

  public void reset(Wedding wedding, Set<FamilyMember> children) {
    this.spouse1 = wedding.getSpouse1();
    this.spouse2 = wedding.getSpouse2();

    this.dialog.setSpouse1(wedding.getSpouse1().toString());
    this.dialog.setSpouse2(wedding.getSpouse2().toString());
    this.dialog.setDate(wedding.getDate());
    this.dialog.setWeddingLocation(wedding.getLocation());
    this.dialog.setAvailableChildren(children);
    this.dialog.setChildren(wedding.getChildren());

    this.dialog.setCanceled(false);
    this.dialog.setAddButtonEnabled(false);
    this.dialog.setDeleteButtonEnabled(false);
  }

  public Wedding getWedding() {
    return new Wedding(this.dialog.getDate(), this.dialog.getWeddingLocation(), this.spouse1, this.spouse2, this.dialog.getChildren());
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
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      JList<?> list = (JList<?>) e.getSource();

      if (list.getName().equals("children")) {
        this.dialog.setDeleteButtonEnabled(e.getFirstIndex() != -1);
      }
      else if (list.getName().equals("available-children")) {
        this.dialog.setAddButtonEnabled(e.getFirstIndex() != -1);
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
