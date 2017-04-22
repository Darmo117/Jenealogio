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
