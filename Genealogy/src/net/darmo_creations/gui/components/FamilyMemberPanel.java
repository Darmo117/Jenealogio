package net.darmo_creations.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Period;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import net.darmo_creations.gui.components.drag.Dragable;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.ImageUtil;

public class FamilyMemberPanel extends JPanel implements Dragable {
  private static final long serialVersionUID = 2109771883765681092L;

  private static final String UNKNOWN_DATA = "?";
  private static final Border SELECTED_BORDER = new LineBorder(Color.BLUE.brighter(), 2);
  private static final Border UNSELECTED_BORDER = new LineBorder(Color.GRAY, 2);
  public static final Color UNKNOW_GENDER_COLOR = Color.GRAY;
  public static final Color MAN_COLOR = new Color(117, 191, 255);
  public static final Color WOMAN_COLOR = new Color(255, 183, 255);

  private PanelModel model;

  private JLabel nameLbl;
  private JLabel birthLbl;
  private JLabel birthPlaceLbl;
  private JLabel weddingLbl;
  private JLabel weddingPlaceLbl;
  private JLabel deathLbl;
  private JLabel deathPlaceLbl;
  private JLabel ageLbl;

  public FamilyMemberPanel(FamilyMember familyMember, Wedding wedding) {
    super(new BorderLayout());
    setPreferredSize(new Dimension(200, 110));
    setLayout(new BorderLayout());

    this.model = new PanelModel(familyMember.getId());

    JPanel infoPnl = new JPanel(new GridBagLayout());
    infoPnl.setOpaque(false);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.weightx = gbc.weighty = 1;

    gbc.gridx = 1;
    gbc.gridwidth = 5;
    infoPnl.add(this.nameLbl = new JLabel(), gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(ImageUtil.BABY), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 8;
    infoPnl.add(this.birthLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel("à"), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 8;
    infoPnl.add(this.birthPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(ImageUtil.HEART), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 8;
    infoPnl.add(this.weddingLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel("à"), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 8;
    infoPnl.add(this.weddingPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(ImageUtil.CROSS), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 8;
    infoPnl.add(this.deathLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel("à"), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 8;
    infoPnl.add(this.deathPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 1;
    gbc.gridy = 4;
    infoPnl.add(this.ageLbl = new JLabel(), gbc);

    add(infoPnl, BorderLayout.CENTER);

    setInfo(familyMember, wedding);
    setSelected(false);
  }

  public void setInfo(FamilyMember familyMember, Wedding wedding) {
    switch (familyMember.getGender()) {
      case UNKNOW:
        setBackground(UNKNOW_GENDER_COLOR);
        break;
      case MAN:
        setBackground(MAN_COLOR);
        break;
      case WOMAN:
        setBackground(WOMAN_COLOR);
        break;
    }

    this.nameLbl.setText(getFullName(familyMember.getName(), familyMember.getFirstName()));
    this.birthLbl.setText(getDate(familyMember.getBirthDate()));
    this.birthPlaceLbl.setText(familyMember.getBirthPlace().orElse(UNKNOWN_DATA));
    this.weddingLbl.setText(wedding != null ? getDate(wedding.getDate()) : UNKNOWN_DATA);
    this.weddingPlaceLbl.setText(wedding != null ? wedding.getLocation().orElse(UNKNOWN_DATA) : UNKNOWN_DATA);
    this.deathLbl.setText(getDate(familyMember.getDeathDate()));
    this.deathPlaceLbl.setText(familyMember.getDeathPlace().orElse(UNKNOWN_DATA));
    this.ageLbl.setText(getAge(familyMember.getAge()));
    revalidate();
  }

  private String getFullName(Optional<String> name, Optional<String> firstName) {
    String fullName = UNKNOWN_DATA;

    if (firstName.isPresent())
      fullName = firstName.get();
    if (name.isPresent())
      fullName += " " + name.get();
    else if (firstName.isPresent())
      fullName += " " + UNKNOWN_DATA;

    return fullName;
  }

  private String getAge(Optional<Period> period) {
    if (period.isPresent()) {
      Period p = period.get();
      int years = p.getYears();
      int months = p.getMonths();
      String res = years + " an" + (years > 1 ? "s" : "");

      if (months > 0)
        res += months + " mois";

      return res;
    }
    return UNKNOWN_DATA;
  }

  private String getDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      return String.format("%d/%02d/%d", d.getDate(), d.getMonth(), d.getYear());
    }
    return UNKNOWN_DATA;
  }

  @Override
  public void setLocation(Point p) {
    super.setLocation(p);
    getParent().repaint();
  }

  public boolean isSelected() {
    return this.model.isSelected();
  }

  public void setSelected(boolean selected) {
    this.model.setSelected(selected);
    setBorder(selected ? SELECTED_BORDER : UNSELECTED_BORDER);
  }

  @Override
  public void doClick() {
    fireActionPerformed();
  }

  public void addActionListener(ActionListener l) {
    this.listenerList.add(ActionListener.class, l);
  }

  public void removeActionListener(ActionListener l) {
    this.listenerList.remove(ActionListener.class, l);
  }

  private void fireActionPerformed() {
    ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "select:" + this.model.getId(), System.currentTimeMillis(), ActionEvent.ACTION_PERFORMED);

    for (ActionListener l : this.listenerList.getListeners(ActionListener.class)) {
      l.actionPerformed(e);
    }
  }

  private class PanelModel {
    private boolean selected;
    private final long id;

    public PanelModel(long id) {
      this.selected = false;
      this.id = id;
    }

    public boolean isSelected() {
      return this.selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    public long getId() {
      return this.id;
    }
  }
}
