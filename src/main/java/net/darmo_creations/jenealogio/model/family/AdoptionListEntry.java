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
package net.darmo_creations.jenealogio.model.family;

import net.darmo_creations.jenealogio.model.date.Date;

/**
 * This class is used to store children items with adoption data in JLists.
 *
 * @author Damien Vergnet
 */
public class AdoptionListEntry {
  private final FamilyMember member;
  private boolean adopted;
  private Date adoptionDate;

  public AdoptionListEntry(FamilyMember member, boolean adopted, Date adoptionDate) {
    this.member = member;
    this.adopted = adopted;
    this.adoptionDate = adoptionDate;
  }

  public FamilyMember getMember() {
    return this.member;
  }

  public boolean isAdopted() {
    return this.adopted;
  }

  public void setAdopted(boolean adopted) {
    this.adopted = adopted;
  }

  public Date getAdoptionDate() {
    return this.adoptionDate;
  }

  public void setAdoptionDate(Date adoptionDate) {
    this.adoptionDate = adoptionDate;
  }
}
