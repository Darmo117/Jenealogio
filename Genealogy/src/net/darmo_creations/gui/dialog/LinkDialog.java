package net.darmo_creations.gui.dialog;

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
import javax.swing.border.EmptyBorder;

import net.darmo_creations.controllers.LinkController;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.DummyFamilyMember;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.ImageUtil;

public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  private LinkController controller;
  private JFormattedTextField dateFld;
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
    this.husbandCombo = new JComboBox<>();
    this.husbandCombo.addItemListener(this.controller);
    this.wifeCombo = new JComboBox<>();
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
    fieldsPnl.add(new JLabel("Époux"), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel("Épouse"), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel("Enfants"), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 6;
    fieldsPnl.add(new JLabel("Enfants disponibles"), gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 0;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.husbandCombo, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.wifeCombo, gbc);
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);
    gbc.gridy = 6;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JPanel p = new JPanel();
    p.add(this.addBtn);
    p.add(this.removeBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 7;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.availChildrenList), gbc);

    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
  }

  public boolean isHusbandSelected() {
    return this.husbandCombo.getSelectedIndex() > 0;
  }

  public boolean isWifeSelected() {
    return this.wifeCombo.getSelectedIndex() > 0;
  }

  public void setAddButtonEnabled(boolean enabled) {
    this.addBtn.setEnabled(enabled);
  }

  public void setDeleteButtonEnabled(boolean enabled) {
    this.removeBtn.setEnabled(enabled);
  }

  public FamilyMember getSelectedHusband() {
    return (FamilyMember) this.husbandCombo.getSelectedItem();
  }

  public void setHusbandCombo(Set<FamilyMember> potentialHusbands, FamilyMember memberToSelect) {
    ((DefaultComboBoxModel<FamilyMember>) this.husbandCombo.getModel()).removeAllElements();
    this.husbandCombo.addItem(new DummyFamilyMember("Sélectionner..."));
    potentialHusbands.forEach(man -> this.husbandCombo.addItem(man));
    if (memberToSelect != null)
      this.husbandCombo.setSelectedItem(memberToSelect);
    else
      this.husbandCombo.setSelectedIndex(0);
  }

  public FamilyMember getSelectedWife() {
    return (FamilyMember) this.wifeCombo.getSelectedItem();
  }

  public void setWifeCombo(Set<FamilyMember> potentialWives, FamilyMember memberToSelect) {
    ((DefaultComboBoxModel<FamilyMember>) this.wifeCombo.getModel()).removeAllElements();
    this.wifeCombo.addItem(new DummyFamilyMember("Sélectionner..."));
    potentialWives.forEach(woman -> this.wifeCombo.addItem(woman));
    if (memberToSelect != null)
      this.wifeCombo.setSelectedItem(memberToSelect);
    else
      this.wifeCombo.setSelectedIndex(0);
  }

  public void setAvailableChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
  }

  public void addSelectedChildren() {
    transfertItems(this.availChildrenList, this.childrenList);
    if (this.availChildrenList.isSelectionEmpty())
      this.addBtn.setEnabled(false);
  }

  public void removeSelectedChildren() {
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
      updateLists();
    }
  }

  /**
   * Met à jour les listes et combos en fonction des sélections.
   */
  public void updateLists() {
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

  public void setLink(Wedding wedding, Set<FamilyMember> potentialHusbands, Set<FamilyMember> potentialWives, Set<FamilyMember> potentialChildren) {
    DefaultListModel<FamilyMember> model1 = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    DefaultListModel<FamilyMember> model2 = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();

    if (wedding != null) {
      setTitle("Modifier un lien");
      this.dateFld.setText(formatDate(wedding.getDate()));
      model1.removeAllElements();
      wedding.getChildren().forEach(child -> model1.addElement(child));
      model2.removeAllElements();
      potentialChildren.forEach(child -> model2.addElement(child));
    }
    else {
      setTitle("Ajouter un lien");
      this.dateFld.setText("");
    }

    this.controller.reset(wedding, potentialHusbands, potentialWives, potentialChildren);

    setAddButtonEnabled(false);
    setDeleteButtonEnabled(false);
    setValidateButtonEnabled(false);
  }

  private String formatDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      return String.format("%02d/%02d/%d", d.getYear(), d.getMonth(), d.getDate());
    }
    else
      return "";
  }

  public Optional<Wedding> getLink() {
    if (!isCanceled()) {
      // @f0
      Wedding wedding = new Wedding(
          parseDate(this.dateFld),
          (FamilyMember) this.husbandCombo.getSelectedItem(),
          (FamilyMember) this.wifeCombo.getSelectedItem(),
          getChildren());
      // @f1

      return Optional.of(wedding);
    }
    return Optional.empty();
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

  public void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }
}
