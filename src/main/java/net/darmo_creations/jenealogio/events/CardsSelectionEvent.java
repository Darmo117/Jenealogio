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

import java.util.Arrays;

/**
 * This event is fired when cards have been selected.
 *
 * @author Damien Vergnet
 */
public final class CardsSelectionEvent extends AbstractEvent {
  private final long[] ids;

  /**
   * Creates an event.
   * 
   * @param ids IDs of all selected cards
   */
  public CardsSelectionEvent(long... ids) {
    this.ids = Arrays.copyOf(ids, ids.length);
  }

  /**
   * @return IDs of all selected cards
   */
  public long[] getSelectedPanelsIds() {
    return Arrays.copyOf(this.ids, this.ids.length);
  }
}
