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
package net.darmo_creations.jenealogio.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This controller gives a default behaviour to the AbstractDialog window.
 * 
 * @author Damien Vergnet
 *
 * @param <T> the dialog type
 */
public class DefaultDialogController<T extends AbstractDialog> extends WindowAdapter implements ActionListener {
  protected T dialog;

  public DefaultDialogController(T dialog) {
    this.dialog = dialog;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "cancel":
        this.dialog.setCanceled(true);
      case "close":
      case "validate":
        this.dialog.setVisible(false);
        break;
    }
  }

  @Override
  public void windowClosing(WindowEvent e) {
    this.dialog.setCanceled(true);
  }
}
