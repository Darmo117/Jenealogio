package net.darmo_creations.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.darmo_creations.gui.dialog.AbstractDialog;

public abstract class AbstractDialogController<T extends AbstractDialog> extends WindowAdapter implements ActionListener {
  protected T dialog;

  public AbstractDialogController(T dialog) {
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
