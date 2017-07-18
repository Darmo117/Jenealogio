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
package net.darmo_creations.gui.dialog.card;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.darmo_creations.model.family.Gender;
import net.darmo_creations.util.I18n;

/**
 * This class encapsulate the default JComboBox renderer to fix the displayed gender. It is used in
 * the CardDialog class.
 *
 * @author Damien Vergnet
 */
class GenderComboRenderer implements ListCellRenderer<Gender> {
  /** The default renderer */
  private final ListCellRenderer<? super Gender> delegate;

  GenderComboRenderer(ListCellRenderer<? super Gender> delegate) {
    this.delegate = delegate;
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Gender> list, Gender value, int index, boolean isSelected,
      boolean cellHasFocus) {
    Component component = this.delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (component instanceof JLabel) {
      String text = I18n.getLocalizedString(value.getUnlocalizedName());
      ((JLabel) component).setText(text.substring(0, 1).toUpperCase() + text.substring(1));
    }
    return component;
  }
}
