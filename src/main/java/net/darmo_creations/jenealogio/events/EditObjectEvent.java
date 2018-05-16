/*
 * Copyright © 2018 Damien Vergnet
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
 * Base class for object editing events.
 *
 * @author Damien Vergnet
 */
public abstract class EditObjectEvent extends AbstractEvent {
  public EditObjectEvent() {
    super(false);
  }

  /**
   * This event is fired to edit a specific member.
   *
   * @author Damien Vergnet
   */
  public static final class Member extends EditObjectEvent {
    private final long id;

    /**
     * Creates an event.
     * 
     * @param id member's ID
     */
    public Member(long id) {
      this.id = id;
    }

    /**
     * Returns member's ID.
     */
    public long getId() {
      return this.id;
    }
  }

  /**
   * This event is fired to edit a specific relation.
   *
   * @author Damien Vergnet
   */
  public static final class Relation extends EditObjectEvent {
    private final long partner1, partner2;

    /**
     * Creates an event.
     * 
     * @param partner1 first partner's ID
     * @param partner2 second partner's ID
     */
    public Relation(long partner1, long partner2) {
      this.partner1 = partner1;
      this.partner2 = partner2;
    }

    /**
     * Returns the ID of the first partner.
     */
    public long getPartner1() {
      return this.partner1;
    }

    /**
     * Returns the ID of the second partner.
     */
    public long getPartner2() {
      return this.partner2;
    }
  }
}
