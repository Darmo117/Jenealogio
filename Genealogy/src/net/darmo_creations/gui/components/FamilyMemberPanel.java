package net.darmo_creations.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.Period;
import java.util.Optional;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.FamilyMember;
import net.darmo_creations.model.Wedding;

public class FamilyMemberPanel extends JPanel {
  private static final long serialVersionUID = 2109771883765681092L;

  private final long id;
  private JLabel imageLbl;
  private JLabel nameLbl;
  private JLabel birthLbl;
  private JLabel weddingLbl;
  private JLabel deathLbl;
  private JLabel ageLbl;

  public FamilyMemberPanel(FamilyMember familyMember, Optional<Wedding> wedding, boolean detailled) {
    setLayout(new BorderLayout());

    this.id = familyMember.getId();
    JPanel infoPnl = new JPanel(new GridLayout());
    infoPnl.add(this.nameLbl = new JLabel());
    infoPnl.add(this.birthLbl = new JLabel());
    if (detailled)
      infoPnl.add(this.weddingLbl = new JLabel());
    infoPnl.add(this.deathLbl = new JLabel());
    if (detailled) {
      infoPnl.add(this.ageLbl = new JLabel());
      add(this.imageLbl = new JLabel(), BorderLayout.CENTER);
    }

    add(infoPnl, BorderLayout.SOUTH);

    setInfo(familyMember, wedding);

    setPreferredSize(new Dimension(200, 500));
  }

  public void setInfo(FamilyMember familyMember, Optional<Wedding> wedding) {
    this.imageLbl.setIcon((Icon) null);
    this.nameLbl.setText(getFullName(familyMember.getName(), familyMember.getFirstName()));
    this.birthLbl.setText(getDate(familyMember.getBirthDate()));
    this.weddingLbl.setText(wedding.isPresent() ? getDate(wedding.get().getDate()) : "-");
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

  public long getId() {
    return this.id;
  }
}
