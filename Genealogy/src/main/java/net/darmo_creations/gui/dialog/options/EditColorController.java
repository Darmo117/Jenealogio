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
package net.darmo_creations.gui.dialog.options;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.config.ColorConfigKey;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.DefaultDialogController;

/**
 * This controller handles events from the EditColorsDialog.
 *
 * @author Damien Vergnet
 */
class EditColorController extends DefaultDialogController<EditColorsDialog> implements TreeSelectionListener {
  private GlobalConfig config;
  private String selectedNode;

  EditColorController(EditColorsDialog dialog) {
    super(dialog);
    this.config = null;
    this.selectedNode = null;
  }

  /**
   * @return the current config
   */
  GlobalConfig getConfig() {
    return this.config;
  }

  /**
   * Sets the config. Will also update the dialog.
   * 
   * @param config the new config
   */
  void setConfig(GlobalConfig config) {
    this.dialog.setCanceled(false);
    this.config = config;
    if (this.selectedNode != null)
      this.dialog.setButtonColor(getColorForNode());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    if ("edit_color".equals(e.getActionCommand())) {
      Optional<Color> opt = this.dialog.showColorChooser(getColorForNode());

      if (opt.isPresent()) {
        setColorForNode(opt.get());
        this.dialog.setButtonColor(opt.get());
      }
    }
  }

  /**
   * @return the color for the selected option
   */
  private Color getColorForNode() {
    return this.config.getValue(ColorConfigKey.fromName(this.selectedNode));
  }

  /**
   * Sets the config color for the selected option.
   * 
   * @param c the color
   */
  private void setColorForNode(Color c) {
    this.config.setValue(ColorConfigKey.fromName(this.selectedNode), c);
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    NamedTreeNode node = (NamedTreeNode) e.getPath().getLastPathComponent();

    if (node.isLeaf()) {
      this.dialog.setColorButtonEnabled(true);
      this.selectedNode = node.getName();
      this.dialog.setButtonColor(getColorForNode());
    }
    else {
      this.dialog.setColorButtonEnabled(false);
      this.selectedNode = null;
      this.dialog.setButtonColor(Color.GRAY);
    }
  }
}
