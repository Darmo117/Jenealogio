package net.darmo_creations.gui.dialog;

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

import net.darmo_creations.util.I18n;

public abstract class AbstractDialog extends JDialog {
  private static final long serialVersionUID = -7155586918339699837L;

  private JPanel buttonsPnl;
  private JButton validationBtn, cancelBtn, closeBtn;
  private List<AbstractButton> additionnalBtns;
  private boolean canceled;

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

  public void addButton(AbstractButton button) {
    this.buttonsPnl.add(button);
    this.additionnalBtns.add(button);
  }

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

  public static enum Mode {
    CLOSE_OPTION,
    VALIDATE_CANCEL_OPTION;
  }
}
