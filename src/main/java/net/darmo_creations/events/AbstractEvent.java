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
package net.darmo_creations.events;

/**
 * Base class for all internal events.
 *
 * @author Damien Vergnet
 */
public abstract class AbstractEvent {
  private boolean canceled;

  protected AbstractEvent() {
    this.canceled = false;
  }

  /**
   * @return true if this event can be canceled
   */
  public boolean isCancelable() {
    return true;
  }

  /**
   * @return true if this event has been canceled
   */
  public final boolean isCanceled() {
    return this.canceled;
  }

  /**
   * Cancels this event. If this event is not cancelable, an {@link IllegalStateException} will be
   * thrown.
   * 
   * @throws IllegalStateException if this event is not cancelable
   */
  public final void setCanceled() throws IllegalStateException {
    if (!isCancelable())
      throw new IllegalStateException("cannot cancel non-cancelable event " + getClass().getSimpleName());
    this.canceled = true;
  }
}
