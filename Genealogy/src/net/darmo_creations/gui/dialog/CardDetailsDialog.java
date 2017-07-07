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
package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.components.DetailsPanel;
import net.darmo_creations.gui.components.ImageLabel;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Relationship;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;
import net.darmo_creations.util.Observable;
import net.darmo_creations.util.Observer;

/**
 * This dialog dislays details about a person.
 *
 * @author Damien Vergnet
 */
public class CardDetailsDialog extends AbstractDialog implements Observable {
  private static final long serialVersionUID = 4772771687761193691L;

  private List<Observer> observers;

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

    this.observers = new ArrayList<>();

    JPanel imagePnl = new JPanel();
    imagePnl.add(this.imageLbl = new ImageLabel(null));
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
          notifyObservers("edit");
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

  @Override
  public void addObserver(Observer observer) {
    this.observers.add(Objects.requireNonNull(observer));
  }

  @Override
  public void removeObserver(Observer observer) {
    if (observer != null)
      this.observers.remove(observer);
  }

  @Override
  public void notifyObservers(Object o) {
    this.observers.forEach(obs -> obs.update(this, o));
  }
}
