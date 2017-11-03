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

import java.awt.Rectangle;

/**
 * Base class for all card resizing events.
 *
 * @author Damien Vergnet
 */
public abstract class CardResizeEvent extends CardEvent {
  /**
   * Creates an event.
   * 
   * @param memberId target membr ID
   */
  public CardResizeEvent(long memberId) {
    super(memberId);
  }

  /**
   * This event is fired before a card is resized.
   *
   * @author Damien Vergnet
   */
  public static class Pre extends CardResizeEvent {
    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     */
    public Pre(long memberId) {
      super(memberId);
    }
  }

  /**
   * This event is fired after a card has been resized.
   *
   * @author Damien Vergnet
   */
  public static class Post extends CardResizeEvent {
    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     */
    public Post(long memberId) {
      super(memberId);
    }
  }

  /**
   * This event is fired when a card is being resized.
   *
   * @author Damien Vergnet
   */
  public static class Resizing extends CardResizeEvent {
    private final Rectangle oldSize, newSize;

    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     * @param oldSize card's old size
     * @param newSize card's new size
     */
    public Resizing(long memberId, Rectangle oldSize, Rectangle newSize) {
      super(memberId);
      this.oldSize = oldSize;
      this.newSize = newSize;
    }

    /**
     * @return card's old size
     */
    public Rectangle getOldSize() {
      return this.oldSize;
    }

    /**
     * @return card's new size
     */
    public Rectangle getNewSize() {
      return this.newSize;
    }
  }
}
