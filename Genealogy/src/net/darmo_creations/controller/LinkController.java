package net.darmo_creations.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.darmo_creations.gui.dialog.LinkDialog;
import net.darmo_creations.model.FamilyMember;
import net.darmo_creations.model.Wedding;

public class LinkController extends AbstractDialogController<LinkDialog> implements KeyListener, ListSelectionListener, ItemListener {
  private List<FamilyMember> availableChildren;

  public LinkController(LinkDialog dialog) {
    super(dialog);
    this.availableChildren = new ArrayList<>();
  }

  public void reset(Wedding wedding, List<FamilyMember> potentialHusbands, List<FamilyMember> potentialWives, List<FamilyMember> potentialChildren) {
    this.availableChildren.clear();
    this.availableChildren.addAll(potentialChildren);
    this.dialog.setHusbandCombo(potentialHusbands, null);
    this.dialog.setWifeCombo(potentialWives, null);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    if (e.getActionCommand().equals("add")) {

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
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting())
      this.dialog.setDeleteButtonEnabled(e.getFirstIndex() != -1);
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}
}
