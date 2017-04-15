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
import java.time.DateTimeException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import net.darmo_creations.util.I18n;

public class CardDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  private CardController controller;

  private JFileChooser fileChooser;

  private long id;
  private BufferedImage image;
  private JLabel imageLbl;
  private JTextField nameFld, firstNameFld, birthLocationFld, deathLocationFld;
  private JComboBox<Gender> genderCombo;
  private JFormattedTextField birthFld, deathFld;

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
    this.genderCombo = new JComboBox<>(Gender.values());
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

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return this.id;
  }

  public String getMemberName() {
    return getContent(this.nameFld);
  }

  public void setMemberName(String name) {
    this.nameFld.setText(name);
  }

  public String getFirstName() {
    return getContent(this.firstNameFld);
  }

  public void setFirstName(String firstName) {
    this.firstNameFld.setText(firstName);
  }

  public Gender getGender() {
    return Gender.values()[this.genderCombo.getSelectedIndex()];
  }

  public void setGender(Gender gender) {
    this.genderCombo.setSelectedIndex(gender.ordinal());
  }

  public Date getBirthDate() {
    return parseDate(this.birthFld);
  }

  public void setBirthDate(Optional<Date> date) {
    this.birthFld.setText(getDate(date));
  }

  public String getBirthLocation() {
    return getContent(this.birthLocationFld);
  }

  public void setBirthLocation(String location) {
    this.birthLocationFld.setText(location);
  }

  public Date getDeathDate() {
    return parseDate(this.deathFld);
  }

  public void setDeathDate(Optional<Date> date) {
    this.deathFld.setText(getDate(date));
  }

  public String getDeathLocation() {
    return getContent(this.deathLocationFld);
  }

  public void setDeathLocation(String location) {
    this.deathLocationFld.setText(location);
  }

  private String getDate(Optional<Date> date) {
    if (date.isPresent())
      return I18n.getFormattedDate(date.get());
    return "";
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

  private boolean checkDates() {
    Date birth = parseDate(this.birthFld);
    Date death = parseDate(this.deathFld);

    return birth == null || death == null || (birth != null && death != null && birth.before(death));
  }

  File showOpenFileChooser() {
    this.fileChooser.showOpenDialog(this);
    return this.fileChooser.getSelectedFile();
  }

  public void setCard(FamilyMember member) {
    this.controller.reset(member);
  }

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

  public Optional<FamilyMember> getCard() {
    if (!isCanceled()) {
      FamilyMember member = new FamilyMember(getId(), this.image, getMemberName(), getFirstName(), getGender(), getBirthDate(),
          getBirthLocation(), getDeathDate(), getDeathLocation());

      return Optional.of(member);
    }
    return Optional.empty();
  }

  private String getContent(JTextField field) {
    String text = field.getText();
    return text.length() > 0 ? text : null;
  }

  private Date parseDate(JFormattedTextField field) throws DateTimeException {
    String str = field.getText();

    if (str.length() > 0) {
      Matcher matcher = DATE_PATTERN.matcher(str);

      if (matcher.matches()) {
        String format = I18n.getLocalizedString("date.format");
        boolean monthFirst = format.startsWith("M");

        int year = Integer.parseInt(matcher.group(3));
        int month = Integer.parseInt(matcher.group(monthFirst ? 1 : 2));
        int date = Integer.parseInt(matcher.group(monthFirst ? 2 : 1));

        return new Date(year, month, date);
      }

      throw new DateTimeException("wrong date format");
    }

    return null;
  }

  boolean atLeastOneFieldCompleted() {
    boolean ok = false;

    ok |= this.nameFld.getText().length() > 0;
    ok |= this.firstNameFld.getText().length() > 0;
    ok |= this.genderCombo.getSelectedItem() != Gender.UNKNOW;
    ok |= this.birthFld.getText().length() > 0;
    ok |= this.deathFld.getText().length() > 0;

    return ok;
  }

  void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }
}
