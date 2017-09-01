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
package net.darmo_creations.jenealogio.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * This file filter is used by JFileChooser objects to filter several file types.
 * 
 * @author Damien Vergnet
 */
public class ExtensionsFileFilter extends FileFilter {
  private List<String> extensions;
  private String description;

  /**
   * Creates a file filter.
   * 
   * @param fileType the description of the file type.
   * @param extensions the extensions without the '.'
   */
  public ExtensionsFileFilter(String fileType, String... extensions) {
    this.extensions = Arrays.asList(extensions);
    String[] exts = this.extensions.stream().map(ext -> "*." + ext).toArray(String[]::new);
    this.description = fileType + " (" + String.join(", ", exts) + ")";
  }

  @Override
  public boolean accept(File f) {
    return f.isDirectory() || hasValidExtension(f);
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * @return the list of all extensions accepted by this filter
   */
  public List<String> getExtensions() {
    return new ArrayList<>(this.extensions);
  }

  /**
   * Checks if the given file has a valid extension.
   * 
   * @param f the file
   * @return true if and only if the file has a valid extension
   */
  private boolean hasValidExtension(File f) {
    return this.extensions.stream().filter(ext -> f.getName().toLowerCase().endsWith("." + ext)).findAny().isPresent();
  }
}
