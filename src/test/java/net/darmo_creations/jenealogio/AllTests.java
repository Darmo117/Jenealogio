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
package net.darmo_creations.jenealogio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.darmo_creations.jenealogio.config.ColorTagTest;
import net.darmo_creations.jenealogio.model.CardStateTest;
import net.darmo_creations.jenealogio.model.FamilyEditTest;
import net.darmo_creations.jenealogio.model.date.CalendarUtilTest;
import net.darmo_creations.jenealogio.model.date.DateBuilderTest;
import net.darmo_creations.jenealogio.model.family.AdoptionListEntryTest;
import net.darmo_creations.jenealogio.model.family.FamilyMemberTest;
import net.darmo_creations.jenealogio.model.family.FamilyTest;
import net.darmo_creations.jenealogio.model.family.GenderTest;
import net.darmo_creations.jenealogio.model.family.RelationshipTest;
import net.darmo_creations.jenealogio.util.ImagesTest;
import net.darmo_creations.jenealogio.util.PairTest;
import net.darmo_creations.jenealogio.util.SelectionTest;
import net.darmo_creations.jenealogio.util.StringUtilTest;

@RunWith(Suite.class)
@SuiteClasses({FamilyMemberTest.class, CalendarUtilTest.class, ImagesTest.class, PairTest.class, SelectionTest.class, ColorTagTest.class,
  DateBuilderTest.class, GenderTest.class, StringUtilTest.class, RelationshipTest.class, AdoptionListEntryTest.class, CardStateTest.class,
  FamilyEditTest.class, FamilyTest.class})
public class AllTests {}
