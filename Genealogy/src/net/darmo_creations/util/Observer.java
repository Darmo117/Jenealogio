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
package net.darmo_creations.util;

/**
 * A class can implement the <code>Observer</code> interface when it wants to be informed of changes
 * in observable objects.
 *
 * @author Damien Vergnet
 */
public interface Observer {
  /**
   * This method is called whenever the observed object is changed. An application calls an
   * <tt>Observable</tt> object's <code>notifyObservers</code> method to have all the object's
   * observers notified of the change.
   *
   * @param obs the observable object.
   * @param o an argument passed to the <code>notifyObservers</code> method.
   */
  void update(Observable obs, Object o);
}
