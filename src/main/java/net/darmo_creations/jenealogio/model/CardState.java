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
package net.darmo_creations.jenealogio.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

/**
 * This class represents the state of a card in the canvas.
 *
 * @author Damien Vergnet
 */
public final class CardState {
  private final Point location;
  private final Dimension size;
  private final Color bgColor;

  public CardState(Point location, Dimension size, Color bgColor) {
    this.location = location;
    this.size = size;
    this.bgColor = bgColor;
  }

  public Point getLocation() {
    return this.location;
  }

  public Dimension getSize() {
    return this.size;
  }

  public Color getBgColor() {
    return this.bgColor;
  }
}
