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
package net.darmo_creations.jenealogio.gui.components.side_tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;

public class SideView extends JPanel {
  private static final long serialVersionUID = -1739589891038892474L;

  private DefaultTreeModel treeModel;
  private NamedTreeNode membersNode, relationsNode;
  private JPopupMenu popupMenu;

  private Family family;

  public SideView() {
    super(new BorderLayout());

    NamedTreeNode root = new NamedTreeNode("root", "Root");
    this.membersNode = new NamedTreeNode("members", I18n.getLocalizedString("node.members.text"));
    this.relationsNode = new NamedTreeNode("relations", I18n.getLocalizedString("node.relations.text"));
    root.add(this.membersNode);
    root.add(this.relationsNode);

    SideViewController controller = new SideViewController(this);
    this.treeModel = new DefaultTreeModel(root);
    JTree tree = new JTree(this.treeModel);
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.setBorder(new EmptyBorder(5, 5, 5, 5));
    tree.addMouseListener(controller);
    tree.addTreeSelectionListener(controller);
    tree.setLargeModel(true);
    tree.setCellRenderer(new DefaultTreeCellRenderer() {
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

    JLabel topLbl = new JLabel(I18n.getLocalizedString("label.tree_explorer.text"));
    topLbl.setBorder(new CompoundBorder(new MatteBorder(0, 0, 2, 0, Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
    add(topLbl, BorderLayout.NORTH);

    add(new JScrollPane(tree), BorderLayout.CENTER);

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

    List<Relationship> relations = new ArrayList<>(family.getAllRelations());
    relations.sort((r1, r2) -> {
      return 0; // TODO trouver un critère de tri
    });
    relations.forEach(r -> this.relationsNode.add(new NamedTreeNode(r.getPartner1() + "_" + r.getPartner2(), r, false)));

    this.treeModel.reload(this.membersNode);
    this.treeModel.reload(this.relationsNode);

    revalidate();
    repaint();
  }

  JPopupMenu getPopupMenu() {
    return this.popupMenu;
  }
}
