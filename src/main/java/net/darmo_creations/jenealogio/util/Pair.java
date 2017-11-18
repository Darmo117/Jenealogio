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
package net.darmo_creations.jenealogio.util;

import java.util.Objects;

/**
 * A simple class representing a pair of values.
 * 
 * @author Damien Vergnet
 *
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {
  private final T v1;
  private final U v2;

  public Pair(T v1, U v2) {
    this.v1 = Objects.requireNonNull(v1);
    this.v2 = Objects.requireNonNull(v2);
  }

  public T getValue1() {
    return this.v1;
  }

  public U getValue2() {
    return this.v2;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((this.v1 == null) ? 0 : this.v1.hashCode());
    result = prime * result + ((this.v2 == null) ? 0 : this.v2.hashCode());

    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o instanceof Pair) {
      Pair<T, U> p = (Pair<T, U>) o;
      return this.v1.equals(p.getValue1()) && this.v2.equals(p.getValue2());
    }
    return false;
  }

  @Override
  public String toString() {
    return "<" + this.v1 + ", " + this.v2 + ">";
  }
}
