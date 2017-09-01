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
package net.darmo_creations.jenealogio.gui.dialog.link;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import net.darmo_creations.jenealogio.gui.MainFrame;
import net.darmo_creations.jenealogio.gui.components.DateField;
import net.darmo_creations.jenealogio.gui.dialog.AbstractDialog;
import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.I18n;
import net.darmo_creations.jenealogio.util.Images;

/**
 * This dialog lets the user add or edit links.
 *
 * @author Damien Vergnet
 */
public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private LinkController controller;
  private JCheckBox weddingChk;
  private DateField dateFld;
  private JTextField locationFld;
  private JCheckBox endedChk;
  private DateField endDateFld;
  private JTextField partner1Field, partner2Field;
  private JList<FamilyMember> childrenList, availChildrenList;
  private JButton addBtn, removeBtn;
  private JTextField searchFld;

  /**
   * Creates a new dialog.
   * 
   * @param owner the owner
   */
  public LinkDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);
    setIconImage(Images.JENEALOGIO.getImage());

    this.controller = new LinkController(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        LinkDialog.this.dateFld.requestFocus();
      }
    });

    this.weddingChk = new JCheckBox(I18n.getLocalizedString("label.wedding.text"));
    this.dateFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.locationFld = new JTextField();
    this.endedChk = new JCheckBox(I18n.getLocalizedString("label.relationship_ended.text"));
    this.endedChk.setActionCommand("ended");
    this.endedChk.addActionListener(e -> this.endDateFld.setEnabled(this.endedChk.isSelected()));
    this.endDateFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.partner1Field = new JTextField();
    this.partner1Field.setEnabled(false);
    this.partner2Field = new JTextField();
    this.partner2Field.setEnabled(false);
    this.childrenList = new JList<>(new DefaultListModel<>());
    this.childrenList.addListSelectionListener(this.controller);
    this.childrenList.setName("children");
    this.availChildrenList = new JList<>(new DefaultListModel<>());
    this.availChildrenList.addListSelectionListener(this.controller);
    this.availChildrenList.setName("available-children");
    this.addBtn = new JButton(Images.ARROW_UP);
    this.addBtn.setActionCommand("add");
    this.addBtn.addActionListener(this.controller);
    this.removeBtn = new JButton(Images.ARROW_DOWN);
    this.removeBtn.setActionCommand("remove");
    this.removeBtn.addActionListener(this.controller);
    this.searchFld = new JTextField();
    this.searchFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update(e);
      }

      private void update(DocumentEvent e) {
        try {
          String text = e.getDocument().getText(0, e.getDocument().getLength()).toLowerCase();
          DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) LinkDialog.this.availChildrenList.getModel();

          for (int i = 0; i < model.size(); i++) {
            FamilyMember m = model.getElementAt(i);

            if (m.toString().toLowerCase().contains(text)) {
              model.remove(i);
              model.insertElementAt(m, 0);
            }
          }
        }
        catch (BadLocationException __) {}
      }
    });

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 2;
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_date.text")), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_location.text")), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.relationship_end_date.text")), gbc);
    gbc.gridy = 5;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.partner1.text")), gbc);
    gbc.gridy = 6;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.partner2.text")), gbc);
    gbc.gridy = 7;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.children.text")), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.available_children.text")), gbc);
    gbc.gridwidth = 2;
    gbc.gridy = 14;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.search.text")), gbc);

    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 0;
    fieldsPnl.add(this.weddingChk, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.locationFld, gbc);
    gbc.gridy = 3;
    fieldsPnl.add(this.endedChk, gbc);
    gbc.gridy = 4;
    fieldsPnl.add(this.endDateFld, gbc);
    gbc.gridy = 5;
    fieldsPnl.add(this.partner1Field, gbc);
    gbc.gridy = 6;
    fieldsPnl.add(this.partner2Field, gbc);
    gbc.gridy = 7;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);
    gbc.gridy = 10;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JPanel p = new JPanel();
    p.add(this.addBtn);
    p.add(this.removeBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 11;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.availChildrenList), gbc);
    gbc.gridy = 14;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridheight = 1;
    fieldsPnl.add(this.searchFld, gbc);

    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Sets the dialog to "add link" mode. The two spouses must be different
   * 
   * @param partner1 one partner
   * @param partner2 the other partner
   * @param family the family
   */
  public void addLink(long partner1, long partner2, Family family) {
    setTitle(I18n.getLocalizedString("dialog.add_link.title"));
    this.controller.reset(partner1, partner2, family);
  }

  /**
   * Sets the dialog to "update link" mode.
   * 
   * @param relation the link
   * @param family the family
   */
  public void updateLink(Relationship relation, Family family) {
    setTitle(I18n.getLocalizedString("dialog.update_link.title"));
    this.controller.reset(relation, family);
  }

  /**
   * @return the created/updated link or nothing if the dialog was canceled
   */
  public Optional<Relationship> getLink() {
    if (!isCanceled())
      return Optional.of(this.controller.getLink());
    return Optional.empty();
  }

  /**
   * Enables/disables the "add" button.
   * 
   * @param enabled
   */
  void setAddButtonEnabled(boolean enabled) {
    this.addBtn.setEnabled(enabled);
  }

  /**
   * Enables/disables the "remove" button.
   * 
   * @param enabled
   */
  void setDeleteButtonEnabled(boolean enabled) {
    this.removeBtn.setEnabled(enabled);
  }

  /**
   * @return true if wedding checkbox is checked
   */
  boolean isWeddingChecked() {
    return this.weddingChk.isSelected();
  }

  /**
   * Sets the wedding checkbox selection.
   */
  void setWeddingChecked(boolean checked) {
    this.weddingChk.setSelected(checked);
  }

  /**
   * @return the date
   */
  Date getDate() {
    return this.dateFld.getDate().orElse(null);
  }

  /**
   * Sets the date.
   * 
   * @param date the new date
   */
  void setDate(Date date) {
    this.dateFld.setDate(date);
  }

  /**
   * @return the relationship location
   */
  String getRelationshipLocation() {
    return this.locationFld.getText();
  }

  /**
   * Sets the relationship location.
   * 
   * @param location the new location
   */
  void setRelationshipLocation(Optional<String> location) {
    this.locationFld.setText(location.orElse(""));
  }

  /**
   * @return true if "ended" checkboc is selected
   */
  boolean isEndedChecked() {
    return this.endedChk.isSelected();
  }

  /**
   * Sets the "ended" checkbox selection.
   */
  void setEndedChecked(boolean checked) {
    this.endedChk.setSelected(checked);
    this.endDateFld.setEnabled(checked);
  }

  /**
   * @return the end date
   */
  Date getEndDate() {
    return isEndedChecked() ? this.endDateFld.getDate().orElse(null) : null;
  }

  /**
   * Sets the end date.
   * 
   * @param date the new date
   */
  void setEndDate(Date date) {
    this.endDateFld.setDate(date);
  }

  /**
   * Sets the first partner.
   * 
   * @param name the name
   */
  void setPartner1(String name) {
    this.partner1Field.setText(name);
  }

  /**
   * Sets the second partner.
   * 
   * @param name the name
   */
  void setPartner2(String name) {
    this.partner2Field.setText(name);
  }

  /**
   * @return the children
   */
  long[] getChildren() {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    long[] children = new long[model.size()];

    for (int i = 0; i < model.size(); i++) {
      children[i] = model.getElementAt(i).getId();
    }

    return children;
  }

  /**
   * Sets the list of children.
   * 
   * @param children the children
   */
  void setChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
  }

  /**
   * Sets the list of potential children.
   * 
   * @param children the potential children
   */
  void setAvailableChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
    this.searchFld.setText(null);
  }

  /**
   * Add the selected children to the list.
   */
  void addSelectedChildren() {
    transfertItems(this.availChildrenList, this.childrenList);
    if (this.availChildrenList.isSelectionEmpty())
      this.addBtn.setEnabled(false);
  }

  /**
   * Remove the selected children from the list.
   */
  void removeSelectedChildren() {
    transfertItems(this.childrenList, this.availChildrenList);
    if (this.childrenList.isSelectionEmpty())
      this.removeBtn.setEnabled(false);
  }

  /**
   * Transfer selected items from {@code source} to {@code destination}.
   * 
   * @param source source list
   * @param destination destination list
   */
  private void transfertItems(JList<FamilyMember> source, JList<FamilyMember> destination) {
    if (!source.isSelectionEmpty()) {
      List<FamilyMember> items = source.getSelectedValuesList();
      DefaultListModel<FamilyMember> srcModel = (DefaultListModel<FamilyMember>) source.getModel();
      DefaultListModel<FamilyMember> destModel = (DefaultListModel<FamilyMember>) destination.getModel();

      items.forEach(child -> {
        destModel.addElement(child);
        srcModel.removeElement(child);
      });
    }
  }

  /**
   * Shows an error message.
   * 
   * @param message the message
   */
  void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }
}
