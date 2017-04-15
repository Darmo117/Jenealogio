package net.darmo_creations.gui.dialog.link;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  private LinkController controller;
  private JFormattedTextField dateFld;
  private JTextField locationFld;
  private JTextField spouse1Field, spouse2Field;
  private JList<FamilyMember> childrenList, availChildrenList;
  private JButton addBtn, removeBtn;

  public LinkDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);

    this.controller = new LinkController(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        LinkDialog.this.dateFld.requestFocus();
      }
    });

    DateFormat dateFormat = new SimpleDateFormat(I18n.getLocalizedString("date.format"));
    this.dateFld = new JFormattedTextField(dateFormat);
    this.dateFld.addKeyListener(this.controller);
    this.locationFld = new JTextField();
    this.spouse1Field = new JTextField();
    this.spouse1Field.setEnabled(false);
    this.spouse2Field = new JTextField();
    this.spouse2Field.setEnabled(false);
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

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.wedding_date.text")), gbc);
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.wedding_location.text")), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.spouse1.text")), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.spouse2.text")), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.children.text")), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 7;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.available_children.text")), gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 0;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.locationFld, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.spouse1Field, gbc);
    gbc.gridy = 3;
    fieldsPnl.add(this.spouse2Field, gbc);
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);
    gbc.gridy = 7;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JPanel p = new JPanel();
    p.add(this.addBtn);
    p.add(this.removeBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 8;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.availChildrenList), gbc);

    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
  }

  public void addLink(FamilyMember spouse1, FamilyMember spouse2, Set<FamilyMember> children) {
    setTitle(I18n.getLocalizedString("dialog.add_link.title"));
    this.controller.reset(spouse1, spouse2, children);
  }

  public void updateLink(Wedding wedding, Set<FamilyMember> children) {
    setTitle(I18n.getLocalizedString("dialog.update_link.title"));
    this.controller.reset(wedding, children);
  }

  public Optional<Wedding> getLink() {
    if (!isCanceled())
      return Optional.of(this.controller.getWedding());
    return Optional.empty();
  }

  void setAddButtonEnabled(boolean enabled) {
    this.addBtn.setEnabled(enabled);
  }

  void setDeleteButtonEnabled(boolean enabled) {
    this.removeBtn.setEnabled(enabled);
  }

  void setSpouse1(String str) {
    this.spouse1Field.setText(str);
  }

  void setSpouse2(String str) {
    this.spouse2Field.setText(str);
  }

  Date getDate() {
    return parseDate(this.dateFld);
  }

  void setDate(Optional<Date> date) {
    this.dateFld.setText(formatDate(date));
  }

  String getWeddingLocation() {
    return this.locationFld.getText();
  }

  void setWeddingLocation(Optional<String> location) {
    this.locationFld.setText(location.orElse(""));
  }

  FamilyMember[] getChildren() {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    FamilyMember[] children = new FamilyMember[model.size()];

    for (int i = 0; i < model.size(); i++) {
      children[i] = model.getElementAt(i);
    }

    return children;
  }

  void setChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
  }

  void setAvailableChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
  }

  void addSelectedChildren() {
    transfertItems(this.availChildrenList, this.childrenList);
    if (this.availChildrenList.isSelectionEmpty())
      this.addBtn.setEnabled(false);
  }

  void removeSelectedChildren() {
    transfertItems(this.childrenList, this.availChildrenList);
    if (this.childrenList.isSelectionEmpty())
      this.removeBtn.setEnabled(false);
  }

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

  void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }

  private String formatDate(Optional<Date> date) {
    if (date.isPresent())
      return I18n.getFormattedDate(date.get());
    return "";
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
}
