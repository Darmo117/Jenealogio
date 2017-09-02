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
package net.darmo_creations.jenealogio.gui.components;

import java.awt.Component;
import java.util.Optional;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.darmo_creations.jenealogio.model.family.AdoptionListEntry;
import net.darmo_creations.jenealogio.util.CalendarUtil;
import net.darmo_creations.utils.I18n;

/**
 * This list cell renderer is used to render adoption entries.
 *
 * @author Damien Vergnet
 */
public class AdoptionListRenderer extends DefaultListCellRenderer {
  private static final long serialVersionUID = 2999526543856250928L;

  @Override
  public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    if (value instanceof AdoptionListEntry) {
      AdoptionListEntry entry = (AdoptionListEntry) value;
      String line = entry.getMember().toString();
      if (entry.isAdopted()) {
        line += String.format(" (%s: %s)", I18n.toTitleCase(I18n.getLocalizedWord("adopted", entry.getMember().isWoman(), false)),
            CalendarUtil.formatDate(Optional.ofNullable(entry.getAdoptionDate())).orElse("?"));
      }

      setText(line);
    }

    return this;
  }
}
