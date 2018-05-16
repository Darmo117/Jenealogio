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
package net.darmo_creations.jenealogio.gui.dialog.options;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ColorTag;
import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;

/**
 * This controller handles events from the EditColorsDialog.
 *
 * @author Damien Vergnet
 */
class EditColorController extends DefaultDialogController<EditColorsDialog> implements TreeSelectionListener {
  private WritableConfig config;
  private String selectedNode;

  EditColorController(EditColorsDialog dialog) {
    super(dialog);
    this.config = null;
    this.selectedNode = null;
  }

  /**
   * @return the current config
   */
  WritableConfig getConfig() {
    return this.config;
  }

  /**
   * Sets the config. Will also update the dialog.
   * 
   * @param config the new config
   */
  void setConfig(WritableConfig config) {
    this.dialog.setCancelled(false);
    this.config = config;
    if (this.selectedNode != null)
      this.dialog.setButtonColor(getColorForNode());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    switch (e.getActionCommand()) {
      case "edit_color":
        Optional<Color> opt = this.dialog.showColorChooser(getColorForNode());

        if (opt.isPresent()) {
          setColorForNode(opt.get());
          this.dialog.setButtonColor(opt.get());
        }
        break;
      case "default_color":
        Optional<ColorTag> tag = WritableConfig.getTagFromName(this.selectedNode, Color.class.getName());

        if (tag.isPresent()) {
          Color color = WritableConfig.getDefaultValue(tag.get());
          setColorForNode(color);
          this.dialog.setButtonColor(color);
        }
        break;
    }
  }

  /**
   * @return the color for the selected option
   */
  private Color getColorForNode() {
    Optional<ColorTag> key = WritableConfig.getTagFromName(this.selectedNode, Color.class.getName());
    if (key.isPresent())
      return this.config.getValue(key.get());
    return null;
  }

  /**
   * Sets the config color for the selected option.
   * 
   * @param c the color
   */
  private void setColorForNode(Color c) {
    Optional<ColorTag> key = WritableConfig.getTagFromName(this.selectedNode, Color.class.getName());
    if (key.isPresent())
      this.config.setValue(key.get(), c);
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
