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

  private JFileChooser fileChooser;

  private long id;
  private BufferedImage image;
  private JLabel imageLbl;
  private JTextField nameFld, firstNameFld, birthPlaceFld, deathPlaceFld;
  private JComboBox<Gender> genderCombo;
  private JFormattedTextField birthFld, deathFld;

  public CardDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setPreferredSize(new Dimension(300, 410));
    setResizable(false);

    CardController controller = new CardController(this);

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
    imageBtn.addActionListener(controller);
    imageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    imageBtn.setFocusPainted(false);
    this.nameFld = new JTextField();
    this.nameFld.getDocument().addDocumentListener(controller);
    this.firstNameFld = new JTextField();
    this.firstNameFld.getDocument().addDocumentListener(controller);
    this.genderCombo = new JComboBox<>(Gender.values());
    this.genderCombo.addItemListener(controller);
    DateFormat dateFormat = new SimpleDateFormat(I18n.getLocalizedString("date.format"));
    this.birthFld = new JFormattedTextField(dateFormat);
    this.birthFld.addKeyListener(controller);
    this.birthFld.getDocument().addDocumentListener(controller);
    this.birthPlaceFld = new JTextField();
    this.birthPlaceFld.getDocument().addDocumentListener(controller);
    this.deathFld = new JFormattedTextField(dateFormat);
    this.deathFld.addKeyListener(controller);
    this.deathFld.getDocument().addDocumentListener(controller);
    this.deathPlaceFld = new JTextField();
    this.deathPlaceFld.getDocument().addDocumentListener(controller);

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
    fieldsPnl.add(this.birthPlaceFld, gbc);
    gbc.gridy = 5;
    fieldsPnl.add(this.deathFld, gbc);
    gbc.gridy = 6;
    fieldsPnl.add(this.deathPlaceFld, gbc);

    add(imagePnl, BorderLayout.NORTH);
    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(controller);

    pack();
    setLocationRelativeTo(owner);
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

  // TODO tout déplacer dans le contrôleur.
  public void setCard(FamilyMember member) {
    if (member != null) {
      setTitle(I18n.getLocalizedString("dialog.update_card.title"));
      this.id = member.getId();
      setImage(member.getImage());
      this.nameFld.setText(member.getName().orElse(""));
      this.firstNameFld.setText(member.getFirstName().orElse(""));
      this.genderCombo.setSelectedIndex(member.getGender().ordinal());
      this.birthFld.setText(getDate(member.getBirthDate()));
      this.birthPlaceFld.setText(member.getBirthPlace().orElse(""));
      this.deathFld.setText(getDate(member.getDeathDate()));
      this.deathPlaceFld.setText(member.getDeathPlace().orElse(""));
    }
    else {
      setTitle(I18n.getLocalizedString("dialog.add_card.title"));
      this.id = -1;
      setImage(Optional.empty());
      this.nameFld.setText("");
      this.firstNameFld.setText("");
      this.genderCombo.setSelectedIndex(Gender.UNKNOW.ordinal());
      this.birthFld.setText("");
      this.birthPlaceFld.setText("");
      this.deathFld.setText("");
      this.deathPlaceFld.setText("");
    }

    setCanceled(false);
    setValidateButtonEnabled(false);
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

  private String getDate(Optional<Date> date) {
    if (date.isPresent())
      return I18n.getFormattedDate(date.get());
    return "";
  }

  public Optional<FamilyMember> getCard() {
    if (!isCanceled()) {
      FamilyMember member = new FamilyMember(this.id, this.image, getContent(this.nameFld), getContent(this.firstNameFld),
          Gender.values()[this.genderCombo.getSelectedIndex()], parseDate(this.birthFld), getContent(this.birthPlaceFld), parseDate(this.deathFld),
          getContent(this.deathPlaceFld));

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
