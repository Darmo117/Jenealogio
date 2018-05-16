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
 * This event is fired when a card is double-clicked.
 *
 * @author Damien Vergnet
 */
public final class CardDoubleClickEvent extends AbstractEvent {
  private final long memberId;

  /**
   * Creates an event.
   * 
   * @param memberId target member ID
   */
  public CardDoubleClickEvent(long memberId) {
    super(false);
    this.memberId = memberId;
  }

  /**
   * @return ID of the concerned member
   */
  public final long getMemberId() {
    return this.memberId;
  }
}
