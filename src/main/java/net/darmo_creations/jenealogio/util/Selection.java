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

import java.util.Collections;
import java.util.List;

/**
 * This class represents the active selection in a view.
 *
 * @author Damien Vergnet
 */
public class Selection {
  private final List<Long> members;
  private final List<Pair<Long, Long>> relations;

  public Selection(List<Long> members, List<Pair<Long, Long>> relations) {
    this.members = Collections.unmodifiableList(members);
    this.relations = Collections.unmodifiableList(relations);
  }

  /**
   * Tells if this selection is empty.
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the number of objects in this selection.
   */
  public int size() {
    return this.members.size() + this.relations.size();
  }

  public List<Long> getMembers() {
    return this.members;
  }

  public List<Pair<Long, Long>> getRelations() {
    return this.relations;
  }

  @Override
  public String toString() {
    return "Selection [members=" + this.members + ",relations=" + this.relations + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + ((this.members == null) ? 0 : this.members.hashCode());
    result = prime * result + ((this.relations == null) ? 0 : this.relations.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Selection) {
      Selection s = (Selection) o;
      return s.members.equals(this.members) && s.relations.equals(this.relations);
    }
    return false;
  }
}
