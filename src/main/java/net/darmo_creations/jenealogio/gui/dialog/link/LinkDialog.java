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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import net.darmo_creations.jenealogio.gui.components.AdoptionListRenderer;
import net.darmo_creations.jenealogio.gui.components.DateField;
import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.family.AdoptionListEntry;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.Nullable;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;

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
  private JCheckBox adoptedChk;
  private DateField adoptionDateFld;
  private JButton adoptionValidateBtn;
  private JList<AdoptionListEntry> childrenList;
  private JList<FamilyMember> availChildrenList;
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
    this.adoptedChk = new JCheckBox();
    this.adoptedChk.addActionListener(e -> this.adoptionDateFld.setEnabled(this.adoptedChk.isSelected()));
    this.adoptionDateFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.adoptionValidateBtn = new JButton(I18n.getLocalizedString("button.validate.text"));
    this.adoptionValidateBtn.setActionCommand("validate-adoption");
    this.adoptionValidateBtn.addActionListener(this.controller);
    this.childrenList = new JList<>(new DefaultListModel<>());
    this.childrenList.setCellRenderer(new AdoptionListRenderer());
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
        catch (BadLocationException ex) {}
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
    gbc.gridy = 8;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.children.text")), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 11;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.available_children.text")), gbc);
    gbc.gridwidth = 2;
    gbc.gridy = 15;
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
    JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    p.add(this.adoptedChk);
    p.add(this.adoptionDateFld);
    p.add(this.adoptionValidateBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridy = 8;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);
    gbc.gridy = 11;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    p = new JPanel();
    p.add(this.addBtn);
    p.add(this.removeBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 12;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.availChildrenList), gbc);
    gbc.gridy = 15;
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

  void setAdoptionFieldsEnabled(boolean enabled, boolean checked, @Nullable Date date, boolean isWoman) {
    this.adoptedChk.setEnabled(enabled);
    this.adoptedChk.setSelected(enabled && checked);
    this.adoptedChk.setText(I18n.toTitleCase(I18n.getLocalizedWord("adopted", isWoman, false)));
    this.adoptionDateFld.setEnabled(this.adoptedChk.isSelected());
    this.adoptionDateFld.setDate(date);
    this.adoptionValidateBtn.setEnabled(enabled);
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
   * Returns the selected item in the "children" list.
   */
  AdoptionListEntry getSelectedItem() {
    return this.childrenList.getSelectedValue();
  }

  /**
   * Returns true if the "adopted" checkbox is selected; false otherwise.
   */
  boolean isAdoptedChecked() {
    return this.adoptedChk.isSelected();
  }

  /**
   * Returns the date written inside the "adoption date" field. If the "adoption" checkbox is
   * unchecked, null is returned.
   */
  Date getAdoptionDate() {
    return isAdoptedChecked() ? this.adoptionDateFld.getDate().orElse(null) : null;
  }

  /**
   * @return the children
   */
  Set<Long> getChildren() {
    DefaultListModel<AdoptionListEntry> model = (DefaultListModel<AdoptionListEntry>) this.childrenList.getModel();
    Set<Long> children = new HashSet<>();

    for (int i = 0; i < model.size(); i++) {
      children.add(model.getElementAt(i).getMember().getId());
    }

    return children;
  }

  /**
   * Returns the adopted children.
   */
  Map<Long, Date> getAdoptedChildren() {
    DefaultListModel<AdoptionListEntry> model = (DefaultListModel<AdoptionListEntry>) this.childrenList.getModel();
    Map<Long, Date> adoptions = new HashMap<>();

    for (int i = 0; i < model.size(); i++) {
      AdoptionListEntry entry = model.getElementAt(i);

      if (entry.isAdopted()) {
        adoptions.put(entry.getMember().getId(), entry.getAdoptionDate());
      }
    }

    return adoptions;
  }

  /**
   * Sets the list of children.
   * 
   * @param children the children
   * @param adoptions the adopted children
   */
  void setChildren(Set<FamilyMember> children, Map<Long, Date> adoptions) {
    DefaultListModel<AdoptionListEntry> model = (DefaultListModel<AdoptionListEntry>) this.childrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> {
      long id = child.getId();
      AdoptionListEntry entry = new AdoptionListEntry(child, adoptions.containsKey(id), adoptions.get(id));
      model.addElement(entry);
    });
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
    if (!this.availChildrenList.isSelectionEmpty()) {
      List<FamilyMember> items = this.availChildrenList.getSelectedValuesList();
      DefaultListModel<FamilyMember> srcModel = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();
      DefaultListModel<AdoptionListEntry> destModel = (DefaultListModel<AdoptionListEntry>) this.childrenList.getModel();

      items.forEach(child -> {
        destModel.addElement(new AdoptionListEntry(child, false, null));
        srcModel.removeElement(child);
      });
    }
    if (this.availChildrenList.isSelectionEmpty())
      this.addBtn.setEnabled(false);
  }

  /**
   * Remove the selected children from the list.
   */
  void removeSelectedChildren() {
    if (!this.childrenList.isSelectionEmpty()) {
      List<AdoptionListEntry> items = this.childrenList.getSelectedValuesList();
      DefaultListModel<AdoptionListEntry> srcModel = (DefaultListModel<AdoptionListEntry>) this.childrenList.getModel();
      DefaultListModel<FamilyMember> destModel = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();

      items.forEach(entry -> {
        destModel.addElement(entry.getMember());
        srcModel.removeElement(entry);
      });
    }
    if (this.childrenList.isSelectionEmpty())
      this.removeBtn.setEnabled(false);
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
