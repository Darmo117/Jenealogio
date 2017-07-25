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
package net.darmo_creations.gui.dialog.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.darmo_creations.controllers.ExtensionsFileFilter;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.components.DateField;
import net.darmo_creations.gui.components.ImageLabel;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.model.date.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

/**
 * This dialog lets the user add or edit cards.
 *
 * @author Damien Vergnet
 */
public class CardDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private CardController controller;

  private JFileChooser fileChooser;

  private long id;
  private BufferedImage image;
  private JLabel imageLbl;
  private JTextField familyNameFld, useNameFld, firstNameFld, otherNamesFld, birthLocationFld, deathLocationFld;
  private JCheckBox deadChkBox;
  private JComboBox<Gender> genderCombo;
  private DateField birthFld, deathFld;
  private JTextArea commentFld;

  /**
   * Creates a new dialog.
   * 
   * @param owner the owner
   */
  public CardDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setPreferredSize(new Dimension(350, 570));
    setResizable(false);
    setIconImage(Images.JENEALOGIO.getImage());

    this.controller = new CardController(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        CardDialog.this.familyNameFld.requestFocus();
      }
    });

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileFilter(new ExtensionsFileFilter(I18n.getLocalizedString("file_type.image.desc"), "png", "jpg", "jpeg", "bmp"));

    this.image = null;
    this.imageLbl = new ImageLabel(null);
    this.imageLbl.setPreferredSize(new Dimension(120, 150));
    this.imageLbl.setMaximumSize(this.imageLbl.getPreferredSize());
    this.imageLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.imageLbl.setBorder(new LineBorder(Color.GRAY));

    JButton imageBtn = new JButton(I18n.getLocalizedString("button.choose_image.text"));
    imageBtn.setActionCommand("choose-image");
    imageBtn.addActionListener(this.controller);
    imageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    imageBtn.setFocusPainted(false);

    this.familyNameFld = new JTextField();
    this.familyNameFld.getDocument().addDocumentListener(this.controller);

    this.useNameFld = new JTextField();
    this.useNameFld.getDocument().addDocumentListener(this.controller);

    this.firstNameFld = new JTextField();
    this.firstNameFld.getDocument().addDocumentListener(this.controller);

    this.otherNamesFld = new JTextField();
    this.otherNamesFld.getDocument().addDocumentListener(this.controller);

    this.genderCombo = new JComboBox<>(Gender.values());
    this.genderCombo.addItemListener(this.controller);
    this.genderCombo.setRenderer(new GenderComboRenderer(this.genderCombo.getRenderer()));

    this.birthFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.birthFld.addDocumentListener(this.controller);

    this.birthLocationFld = new JTextField();
    this.birthLocationFld.getDocument().addDocumentListener(this.controller);

    this.deathFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.deathFld.addDocumentListener(this.controller);

    this.deathLocationFld = new JTextField();
    this.deathLocationFld.getDocument().addDocumentListener(this.controller);

    this.deadChkBox = new JCheckBox(I18n.getLocalizedString("label.dead.text"));

    this.commentFld = new JTextArea();
    this.commentFld.setFont(Font.getFont("Tahoma"));

    JPanel imagePnl = new JPanel();
    imagePnl.setBorder(new EmptyBorder(5, 5, 0, 5));
    imagePnl.setLayout(new BoxLayout(imagePnl, BoxLayout.PAGE_AXIS));
    imagePnl.add(this.imageLbl);
    imagePnl.add(imageBtn);

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.family_name.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.familyNameFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.use_name.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.useNameFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.first_name.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.firstNameFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.other_names.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.otherNamesFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.gender.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.genderCombo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.birth_date.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.birthFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.birth_location.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.birthLocationFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.death_date.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.deathFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.death_location.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.insets = new Insets(5, 10, 0, 10);
    fieldsPnl.add(this.deathLocationFld, gbc);

    gbc.gridx = 0;
    gbc.gridy = 9;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.insets = new Insets(5, 10, 0, 0);
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.comment.text")), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = gbc.weighty = 1.;
    gbc.insets = new Insets(5, 10, 0, 10);
    JScrollPane scroll = new JScrollPane(this.commentFld);
    scroll.setPreferredSize(new Dimension(-1, 80));
    fieldsPnl.add(scroll, gbc);

    gbc.gridx = 0;
    gbc.gridy = 10;
    gbc.weightx = gbc.weighty = 0.;
    gbc.fill = GridBagConstraints.NONE;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets = new Insets(5, 10, 0, 0);
    fieldsPnl.add(this.deadChkBox, gbc);

    add(imagePnl, BorderLayout.NORTH);
    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * @return the ID
   */
  public long getId() {
    return this.id;
  }

  /**
   * Sets the ID.
   * 
   * @param id the new ID
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the family name
   */
  public String getFamilyName() {
    return getContent(this.familyNameFld);
  }

  /**
   * Sets the family name.
   * 
   * @param familyName the new family name
   */
  public void setFamilyName(String familyName) {
    this.familyNameFld.setText(familyName);
  }

  /**
   * @return the use name
   */
  public String getUseName() {
    return getContent(this.useNameFld);
  }

  /**
   * Sets the use name.
   * 
   * @param useName the new use name
   */
  public void setUseName(String useName) {
    this.useNameFld.setText(useName);
  }

  /**
   * @return the first name
   */
  public String getFirstName() {
    return getContent(this.firstNameFld);
  }

  /**
   * Sets the first name.
   * 
   * @param firstName the new first name
   */
  public void setFirstName(String firstName) {
    this.firstNameFld.setText(firstName);
  }

  /**
   * @return the other names
   */
  public String getOtherNames() {
    return getContent(this.otherNamesFld);
  }

  /**
   * Sets the other names.
   * 
   * @param otherNames the new other names
   */
  public void setOtherNames(String otherNames) {
    this.otherNamesFld.setText(otherNames);
  }

  /**
   * @return the gender
   */
  public Gender getGender() {
    return Gender.values()[this.genderCombo.getSelectedIndex()];
  }

  /**
   * Sets the gender.
   * 
   * @param gender the new gender
   */
  public void setGender(Gender gender) {
    this.genderCombo.setSelectedIndex(gender.ordinal());
  }

  /**
   * @return the birthday
   */
  public Date getBirthDate() {
    return this.birthFld.getDate().orElse(null);
  }

  /**
   * Sets the birthday.
   * 
   * @param date the new date
   */
  public void setBirthDate(Optional<Date> date) {
    this.birthFld.setDate(date.orElse(null));
  }

  /**
   * @return the birth location
   */
  public String getBirthLocation() {
    return getContent(this.birthLocationFld);
  }

  /**
   * Sets the birth location.
   * 
   * @param location the new location
   */
  public void setBirthLocation(String location) {
    this.birthLocationFld.setText(location);
  }

  /**
   * @return the death date
   */
  public Date getDeathDate() {
    return this.deathFld.getDate().orElse(null);
  }

  /**
   * Sets the death date.
   * 
   * @param date the date
   */
  public void setDeathDate(Optional<Date> date) {
    this.deathFld.setDate(date.orElse(null));
    updateDeath();
  }

  /**
   * @return the death location
   */
  public String getDeathLocation() {
    return getContent(this.deathLocationFld);
  }

  /**
   * Sets the death location.
   * 
   * @param location the new location
   */
  public void setDeathLocation(String location) {
    this.deathLocationFld.setText(location);
    updateDeath();
  }

  /**
   * Updates the death boolean.
   */
  private void updateDeath() {
    setDead(getDeathDate() != null || getDeathLocation() != null);
  }

  /**
   * @return true if the person is dead
   */
  public boolean isDead() {
    return this.deadChkBox.isSelected();
  }

  /**
   * Sets the dead boolean.
   * 
   * @param dead
   */
  public void setDead(boolean dead) {
    if (getDeathDate() == null && getDeathLocation() == null)
      this.deadChkBox.setSelected(dead);
  }

  /**
   * Enables or disables the checkbox.
   * 
   * @param enabled true to enable the button, otherwise false
   */
  public void setCheckBoxEnabled(boolean enabled) {
    this.deadChkBox.setEnabled(enabled);
    if (!this.deadChkBox.isEnabled())
      this.deadChkBox.setSelected(true);
  }

  /**
   * @return the comment
   */
  public String getComment() {
    String text = this.commentFld.getText();
    return text.length() > 0 ? text : null;
  }

  /**
   * Sets the comment.
   * 
   * @param comment the new comment
   */
  public void setComment(String comment) {
    this.commentFld.setText(comment);
  }

  @Override
  public void setVisible(boolean b) {
    if (!isCanceled() && !b) {
      if (!checkDates())
        showErrorDialog(I18n.getLocalizedString("popup.birth_date_error.text"));
      else
        super.setVisible(false);
    }
    else
      super.setVisible(b);
  }

  /**
   * Checks if the dates are valid.
   * 
   * @return true if and only if all dates are well formatted
   */
  private boolean checkDates() {
    Optional<Date> birth = this.birthFld.getDate();
    Optional<Date> death = this.deathFld.getDate();

    return !birth.isPresent() || !death.isPresent() || (birth.isPresent() && death.isPresent() && birth.get().compareTo(death.get()) <= 0);
  }

  /**
   * Shows the "open" file chooser.
   * 
   * @return the selected file
   */
  File showOpenFileChooser() {
    this.fileChooser.showOpenDialog(this);
    return this.fileChooser.getSelectedFile();
  }

  /**
   * Sets card data.
   * 
   * @param member the member to display
   */
  public void setCard(FamilyMember member) {
    this.controller.reset(member);
  }

  /**
   * Sets the image.
   * 
   * @param image the image
   */
  void setImage(Optional<BufferedImage> image) {
    if (image.isPresent()) {
      this.image = image.get();
      this.imageLbl.setIcon(new ImageIcon(image.get()));
    }
    else {
      this.image = null;
      this.imageLbl.setIcon(null);
    }
  }

  /**
   * @return the card or nothing if the dialog was canceled
   */
  public Optional<FamilyMember> getCard() {
    if (!isCanceled()) {
      FamilyMember member = new FamilyMember(getId(), this.image, getFamilyName(), getUseName(), getFirstName(), getOtherNames(),
          getGender(), getBirthDate(), getBirthLocation(), getDeathDate(), getDeathLocation(), isDead(), getComment());

      return Optional.of(member);
    }
    return Optional.empty();
  }

  /**
   * Returns the content of a text field.
   * 
   * @param field the text field
   * @return text field's content
   */
  private String getContent(JTextField field) {
    String text = field.getText().trim().replaceAll("\\s+", " ");
    return text.length() > 0 ? text : null;
  }

  /**
   * Tells if at least one field has been completed.
   * 
   * @return true if a field has been completed
   */
  boolean atLeastOneFieldCompleted() {
    boolean ok = false;

    ok |= this.familyNameFld.getText().length() > 0;
    ok |= this.useNameFld.getText().length() > 0;
    ok |= this.firstNameFld.getText().length() > 0;
    ok |= this.otherNamesFld.getText().length() > 0;
    ok |= this.genderCombo.getSelectedItem() != Gender.UNKNOW;
    ok |= this.birthFld.getDate().isPresent();
    ok |= this.deathFld.getDate().isPresent();
    ok |= this.commentFld.getText().length() > 0;

    return ok;
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
