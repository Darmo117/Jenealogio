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

import java.util.Objects;

import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.events.AbstractEvent;

/**
 * This event is fired when the active selection inside a view changes.
 *
 * @author Damien Vergnet
 */
public class SelectionChangeEvent extends AbstractEvent {
  private final Selection lastSelection, newSelection;

  public SelectionChangeEvent(Selection lastSelection, Selection newSelection) {
    super(false);
    this.lastSelection = Objects.requireNonNull(lastSelection);
    this.newSelection = Objects.requireNonNull(newSelection);
  }

  public Selection getLastSelection() {
    return this.lastSelection;
  }

  public Selection getNewSelection() {
    return this.newSelection;
  }
}
