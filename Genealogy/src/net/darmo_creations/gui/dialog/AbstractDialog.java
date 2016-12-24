package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import net.darmo_creations.controller.AbstractDialogController;

public abstract class AbstractDialog extends JDialog {
  private static final long serialVersionUID = -7155586918339699837L;

  private JButton validationBtn, cancelBtn, closeBtn;
  private boolean canceled;

  public AbstractDialog(JFrame owner, Mode mode, boolean modal) {
    super(owner, modal);

    JPanel buttonsPnl = new JPanel();

    if (mode == Mode.VALIDATE_CANCEL_OPTION) {
      this.validationBtn = new JButton("Valider");
      this.validationBtn.setActionCommand("validate");
      this.validationBtn.setFocusPainted(false);
      this.cancelBtn = new JButton("Annuler");
      this.cancelBtn.setActionCommand("cancel");
      this.cancelBtn.setFocusPainted(false);

      buttonsPnl.add(this.validationBtn);
      buttonsPnl.add(this.cancelBtn);
    }
    else if (mode == Mode.CLOSE_OPTION) {
      this.closeBtn = new JButton("Fermer");
      this.closeBtn.setActionCommand("close");
      this.closeBtn.setFocusPainted(false);

      buttonsPnl.add(this.closeBtn);
    }

    add(buttonsPnl, BorderLayout.SOUTH);

    setCanceled(false);
    installEscapeCloseOperation();
  }

  public void setActionListener(AbstractDialogController<?> controller) {
    if (this.validationBtn != null)
      this.validationBtn.addActionListener(controller);
    if (this.cancelBtn != null)
      this.cancelBtn.addActionListener(controller);
    if (this.closeBtn != null)
      this.closeBtn.addActionListener(controller);
    addWindowListener(controller);
  }

  public void setValidateButtonEnabled(boolean enabled) {
    if (this.validationBtn != null) {
      this.validationBtn.setEnabled(enabled);
    }
  }

  public boolean isCanceled() {
    return this.canceled;
  }

  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }

  private void installEscapeCloseOperation() {
    String actionKey = "WINDOW_CLOSING";
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

  public static enum Mode {
    CLOSE_OPTION,
    VALIDATE_CANCEL_OPTION;
  }
}
