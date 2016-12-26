package net.darmo_creations.controller;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.darmo_creations.gui.dialog.TreeCreationDialog;

public class TreeCreationController extends AbstractDialogController<TreeCreationDialog> implements DocumentListener {
  public TreeCreationController(TreeCreationDialog dialog) {
    super(dialog);
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    update();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    update();
  }

  private void update() {
    this.dialog.setValidateButtonEnabled(this.dialog.getTreeName().isPresent());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {}
}
