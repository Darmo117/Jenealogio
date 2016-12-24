package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import net.darmo_creations.controller.LinkController;
import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.DummyFamilyMember;
import net.darmo_creations.model.FamilyMember;
import net.darmo_creations.model.Wedding;

public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  private LinkController controller;
  private JFormattedTextField dateFld;
  private JComboBox<FamilyMember> husbandCombo, wifeCombo;
  private JList<FamilyMember> childrenList;
  private JButton deleteBtn;

  public LinkDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setPreferredSize(new Dimension(300, 220));
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
    JButton addBtn = new JButton("Ajouter");
    addBtn.setActionCommand("add");
    addBtn.addActionListener(this.controller);
    this.deleteBtn = new JButton("Supprimer");
    this.deleteBtn.setActionCommand("delete");
    this.deleteBtn.addActionListener(this.controller);

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridy = 0;
    fieldsPnl.add(new JLabel("Date"), gbc);
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel("Époux"), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel("Épouse"), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel("Enfants"), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(addBtn, gbc);
    gbc.gridy = 5;
    fieldsPnl.add(this.deleteBtn, gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.gridy = 0;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.husbandCombo, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.wifeCombo, gbc);
    gbc.gridy = 3;
    gbc.gridheight = 3;
    gbc.fill = GridBagConstraints.BOTH;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);

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

  public void setDeleteButtonEnabled(boolean enabled) {
    this.deleteBtn.setEnabled(enabled);
  }

  public FamilyMember getSelectedHusband() {
    return (FamilyMember) this.husbandCombo.getSelectedItem();
  }

  public void setHusbandCombo(List<FamilyMember> potentialHusbands, FamilyMember memberToSelect) {
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

  public void setWifeCombo(List<FamilyMember> potentialWives, FamilyMember memberToSelect) {
    ((DefaultComboBoxModel<FamilyMember>) this.wifeCombo.getModel()).removeAllElements();
    this.wifeCombo.addItem(new DummyFamilyMember("Sélectionner..."));
    potentialWives.forEach(woman -> this.wifeCombo.addItem(woman));
    if (memberToSelect != null)
      this.wifeCombo.setSelectedItem(memberToSelect);
    else
      this.wifeCombo.setSelectedIndex(0);
  }

  public List<FamilyMember> getChildrenList() {
    List<FamilyMember> children = new ArrayList<>();
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    int size = model.size();

    for (int i = 0; i < size; i++) {
      children.add(model.getElementAt(i));
    }

    return children;
  }

  public void setLink(Wedding wedding, List<FamilyMember> potentialHusbands, List<FamilyMember> potentialWives, List<FamilyMember> potentialChildren) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();

    if (wedding != null) {
      setTitle("Modifier un lien");
      this.dateFld.setText(getDate(wedding.getDate()));
      model.removeAllElements();
      wedding.getChildren().forEach(child -> model.addElement(child));
    }
    else {
      setTitle("Ajouter un lien");
      this.dateFld.setText("");
    }

    this.controller.reset(wedding, potentialHusbands, potentialWives, potentialChildren);

    setDeleteButtonEnabled(false);
    setValidateButtonEnabled(false);
  }

  private String getDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      return String.format("%02d/%02d/%d", d.getYear(), d.getMonth(), d.getDate());
    }
    else
      return "";
  }

  public Optional<Wedding> getLink() {
    if (!isCanceled()) {
      DefaultListModel<FamilyMember> list = ((DefaultListModel<FamilyMember>) this.childrenList.getModel());
      FamilyMember[] children = new FamilyMember[list.size()];

      for (int i = 0; i < list.size(); i++)
        children[i] = list.getElementAt(i);

      Wedding wedding = new Wedding(parseDate(this.dateFld), (FamilyMember) this.husbandCombo.getSelectedItem(), (FamilyMember) this.wifeCombo.getSelectedItem(), children);

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

      throw new DateTimeException("");
    }

    return null;
  }

  public void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }
}
