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
package net.darmo_creations.jenealogio.gui.components.side_view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.jenealogio.gui.components.view.View;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;

public class SideView extends View {
  private static final long serialVersionUID = -1739589891038892474L;

  private JTree tree;
  private DefaultTreeModel treeModel;
  private NamedTreeNode membersNode, relationsNode;
  private JPopupMenu popupMenu;

  private Family family;

  public SideView() {
    super(I18n.getLocalizedString("label.tree_explorer.text"), new SideViewController());

    JButton b = new JButton(Images.MINUS);
    b.setPreferredSize(new Dimension(20, 20));
    b.setFocusable(false);
    b.setToolTipText(I18n.getLocalizedString("button.collapse_all.tooltip"));
    b.addActionListener(e -> {
      int row = this.tree.getRowCount() - 1;
      while (row >= 0) {
        this.tree.collapseRow(row--);
      }
    });
    addButton(b);

    NamedTreeNode root = new NamedTreeNode("root", "Root");
    this.membersNode = new NamedTreeNode("members", I18n.getLocalizedString("node.members.text"));
    this.relationsNode = new NamedTreeNode("relations", I18n.getLocalizedString("node.relations.text"));
    root.add(this.membersNode);
    root.add(this.relationsNode);

    this.treeModel = new DefaultTreeModel(root);
    this.tree = new JTree(this.treeModel);
    this.tree.setRootVisible(false);
    this.tree.setShowsRootHandles(true);
    this.tree.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.tree.addMouseListener(getController());
    this.tree.addTreeSelectionListener(getController());
    this.tree.setLargeModel(true);
    this.tree.setCellRenderer(new DefaultTreeCellRenderer() {
      private static final long serialVersionUID = -489015910861060922L;

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
          boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        NamedTreeNode node = (NamedTreeNode) value;
        Object o = node.getUserObject();

        if (node.getName().equals("members")) {
          setIcon(Images.GROUP);
        }
        else if (node.getName().equals("relations")) {
          setIcon(Images.GROUP_LINK);
        }
        else if (o instanceof FamilyMember) {
          FamilyMember m = (FamilyMember) o;
          if (m.isMan())
            setIcon(Images.MALE_SYMBOL);
          else if (m.isWoman())
            setIcon(Images.FEMALE_SYMBOL);
        }
        else if (o instanceof Relationship) {
          Relationship r = (Relationship) o;
          String name1 = SideView.this.family.getMember(r.getPartner1()).orElseThrow(IllegalStateException::new).toString();
          String name2 = SideView.this.family.getMember(r.getPartner2()).orElseThrow(IllegalStateException::new).toString();

          setText(name1 + " / " + name2);
          if (r.hasEnded())
            setIcon(Images.LINK_BROKEN);
          else
            setIcon(Images.LINK);
        }
        else if (leaf) {
          setIcon(null);
        }

        return c;
      }
    });
    this.tree.addFocusListener(this.controller);

    setViewport(this.tree);

    initMenu();
  }

  private void initMenu() {
    this.popupMenu = new JPopupMenu();
    JMenuItem i;
    this.popupMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.go_to.text")));
    i.setName("go_to");
    i.setEnabled(false);
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
    this.popupMenu.add(i = new JMenuItem(I18n.getLocalizedString("item.delete.text")));
    i.setName("delete");
    i.setEnabled(false);
    i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
  }

  public void refresh(Family family) {
    this.family = family;
    this.membersNode.removeAllChildren();
    this.relationsNode.removeAllChildren();

    List<FamilyMember> members = new ArrayList<>(family.getAllMembers());
    members.sort((m1, m2) -> {
      String name1 = m1.getFamilyName().orElse("?");
      String name2 = m2.getFamilyName().orElse("?");

      if (name1.equals(name2)) {
        String n1 = m1.getFirstName().orElse("?");
        String n2 = m2.getFirstName().orElse("?");

        return n1.compareTo(n2);
      }

      return name1.compareTo(name2);
    });
    members.forEach(m -> this.membersNode.add(new NamedTreeNode("" + m.getId(), m, false)));

    family.getAllRelations().forEach(r -> this.relationsNode.add(new NamedTreeNode(r.getPartner1() + "_" + r.getPartner2(), r, false)));

    this.treeModel.reload(this.membersNode);
    this.treeModel.reload(this.relationsNode);

    revalidate();
    repaint();
  }

  public void reset() {
    this.membersNode.removeAllChildren();
    this.relationsNode.removeAllChildren();
    this.treeModel.reload(this.membersNode);
    this.treeModel.reload(this.relationsNode);
    revalidate();
    repaint();
  }

  @Override
  public void deselectAll() {
    this.tree.clearSelection();
  }

  @Override
  public Selection getSelection() {
    TreePath[] treePaths = this.tree.getSelectionPaths();
    List<Long> members = new ArrayList<>();
    List<Pair<Long, Long>> relations = new ArrayList<>();

    if (treePaths != null) {
      List<TreePath> paths = Arrays.asList(treePaths);
      paths.forEach(p -> {
        Object obj = ((NamedTreeNode) p.getLastPathComponent()).getUserObject();
        if (obj instanceof FamilyMember) {
          members.add(((FamilyMember) obj).getId());
        }
        else if (obj instanceof Relationship) {
          Relationship r = (Relationship) obj;
          relations.add(new Pair<Long, Long>(r.getPartner1(), r.getPartner2()));
        }
      });
    }

    return new Selection(members, relations);
  }

  JPopupMenu getPopupMenu() {
    return this.popupMenu;
  }

  private SideViewController getController() {
    return (SideViewController) this.controller;
  }
}
