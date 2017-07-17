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

import java.awt.Point;

/**
 * Base class for all events related to a single card.
 *
 * @author Damien Vergnet
 */
public abstract class CardEvent extends AbstractEvent {
  private final long memberId;

  /**
   * Creates an event.
   * 
   * @param memberId target member ID
   */
  protected CardEvent(long memberId) {
    this.memberId = memberId;
  }

  /**
   * @return ID of the concerned member
   */
  public final long getMemberId() {
    return this.memberId;
  }

  /**
   * This event is fired when a card is clicked.
   *
   * @author Damien Vergnet
   */
  public static class Clicked extends CardEvent {
    private final boolean keepPreviousSelection;

    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     * @param keepPreviousSelection tells if the previous selection should be kept
     */
    public Clicked(long memberId, boolean keepPreviousSelection) {
      super(memberId);
      this.keepPreviousSelection = keepPreviousSelection;
    }

    /**
     * @return true if the previous selection should be kept
     */
    public boolean keepPreviousSelection() {
      return this.keepPreviousSelection;
    }
  }

  /**
   * This event is fired when a card is double-clicked.
   *
   * @author Damien Vergnet
   */
  public static class DoubleClicked extends CardEvent {
    public DoubleClicked(long memberId) {
      super(memberId);
    }
  }

  /**
   * This event is fired when a card is going to be edited.
   *
   * @author Damien Vergnet
   */
  public static class Edit extends CardEvent {
    public Edit(long memberId) {
      super(memberId);
    }
  }

  /**
   * This event is fired when a card is dragged.
   *
   * @author Damien Vergnet
   */
  public static class Dragged extends CardEvent {
    private final Point oldLocation, newLocation;

    /**
     * Creates an event.
     * 
     * @param memberId target member ID
     * @param oldLocation card's old location
     * @param newLocation card's new location
     */
    public Dragged(long memberId, Point oldLocation, Point newLocation) {
      super(memberId);
      this.oldLocation = (Point) oldLocation.clone();
      this.newLocation = (Point) newLocation.clone();
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
  }
}
