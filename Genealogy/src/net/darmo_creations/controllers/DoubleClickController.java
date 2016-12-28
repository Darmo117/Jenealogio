package net.darmo_creations.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.darmo_creations.model.Observer;

public class DoubleClickController extends MouseAdapter {
  private List<Observer> observers;
  private long id;

  public DoubleClickController(long id, Collection<Observer> observers) {
    this.id = id;
    this.observers = new ArrayList<>(observers);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      this.observers.forEach(obs -> obs.update("double-click:" + this.id));
    }
  }
}
