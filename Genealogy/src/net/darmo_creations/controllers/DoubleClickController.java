package net.darmo_creations.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;

import net.darmo_creations.util.Observer;

/**
 * This controller handles double-click events.
 * 
 * @author Damien Vergnet
 */
public class DoubleClickController extends MouseAdapter {
  private Collection<Observer> observers;

  /**
   * Creates a controller that will notify the given observers. The list an me modified afterwards
   * as it is not copied.
   * 
   * @param observers the observers
   */
  public DoubleClickController(Collection<Observer> observers) {
    this.observers = observers;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
      this.observers.forEach(obs -> obs.update(null, "double-click:" + e.getComponent().getName()));
    }
  }
}
