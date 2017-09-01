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
package net.darmo_creations.jenealogio.gui.dialog.tree_creation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.darmo_creations.jenealogio.gui.dialog.DefaultDialogController;

/**
 * This controller handles the TreeCreationDialog class events.
 * 
 * @author Damien Vergnet
 */
class TreeController extends DefaultDialogController<TreeDialog> implements DocumentListener {
  /**
   * Creates a controller.
   * 
   * @param dialog the dialog to monitor
   */
  TreeController(TreeDialog dialog) {
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

  @Override
  public void changedUpdate(DocumentEvent e) {
    update();
  }

  /**
   * Updates the dialog.
   */
  private void update() {
    this.dialog.setValidateButtonEnabled(this.dialog.getTreeName().isPresent());
  }
}
