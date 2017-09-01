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

/**
 * This event is fired when a menu item/button is clicked. This event can be cancelled.
 * 
 * @author Damien Vergnet
 */
public final class UserEvent extends AbstractEvent {
  private final Type type;

  /**
   * Creates an event.
   * 
   * @param type the type
   */
  public UserEvent(Type type) {
    this.type = type;
  }

  /**
   * @return the type
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Event type.
   *
   * @author Damien Vergnet
   */
  public static enum Type {
    NEW,
    EDIT_TREE,
    OPEN,
    SAVE,
    SAVE_AS,
    UNDO,
    REDO,
    ADD_CARD,
    ADD_LINK,
    EDIT_CARD,
    EDIT_LINK,
    DELETE_CARD,
    DELETE_LINK,
    EDIT_COLORS,
    HELP,
    ABOUT,
    EXIT,
    OPEN_UPDATE,
    TOGGLE_CHECK_UPDATES,
    EXPORT_IMAGE;
  }
}
