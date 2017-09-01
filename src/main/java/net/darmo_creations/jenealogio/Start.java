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
package net.darmo_creations.jenealogio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.GuiFramework;
import net.darmo_creations.gui_framework.config.Language;

/**
 * Application's main class.
 *
 * @author Damien Vergnet
 */
public class Start {
  public static void main(String[] args) {
    List<Language> l = new ArrayList<>();
    l.add(new Language("English", Locale.US));
    l.add(new Language("Français", Locale.FRANCE));
    l.add(new Language("Esperanto", new Locale("eo")));

    ApplicationRegistry.setLanguages(l);
    ApplicationRegistry.registerApplication(Jenealogio.class);
    GuiFramework.run();
  }
}
