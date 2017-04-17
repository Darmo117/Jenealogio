package net.darmo_creations.gui.dialog.tree_creation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.darmo_creations.gui.dialog.DefaultDialogController;

/**
 * This controller handles the TreeCreationDialog class events.
 * 
 * @author Damien Vergnet
 */
public class TreeCreationController extends DefaultDialogController<TreeCreationDialog> implements DocumentListener {
  /**
   * Creates a controller.
   * 
   * @param dialog the dialog to monitor
   */
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

  /**
   * Updates the dialog.
   */
  private void update() {
    this.dialog.setValidateButtonEnabled(this.dialog.getTreeName().isPresent());
  }

  @Override
  public void changedUpdate(DocumentEvent e) {}
}
