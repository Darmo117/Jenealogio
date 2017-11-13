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

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.jenealogio.events.CardEvent;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.events.LinkEvent;
import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;

public class SideViewController extends MouseAdapter implements TreeSelectionListener {
  private SideView view;

  public SideViewController(SideView view) {
    this.view = view;
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    JTree tree = (JTree) e.getSource();
    TreePath[] treePaths = tree.getSelectionPaths();

    if (treePaths != null) {
      List<TreePath> paths = Arrays.asList(treePaths);
      JPopupMenu popupMenu = this.view.getPopupMenu();
      boolean gotoEnabled = paths.size() == 1;
      boolean canDelete = !paths.isEmpty();

      if (gotoEnabled) {
        Object o = ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject();
        gotoEnabled &= o instanceof FamilyMember || o instanceof Relationship;
      }
      if (canDelete) {
        canDelete &= paths.size() == 1 && ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject() instanceof Relationship;
        if (!canDelete)
          canDelete |= paths.stream().allMatch(p -> ((NamedTreeNode) p.getLastPathComponent()).getUserObject() instanceof FamilyMember);
      }

      for (Component c : popupMenu.getComponents()) {
        JMenuItem i = (JMenuItem) c;
        switch (i.getName()) {
          case "go_to":
            i.setEnabled(gotoEnabled);
            for (ActionListener l : i.getActionListeners())
              i.removeActionListener(l);

            if (gotoEnabled) {
              Object o = ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject();
              if (o instanceof FamilyMember) {
                FamilyMember m = (FamilyMember) o;
                i.addActionListener(l -> ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(m.getId(), false, true)));
              }
              else if (o instanceof Relationship) {
                Relationship r = (Relationship) o;
                i.addActionListener(l -> {
                  ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(-1, false));
                  ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkEvent.Clicked(r.getPartner1(), r.getPartner2(), true));
                });
              }
            }
            break;
          case "delete":
            i.setEnabled(canDelete);
            for (ActionListener l : i.getActionListeners())
              i.removeActionListener(l);

            if (canDelete) {
              Object o = ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject();
              if (o instanceof FamilyMember) {
                FamilyMember m = (FamilyMember) o;
                i.addActionListener(l -> {
                  ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(m.getId(), false));
                  for (int j = 1; j < paths.size(); j++) {
                    FamilyMember m1 = (FamilyMember) ((NamedTreeNode) paths.get(j).getLastPathComponent()).getUserObject();
                    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.Clicked(m1.getId(), true));
                  }
                  ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(EventType.DELETE_CARD));
                });
                i.setText(I18n.getLocalizedString("item.delete_card.text"));
                i.setIcon(Images.DELETE_CARD);
              }
              else if (o instanceof Relationship) {
                Relationship r = (Relationship) o;
                // TODO listener
                i.setText(I18n.getLocalizedString("item.delete_link.text"));
                i.setIcon(Images.DELETE_LINK);
              }
              else {
                i.setText(I18n.getLocalizedString("item.delete.text"));
                i.setIcon(null);
              }
            }
            else {
              i.setText(I18n.getLocalizedString("item.delete.text"));
              i.setIcon(null);
            }
            break;
        }
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    JTree tree = ((JTree) e.getComponent());
    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

    if (SwingUtilities.isRightMouseButton(e)) {
      this.view.getPopupMenu().show(tree, e.getX(), e.getY());
    }
    else if (selPath != null && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
      NamedTreeNode clickedNode = (NamedTreeNode) selPath.getLastPathComponent();
      NamedTreeNode selectedNode = (NamedTreeNode) tree.getLastSelectedPathComponent();
      Object value = clickedNode.getUserObject();

      if (clickedNode == selectedNode) {
        if (value instanceof FamilyMember) {
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardEvent.DoubleClicked(((FamilyMember) value).getId()));
        }
        else if (value instanceof Relationship) {
          Relationship r = (Relationship) value;
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkEvent.DoubleClicked(r.getPartner1(), r.getPartner2()));
        }
      }
    }
  }
}
