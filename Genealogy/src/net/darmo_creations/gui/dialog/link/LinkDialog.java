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

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import net.darmo_creations.model.family.DummyFamilyMember;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.ImageUtil;

public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  private LinkController controller;
  private JFormattedTextField dateFld;
  private JTextField locationFld;
  private JComboBox<FamilyMember> husbandCombo, wifeCombo;
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

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    this.dateFld = new JFormattedTextField(dateFormat);
    this.dateFld.addKeyListener(this.controller);
    this.locationFld = new JTextField();
    this.husbandCombo = new JComboBox<>();
    this.husbandCombo.setName("husband");
    this.husbandCombo.addItemListener(this.controller);
    this.wifeCombo = new JComboBox<>();
    this.wifeCombo.setName("wife");
    this.wifeCombo.addItemListener(this.controller);
    this.childrenList = new JList<>(new DefaultListModel<>());
    this.childrenList.addListSelectionListener(this.controller);
    this.childrenList.setName("children");
    this.availChildrenList = new JList<>(new DefaultListModel<>());
    this.availChildrenList.addListSelectionListener(this.controller);
    this.availChildrenList.setName("available-children");
    this.addBtn = new JButton(ImageUtil.ARROW_UP);
    this.addBtn.setActionCommand("add");
    this.addBtn.addActionListener(this.controller);
    this.removeBtn = new JButton(ImageUtil.ARROW_DOWN);
    this.removeBtn.setActionCommand("remove");
    this.removeBtn.addActionListener(this.controller);

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 2;
    fieldsPnl.add(new JLabel("Date"), gbc);
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel("Endroit"), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel("Époux"), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel("Épouse"), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(new JLabel("Enfants"), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 7;
    fieldsPnl.add(new JLabel("Enfants disponibles"), gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 0;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.locationFld, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.husbandCombo, gbc);
    gbc.gridy = 3;
    fieldsPnl.add(this.wifeCombo, gbc);
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

  public void setLink(Wedding wedding, Family family) {
    if (wedding != null) {
      setTitle("Modifier un lien");
      this.dateFld.setText(formatDate(wedding.getDate()));
      this.locationFld.setText(wedding.getLocation().orElse(""));
    }
    else {
      setTitle("Ajouter un lien");
      this.dateFld.setText("");
      this.locationFld.setText("");
    }

    this.husbandCombo.setEnabled(wedding == null);
    this.wifeCombo.setEnabled(wedding == null);

    this.controller.reset(wedding, family);

    setCanceled(false);
    setAddButtonEnabled(false);
    setDeleteButtonEnabled(false);
    setValidateButtonEnabled(false);
  }

  public Optional<Wedding> getLink() {
    if (!isCanceled()) {
      // @f0
      Wedding wedding = new Wedding(
          parseDate(this.dateFld),
          this.locationFld.getText().length() > 0 ? this.locationFld.getText() : null,
          (FamilyMember) this.husbandCombo.getSelectedItem(),
          (FamilyMember) this.wifeCombo.getSelectedItem(),
          getChildren());
      // @f1

      return Optional.of(wedding);
    }
    return Optional.empty();
  }

  boolean isHusbandSelected() {
    return this.husbandCombo.getSelectedIndex() > 0;
  }

  boolean isWifeSelected() {
    return this.wifeCombo.getSelectedIndex() > 0;
  }

  void setAddButtonEnabled(boolean enabled) {
    this.addBtn.setEnabled(enabled);
  }

  void setDeleteButtonEnabled(boolean enabled) {
    this.removeBtn.setEnabled(enabled);
  }

  FamilyMember getSelectedHusband() {
    return (FamilyMember) this.husbandCombo.getSelectedItem();
  }

  void setHusbandCombo(Set<FamilyMember> potentialHusbands, FamilyMember memberToSelect) {
    setCombo(this.husbandCombo, potentialHusbands, memberToSelect);
  }

  FamilyMember getSelectedWife() {
    return (FamilyMember) this.wifeCombo.getSelectedItem();
  }

  void setWifeCombo(Set<FamilyMember> potentialWives, FamilyMember memberToSelect) {
    setCombo(this.wifeCombo, potentialWives, memberToSelect);
  }

  private void setCombo(JComboBox<FamilyMember> combo, Set<FamilyMember> members, FamilyMember memberToSelect) {
    ((DefaultComboBoxModel<FamilyMember>) combo.getModel()).removeAllElements();
    combo.addItem(new DummyFamilyMember("Sélectionner..."));
    members.forEach(member -> combo.addItem(member));

    if (memberToSelect != null)
      combo.setSelectedItem(memberToSelect);
    else
      combo.setSelectedIndex(0);
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

  /**
   * Met à jour les listes et combos en fonction des sélections.
   */
  void updateLists() {
    DefaultComboBoxModel<FamilyMember> husbandComboModel = (DefaultComboBoxModel<FamilyMember>) this.husbandCombo.getModel();
    DefaultComboBoxModel<FamilyMember> wifeComboModel = (DefaultComboBoxModel<FamilyMember>) this.wifeCombo.getModel();
    DefaultListModel<FamilyMember> childrenListModel = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    DefaultListModel<FamilyMember> availChildrenListModel = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();

    if (isHusbandSelected())
      availChildrenListModel.removeElement(getSelectedHusband());
    if (isWifeSelected())
      availChildrenListModel.removeElement(getSelectedWife());

    for (int i = 0; i < childrenListModel.size(); i++) {
      husbandComboModel.removeElement(childrenListModel.getElementAt(i));
      wifeComboModel.removeElement(childrenListModel.getElementAt(i));
    }
  }

  void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }

  private String formatDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      return String.format("%02d/%02d/%d", d.getYear(), d.getMonth(), d.getDate());
    }
    else
      return "";
  }

  private Date parseDate(JFormattedTextField field) throws DateTimeException {
    String str = field.getText();

    if (str.length() > 0) {
      Matcher matcher = DATE_PATTERN.matcher(str);
      if (matcher.matches()) {
        int year = Integer.parseInt(matcher.group(3));
        int month = Integer.parseInt(matcher.group(2));
        int date = Integer.parseInt(matcher.group(1));

        return new Date(year, month, date);
      }

      throw new DateTimeException("wrong date format");
    }

    return null;
  }

  private FamilyMember[] getChildren() {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    int size = model.size();
    FamilyMember[] children = new FamilyMember[size];

    for (int i = 0; i < size; i++) {
      children[i] = model.getElementAt(i);
    }

    return children;
  }
}
