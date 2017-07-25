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
package net.darmo_creations.controllers;

/**
 * This file filter is used by JFileChooser objects to filter one file type only.
 *
 * @author Damien Vergnet
 */
public class OneExtensionFileFilter extends ExtensionsFileFilter {
  /**
   * Creates a file filter.
   * 
   * @param fileType the description of the file type.
   * @param extensions the extension without the '.'
   */
  public OneExtensionFileFilter(String fileType, String extension) {
    super(fileType, extension);
  }

  /**
   * @return the extension accepted by this filter
   */
  public String getExtension() {
    return getExtensions().get(0);
  }
}
