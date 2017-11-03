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

import java.awt.Point;

/**
 * Base class for all card draggin events.
 *
 * @author Damien Vergnet
 */
public abstract class CardDragEvent extends CardEvent {
  /**
   * Creates an event.
   * 
   * @param memberId target member ID
   */
  public CardDragEvent(long memberId) {
    super(memberId);
  }

  /**
   * This event is fired before a card is dragged.
   *
   * @author Damien Vergnet
   */
  public static class Pre extends CardDragEvent {
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
   * This event is fired after a card has been dragged.
   *
   * @author Damien Vergnet
   */
  public static class Post extends CardDragEvent {
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
   * This event is fired when a card is being dragged.
   *
   * @author Damien Vergnet
   */
  public static class Dragging extends CardDragEvent {
    private final Point oldLocation, newLocation, mouseLocation;

    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     * @param oldLocation card's old location
     * @param newLocation card's new location
     * @param mouseLocation mouse's location in the parent container
     */
    public Dragging(long memberId, Point oldLocation, Point newLocation, Point mouseLocation) {
      super(memberId);
      this.oldLocation = (Point) oldLocation.clone();
      this.newLocation = (Point) newLocation.clone();
      this.mouseLocation = (Point) mouseLocation.clone();
    }

    /**
     * @return card's old location
     */
    public Point getOldLocation() {
      return (Point) this.oldLocation.clone();
    }

    /**
     * @return card's new location
     */
    public Point getNewLocation() {
      return (Point) this.newLocation.clone();
    }

    /**
     * @return translation amount
     */
    public Point getTranslation() {
      return new Point(this.newLocation.x - this.oldLocation.x, this.newLocation.y - this.oldLocation.y);
    }

    /**
     * @return mouse's location in the parent container
     */
    public Point getMouseLocation() {
      return this.mouseLocation;
    }
  }
}
