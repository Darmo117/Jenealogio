package net.darmo_creations.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
