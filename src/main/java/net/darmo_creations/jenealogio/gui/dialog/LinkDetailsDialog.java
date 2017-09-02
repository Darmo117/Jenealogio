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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.events.UserEvent;
import net.darmo_creations.jenealogio.events.EventType;
import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.jenealogio.gui.components.AdoptionListRenderer;
import net.darmo_creations.jenealogio.gui.components.DetailsPanel;
import net.darmo_creations.jenealogio.model.family.AdoptionListEntry;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.CalendarUtil;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;

/**
 * This dialog dislays details about a person.
 *
 * @author Damien Vergnet
 */
public class LinkDetailsDialog extends AbstractDialog {
  private static final long serialVersionUID = 4772771687761193691L;

  private JLabel weddingLbl;
  private JLabel partnersLbl;
  private JLabel dateLbl, locationLbl;
  private JLabel endDateLbl;
  private JList<AdoptionListEntry> children;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public LinkDetailsDialog(MainFrame owner) {
    super(owner, Mode.CLOSE_OPTION, true);
    setIconImage(Images.JENEALOGIO.getImage());
    setPreferredSize(new Dimension(300, 230));

    JPanel infoPnl = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets = new Insets(5, 10, 0, 10);
    infoPnl.add(new JLabel(I18n.getLocalizedString("label.wedding.text")), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    infoPnl.add(this.weddingLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    infoPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_partners.text")), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    infoPnl.add(this.partnersLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    infoPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_date.text")), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    infoPnl.add(this.dateLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    infoPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_location.text")), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    infoPnl.add(this.locationLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    infoPnl.add(new JLabel(Images.HEART_BROKEN), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    infoPnl.add(this.endDateLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    infoPnl.add(new JLabel(Images.BABY), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    infoPnl.add(new JScrollPane(this.children = new JList<>(new DefaultListModel<>())), gbc);
    this.children.setEnabled(false);
    this.children.setCellRenderer(new AdoptionListRenderer() {
      private static final long serialVersionUID = -8410306056784274760L;

      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setEnabled(true); // To change the foreground to black
        return this;
      }
    });

    add(infoPnl, BorderLayout.CENTER);

    JButton editBtn = new JButton(I18n.getLocalizedString("button.edit.text"));
    addButton(editBtn);
    editBtn.setActionCommand("edit");

    setActionListener(new DefaultDialogController<LinkDetailsDialog>(this) {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("edit")) {
          this.dialog.setVisible(false);
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new UserEvent(EventType.EDIT_LINK));
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
  public void setInfo(Relationship relation, Family family) {
    Supplier<IllegalStateException> supplier = IllegalStateException::new;
    FamilyMember partner1 = family.getMember(relation.getPartner1()).orElseThrow(supplier);
    FamilyMember partner2 = family.getMember(relation.getPartner2()).orElseThrow(supplier);
    String partners = String.format("<html>%s<br/>%s</html>", partner1, partner2).replace(" ", "&nbsp;");

    setTitle(partner1 + "/" + partner2);
    String key = relation.isWedding() ? "yes" : "no";
    this.weddingLbl.setText(I18n.toTitleCase(I18n.getLocalizedWord(key, false, false)));
    this.partnersLbl.setText(partners);
    this.dateLbl.setText(CalendarUtil.formatDate(relation.getDate()).orElse(DetailsPanel.UNKNOWN_DATA));
    this.locationLbl.setText(relation.getLocation().orElse(DetailsPanel.UNKNOWN_DATA));
    String endDate;
    if (relation.hasEnded()) {
      endDate = CalendarUtil.formatDate(relation.getEndDate()).orElse(DetailsPanel.UNKNOWN_DATA);
    }
    else {
      endDate = "–";
    }
    this.endDateLbl.setText(endDate);
    DefaultListModel<AdoptionListEntry> model = (DefaultListModel<AdoptionListEntry>) this.children.getModel();
    model.removeAllElements();
    relation.getChildren().forEach(id -> {
      FamilyMember child = family.getMember(id).orElseThrow(supplier);
      AdoptionListEntry entry = new AdoptionListEntry(child, relation.isAdopted(id), relation.getAdoptionDate(id).orElse(null));
      model.addElement(entry);
    });
    pack();
    setMinimumSize(getSize());
  }
}
