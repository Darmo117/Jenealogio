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
package net.darmo_creations.gui.drag_and_drop;

import java.awt.Component;
import java.io.File;
import java.util.List;

/**
 * Any class that would want to handle files drag-and-drop should implement this class.
 *
 * @author Damien Vergnet
 */
public interface DropHandler {
  /**
   * Accepts or rejects the given list of files.
   * 
   * @param files the files
   * @param c the component that first fired the event
   * @return true if ALL files in the list are accepted; false otherwise
   */
  boolean acceptFiles(List<File> files, Component c);

  /**
   * This method imports the files once they have been accepted.
   * 
   * @param files the files to import
   */
  void importFiles(List<File> files);
}
