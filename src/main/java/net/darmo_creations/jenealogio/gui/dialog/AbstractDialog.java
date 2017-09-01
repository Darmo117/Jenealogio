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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import net.darmo_creations.jenealogio.util.I18n;

/**
 * This class is a template for dialog windows. It provides default buttons and behaviour.
 * 
 * @author Damien Vergnet
 */
public abstract class AbstractDialog extends JDialog {
  private static final long serialVersionUID = -7155586918339699837L;

  private JPanel buttonsPnl;
  private JButton validationBtn, cancelBtn, closeBtn;
  private List<AbstractButton> additionnalBtns;
  private boolean canceled;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner window
   * @param mode the buttons mode
   * @param modal is it modal?
   * @see Mode
   */
  public AbstractDialog(JFrame owner, Mode mode, boolean modal) {
    super(owner, modal);

    this.buttonsPnl = new JPanel();
    this.additionnalBtns = new ArrayList<>();

    if (mode == Mode.VALIDATE_CANCEL_OPTION) {
      this.validationBtn = new JButton(I18n.getLocalizedString("button.validate.text"));
      this.validationBtn.setActionCommand("validate");
      this.validationBtn.setFocusPainted(false);
      this.cancelBtn = new JButton(I18n.getLocalizedString("button.cancel.text"));
      this.cancelBtn.setActionCommand("cancel");
      this.cancelBtn.setFocusPainted(false);

      this.buttonsPnl.add(this.validationBtn);
      this.buttonsPnl.add(this.cancelBtn);
    }
    else if (mode == Mode.CLOSE_OPTION) {
      this.closeBtn = new JButton(I18n.getLocalizedString("button.close.text"));
      this.closeBtn.setActionCommand("close");
      this.closeBtn.setFocusPainted(false);

      this.buttonsPnl.add(this.closeBtn);
    }

    add(this.buttonsPnl, BorderLayout.SOUTH);

    setCanceled(false);
    installEscapeCloseOperation();
  }

  /**
   * Adds a button to the bottom panel, to the right of the existing ones.
   * 
   * @param button the button to add
   */
  public void addButton(AbstractButton button) {
    this.buttonsPnl.add(button);
    this.additionnalBtns.add(button);
  }

  /**
   * Sets the action listener. Will replace the previous one.
   * 
   * @param controller the new controller
   */
  public void setActionListener(DefaultDialogController<?> controller) {
    if (this.validationBtn != null)
      this.validationBtn.addActionListener(controller);
    if (this.cancelBtn != null)
      this.cancelBtn.addActionListener(controller);
    if (this.closeBtn != null)
      this.closeBtn.addActionListener(controller);
    this.additionnalBtns.forEach(b -> b.addActionListener(controller));
    addWindowListener(controller);
  }

  /**
   * Enables/disables the "Validate" button.
   * 
   * @param enabled
   */
  public void setValidateButtonEnabled(boolean enabled) {
    if (this.validationBtn != null) {
      this.validationBtn.setEnabled(enabled);
    }
  }

  /**
   * @return true if the user clicked "Cancel"; false otherwise
   */
  public boolean isCanceled() {
    return this.canceled;
  }

  /**
   * Sets the dialog to cancelled or not.
   * 
   * @param canceled
   */
  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  /**
   * Installs the escape close event.
   */
  private void installEscapeCloseOperation() {
    final String actionKey = "WINDOW_CLOSING";
    JRootPane root = getRootPane();

    root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), actionKey);
    root.getActionMap().put(actionKey, new AbstractAction() {
      private static final long serialVersionUID = -2682313879254943757L;

      @Override
      public void actionPerformed(ActionEvent event) {
        dispatchEvent(new WindowEvent(AbstractDialog.this, WindowEvent.WINDOW_CLOSING));
      }
    });
  }

  /**
   * Buttons modes.
   *
   * @author Damien Vergnet
   */
  public static enum Mode {
    /** One close button only. */
    CLOSE_OPTION,
    /** Two buttons: Validate and Cancel. */
    VALIDATE_CANCEL_OPTION;
  }
}
