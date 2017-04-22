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
package net.darmo_creations.gui.components;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class adds names to <code>DefaultMutableTreeNode</code>s.
 *
 * @author Damien Vergnet
 * @see DefaultMutableTreeNode
 */
public class NamedTreeNode extends DefaultMutableTreeNode {
  private static final long serialVersionUID = 3966366083168093356L;

  private String name;

  /**
   * Creates a tree node that has no parent and no children, but which allows children, and
   * initializes it with the specified name.
   *
   * @param name the node's name
   */
  public NamedTreeNode(String name) {
    this(name, null);
  }

  /**
   * Creates a tree node with no parent, no children, but which allows children, and initializes it
   * with the specified user object and name.
   *
   * @param name the node's name
   * @param userObject an Object provided by the user that constitutes the node's data
   */
  public NamedTreeNode(String name, Object userObject) {
    this(name, userObject, true);
  }

  /**
   * Creates a tree node with no parent, no children, initialized with the specified user object and
   * name, and that allows children only if specified.
   *
   * @param name the node's name
   * @param userObject an Object provided by the user that constitutes the node's data
   * @param allowsChildren if true, the node is allowed to have child nodes -- otherwise, it is
   *          always a leaf node
   */
  public NamedTreeNode(String name, Object userObject, boolean allowsChildren) {
    super(userObject, allowsChildren);
    this.name = name;
  }

  /**
   * @return node's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets node's name.
   * 
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }
}
