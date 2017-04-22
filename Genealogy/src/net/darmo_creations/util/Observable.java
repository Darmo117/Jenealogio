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
 * This interface represents an observable object. It can be implemented to represent an object that
 * the application wants to have observed.
 * <p>
 * An observable object can have one or more observers. An observer may be any object that
 * implements interface <tt>Observer</tt>. After an observable instance changes, an application
 * calling the <code>Observable</code>'s <code>notifyObservers</code> method causes all of its
 * observers to be notified of the change by a call to their <code>update</code> method.
 * </p>
 * <p>
 * The order in which notifications will be delivered is unspecified. When an observable object is
 * newly created, its set of observers is empty.
 * </p>
 * 
 * @author Damien Vergnet
 */
public interface Observable {
  /**
   * Adds an observer to the set of observers for this object, provided that it is not the same as
   * some observer already in the set. The order in which notifications will be delivered to
   * multiple observers is not specified. See the class comment.
   *
   * @param observer an observer to be added.
   * @throws NullPointerException if the parameter o is null.
   */
  void addObserver(Observer observer);

  /**
   * Deletes an observer from the set of observers of this object. Passing <code>null</code> to this
   * method will have no effect.
   * 
   * @param observer the observer to be deleted.
   */
  void removeObserver(Observer observer);

  /**
   * Notifies all of its observers.
   * <p>
   * Each observer has its <code>update</code> method called with two arguments: this observable
   * object and the <code>arg</code> argument.
   * </p>
   *
   * @param o any object.
   */
  void notifyObservers(Object o);
}
