package net.darmo_creations.jenealogio.gui.components.view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.FocusChangeEvent;
import net.darmo_creations.jenealogio.model.ViewType;

public class ViewController<T extends View<?>> extends MouseAdapter implements FocusListener {
  private ViewType type;
  private T view;

  public ViewController(ViewType type) {
    this.type = Objects.requireNonNull(type);
  }

  public T getView() {
    return this.view;
  }

  public void setView(T view) {
    this.view = view;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    System.out.println(e); // DEBUG
    if (!this.view.hasFocus())
      this.view.requestFocus();
  }

  @Override
  public void focusGained(FocusEvent e) {
    System.out.println(this.type); // DEBUG
    this.view.setTopFocused(true);
    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new FocusChangeEvent(this.type));
  }

  @Override
  public void focusLost(FocusEvent e) {
    this.view.setTopFocused(false);
  }
}
