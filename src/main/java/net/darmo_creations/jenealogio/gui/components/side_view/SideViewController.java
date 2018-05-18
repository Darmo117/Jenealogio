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
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
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
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.events.GoToObjectEvent;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.jenealogio.gui.components.view.ViewController;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;

public class SideViewController extends ViewController<SideView> implements TreeSelectionListener {
  private Selection lastSelection;

  public SideViewController() {
    super(ViewType.SIDE);

    this.lastSelection = new Selection(Collections.emptyList(), Collections.emptyList());
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    JTree tree = (JTree) e.getSource();
    TreePath[] treePaths = tree.getSelectionPaths();

    if (treePaths != null) {
      List<TreePath> paths = Arrays.asList(treePaths);
      JPopupMenu popupMenu = getView().getPopupMenu();
      boolean gotoEnabled = paths.size() == 1;
      boolean canDelete = paths.stream().allMatch(p -> {
        Object obj = ((NamedTreeNode) p.getLastPathComponent()).getUserObject();
        return obj instanceof FamilyMember || obj instanceof Relationship;
      });

      if (gotoEnabled) {
        Object o = ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject();
        gotoEnabled &= o instanceof FamilyMember || o instanceof Relationship;
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
              if (o instanceof FamilyMember || o instanceof Relationship) {
                i.addActionListener(evt -> ApplicationRegistry.EVENTS_BUS.dispatchEvent(new GoToObjectEvent(o)));
              }
            }
            break;
          case "delete":
            i.setEnabled(canDelete);

            if (canDelete) {
              Object o = ((NamedTreeNode) paths.get(0).getLastPathComponent()).getUserObject();

              if (o instanceof FamilyMember)
                i.setText(I18n.getLocalizedString("item.delete_card.text"));
              else if (o instanceof Relationship)
                i.setText(I18n.getLocalizedString("item.delete_link.text"));

              if (o instanceof FamilyMember || o instanceof Relationship) {
                i.addActionListener(l -> ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(EventType.DELETE_OBJECT)));
                i.setIcon(Images.DELETE);
              }
            }
            break;
        }
      }
    }

    ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(this.lastSelection, getView().getSelection()));
    this.lastSelection = getView().getSelection();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);

    if (!(e.getSource() instanceof JTree))
      return;

    JTree tree = ((JTree) e.getComponent());
    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

    if (SwingUtilities.isRightMouseButton(e)) {
      getView().getPopupMenu().show(tree, e.getX(), e.getY());
    }
    else if (selPath != null && SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
      NamedTreeNode clickedNode = (NamedTreeNode) selPath.getLastPathComponent();
      NamedTreeNode selectedNode = (NamedTreeNode) tree.getLastSelectedPathComponent();
      Object value = clickedNode.getUserObject();

      if (clickedNode == selectedNode) {
        if (value instanceof FamilyMember) {
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDoubleClickEvent(((FamilyMember) value).getId()));
        }
        else if (value instanceof Relationship) {
          Relationship r = (Relationship) value;
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkDoubleClickEvent(r.getPartner1(), r.getPartner2()));
        }
      }
    }
  }
}
