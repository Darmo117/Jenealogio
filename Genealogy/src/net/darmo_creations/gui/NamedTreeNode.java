package net.darmo_creations.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class NamedTreeNode extends DefaultMutableTreeNode {
  private static final long serialVersionUID = 3966366083168093356L;

  private String name;

  public NamedTreeNode(String name) {
    this(name, null);
  }

  public NamedTreeNode(String name, Object userObject) {
    this(name, userObject, true);
  }

  public NamedTreeNode(String name, Object userObject, boolean allowsChildren) {
    super(userObject, allowsChildren);
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
