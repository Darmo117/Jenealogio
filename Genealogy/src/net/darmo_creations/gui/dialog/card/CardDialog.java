package net.darmo_creations.gui.dialog.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.darmo_creations.controllers.ExtensionFileFilter;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.components.ImageLabel;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.util.CalendarUtil;
import net.darmo_creations.util.I18n;

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
  private JTextField nameFld, firstNameFld, birthLocationFld, deathLocationFld;
  private JComboBox<Gender> genderCombo;
  private JFormattedTextField birthFld, deathFld;

  /**
   * Creates a new dialog.
   * 
   * @param owner the owner
   */
  public CardDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setPreferredSize(new Dimension(300, 410));
    setResizable(false);

    this.controller = new CardController(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        CardDialog.this.nameFld.requestFocus();
      }
    });

    this.fileChooser = new JFileChooser();
    this.fileChooser.setAcceptAllFileFilterUsed(false);
    this.fileChooser.setMultiSelectionEnabled(false);
    this.fileChooser.setFileFilter(new ExtensionFileFilter(I18n.getLocalizedString("file_type.image.desc"), "png", "jpg", "jpeg", "bmp"));

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
    this.nameFld = new JTextField();
    this.nameFld.getDocument().addDocumentListener(this.controller);
    this.firstNameFld = new JTextField();
    this.firstNameFld.getDocument().addDocumentListener(this.controller);
    this.genderCombo = new JComboBox<>(Gender.values()); // TODO utiliser I18n.
    this.genderCombo.addItemListener(this.controller);
    DateFormat dateFormat = new SimpleDateFormat(I18n.getLocalizedString("date.format"));
    this.birthFld = new JFormattedTextField(dateFormat);
    this.birthFld.addKeyListener(this.controller);
    this.birthFld.getDocument().addDocumentListener(this.controller);
    this.birthLocationFld = new JTextField();
    this.birthLocationFld.getDocument().addDocumentListener(this.controller);
    this.deathFld = new JFormattedTextField(dateFormat);
    this.deathFld.addKeyListener(this.controller);
    this.deathFld.getDocument().addDocumentListener(this.controller);
    this.deathLocationFld = new JTextField();
    this.deathLocationFld.getDocument().addDocumentListener(this.controller);

    JPanel imagePnl = new JPanel();
    imagePnl.setBorder(new EmptyBorder(5, 5, 0, 5));
    imagePnl.setLayout(new BoxLayout(imagePnl, BoxLayout.PAGE_AXIS));
    imagePnl.add(this.imageLbl);
    imagePnl.add(imageBtn);

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridy = 0;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.name.text")), gbc);
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.first_name.text")), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.gender.text")), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.birth_date.text")), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.birth_location.text")), gbc);
    gbc.gridy = 5;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.death_date.text")), gbc);
    gbc.gridy = 6;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.death_location.text")), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.gridy = 0;
    fieldsPnl.add(this.nameFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.firstNameFld, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.genderCombo, gbc);
    gbc.gridy = 3;
    fieldsPnl.add(this.birthFld, gbc);
    gbc.gridy = 4;
    fieldsPnl.add(this.birthLocationFld, gbc);
    gbc.gridy = 5;
    fieldsPnl.add(this.deathFld, gbc);
    gbc.gridy = 6;
    fieldsPnl.add(this.deathLocationFld, gbc);

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
   * @return member's name
   */
  public String getMemberName() {
    return getContent(this.nameFld);
  }

  /**
   * Sets member's name.
   * 
   * @param name the new name
   */
  public void setMemberName(String name) {
    this.nameFld.setText(name);
  }

  /**
   * @return first name
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
    return CalendarUtil.parseDate(this.birthFld.getText());
  }

  /**
   * Sets the birthday.
   * 
   * @param date the new date
   */
  public void setBirthDate(Optional<Date> date) {
    this.birthFld.setText(CalendarUtil.formatDate(date).orElse(""));
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
    return CalendarUtil.parseDate(this.deathFld.getText());
  }

  /**
   * Sets the death date.
   * 
   * @param date the date
   */
  public void setDeathDate(Optional<Date> date) {
    this.deathFld.setText(CalendarUtil.formatDate(date).orElse(""));
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
    Date birth = CalendarUtil.parseDate(this.birthFld.getText());
    Date death = CalendarUtil.parseDate(this.deathFld.getText());

    return birth == null || death == null || (birth != null && death != null && birth.before(death));
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
      FamilyMember member = new FamilyMember(getId(), this.image, getMemberName(), getFirstName(), getGender(), getBirthDate(),
          getBirthLocation(), getDeathDate(), getDeathLocation());

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
    String text = field.getText();
    return text.length() > 0 ? text : null;
  }

  /**
   * Tells if at least one field has been completed.
   * 
   * @return true if a field has been completed
   */
  boolean atLeastOneFieldCompleted() {
    boolean ok = false;

    ok |= this.nameFld.getText().length() > 0;
    ok |= this.firstNameFld.getText().length() > 0;
    ok |= this.genderCombo.getSelectedItem() != Gender.UNKNOW;
    ok |= this.birthFld.getText().length() > 0;
    ok |= this.deathFld.getText().length() > 0;

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
