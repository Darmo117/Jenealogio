package net.darmo_creations.jenealogio.gui.components.view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JLabel;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.FocusChangeEvent;
import net.darmo_creations.jenealogio.model.ViewType;

public class ViewController extends MouseAdapter implements FocusListener {
  protected ViewType type;
  protected View view;

  public ViewController(ViewType type) {
    this.type = Objects.requireNonNull(type);
  }

  public void setView(View view) {
    this.view = view;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getSource() instanceof JLabel && !this.view.hasFocus())
      this.view.requestFocus();
  }

  @Override
  public void focusGained(FocusEvent e) {
    this.view.setTopLabelFocused(true);
    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new FocusChangeEvent(this.type));
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.view.setTopLabelFocused(false);
  }
}
