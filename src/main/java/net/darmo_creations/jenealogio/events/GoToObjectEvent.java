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
package net.darmo_creations.jenealogio.events;

import net.darmo_creations.utils.events.AbstractEvent;

/**
 * This event is fired when the user clicks the "Go to" menu from the side view.
 *
 * @author Damien Vergnet
 */
public class GoToObjectEvent extends AbstractEvent {
  private final Object object;

  public GoToObjectEvent(Object object) {
    this.object = object;
  }

  public Object getObject() {
    return this.object;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }
}
