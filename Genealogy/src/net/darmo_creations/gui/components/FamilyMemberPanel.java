package net.darmo_creations.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Period;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.darmo_creations.gui.components.drag.Dragable;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.FamilyMember;
import net.darmo_creations.model.Wedding;

public class FamilyMemberPanel extends JPanel implements Dragable {
  private static final long serialVersionUID = 2109771883765681092L;

  private PanelModel model;

  private JLabel imageLbl;
  private JLabel nameLbl;
  private JLabel birthLbl;
  private JLabel weddingLbl;
  private JLabel deathLbl;
  private JLabel ageLbl;

  public FamilyMemberPanel(FamilyMember familyMember, Wedding wedding, boolean detailled) {
    setLayout(new BorderLayout());
    setBorder(new LineBorder(Color.BLACK));

    this.model = new PanelModel(familyMember.getId(), true);

    JPanel infoPnl = new JPanel(new GridLayout(5, 1));
    infoPnl.add(this.nameLbl = new JLabel());
    infoPnl.add(this.birthLbl = new JLabel());
    infoPnl.add(this.weddingLbl = new JLabel());
    infoPnl.add(this.deathLbl = new JLabel());
    infoPnl.add(this.ageLbl = new JLabel());
    add(this.imageLbl = new JLabel(), BorderLayout.NORTH);

    add(infoPnl, BorderLayout.CENTER);

    setInfo(familyMember, wedding);
    setDetailledMode(detailled);

    setPreferredSize(new Dimension(200, 500));
  }

  public void setDetailledMode(boolean detailled) {
    this.weddingLbl.setVisible(detailled);
    this.ageLbl.setVisible(detailled);
    this.imageLbl.setVisible(detailled);
  }

  public void setInfo(FamilyMember familyMember, Wedding wedding) {
    this.imageLbl.setIcon((Icon) null);
    this.nameLbl.setText(getFullName(familyMember.getName(), familyMember.getFirstName()));
    this.birthLbl.setText(getDate(familyMember.getBirthDate()));
    this.weddingLbl.setText(wedding != null ? getDate(wedding.getDate()) : "-");
    this.deathLbl.setText(getDate(familyMember.getDeathDate()));
    this.ageLbl.setText(getAge(familyMember.getAge()));
    revalidate();
  }

  private String getFullName(Optional<String> name, Optional<String> firstName) {
    String fullName = "-";

    if (name.isPresent())
      fullName = name.get();
    if (firstName.isPresent())
      fullName += " " + firstName.get();

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
    return "-";
  }

  private String getDate(Optional<Date> date) {
    if (date.isPresent()) {
      Date d = date.get();
      return String.format("%d/%2d/%d", d.getDate(), d.getMonth(), d.getYear());
    }
    return "-";
  }

  @Override
  public boolean isSelected() {
    return this.model.isSelected();
  }

  @Override
  public void setSelected(boolean selected) {
    this.model.setSelected(selected);
    if (selected)
      fireActionPerformed();
  }

  private void fireActionPerformed() {
    ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "" + this.model.getId(), System.currentTimeMillis(), 0);

    for (ActionListener l : this.listenerList.getListeners(ActionListener.class)) {
      l.actionPerformed(e);
    }
  }
}
