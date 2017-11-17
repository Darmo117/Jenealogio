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
package net.darmo_creations.jenealogio.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.jenealogio.gui.components.DetailsPanel;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.ImageLabel;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;

/**
 * This dialog dislays details about a person.
 *
 * @author Damien Vergnet
 */
public class CardDetailsDialog extends AbstractDialog {
  private static final long serialVersionUID = 4772771687761193691L;

  private ImageLabel imageLbl;
  private DetailsPanel infoPnl;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public CardDetailsDialog(MainFrame owner) {
    super(owner, Mode.CLOSE_OPTION, true);
    setIconImage(Images.JENEALOGIO.getImage());

    JPanel imagePnl = new JPanel();
    imagePnl.add(this.imageLbl = new ImageLabel(null, true));
    this.imageLbl.setPreferredSize(new Dimension(120, 150));
    this.imageLbl.setMaximumSize(this.imageLbl.getPreferredSize());
    this.imageLbl.setBorder(new LineBorder(Color.GRAY));
    add(imagePnl, BorderLayout.NORTH);
    add(this.infoPnl = new DetailsPanel(), BorderLayout.CENTER);
    JButton editBtn = new JButton(I18n.getLocalizedString("button.edit.text"));
    addButton(editBtn);
    editBtn.setActionCommand("edit");

    setActionListener(new DefaultDialogController<CardDetailsDialog>(this) {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("edit")) {
          this.dialog.setVisible(false);
          // TODO
          // ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(EventType.EDIT_CARD));
          return;
        }
        super.actionPerformed(e);
      }
    });

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Sets displayed information.
   * 
   * @param member the person
   * @param relations the relations it is part of
   */
  public void setInfo(FamilyMember member, Set<Relationship> relations) {
    setTitle(member.toString());
    if (member.getImage().isPresent()) {
      this.imageLbl.setIcon(new ImageIcon(member.getImage().get()));
    }
    else {
      this.imageLbl.setIcon(null);
    }
    this.infoPnl.setInfo(member, relations);
    pack();
    setMinimumSize(getSize());
  }
}
