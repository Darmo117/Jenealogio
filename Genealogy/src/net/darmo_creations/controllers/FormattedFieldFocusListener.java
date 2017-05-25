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
package net.darmo_creations.controllers;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JFormattedTextField;

/**
 * This focus listener lets JFormattedTextFields accept blank values.
 *
 * @author Damien Vergnet
 */
public class FormattedFieldFocusListener extends FocusAdapter {
  private JFormattedTextField field;

  /**
   * Creates a listener.
   * 
   * @param field the field to listen to
   */
  public FormattedFieldFocusListener(JFormattedTextField field) {
    this.field = field;
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (this.field.getFocusLostBehavior() == JFormattedTextField.PERSIST)
      this.field.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
  }

  @Override
  public void focusLost(FocusEvent e) {
    if ("".equals(this.field.getText()))
      this.field.setFocusLostBehavior(JFormattedTextField.PERSIST);
  }
}
