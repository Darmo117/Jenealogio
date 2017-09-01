/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.jenealogio.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.CalendarUtil;
import net.darmo_creations.jenealogio.util.I18n;
import net.darmo_creations.jenealogio.util.Images;

/**
 * This class displays all data of a person.
 *
 * @author Damien Vergnet
 */
public class DetailsPanel extends JPanel {
  private static final long serialVersionUID = 2109771883765681092L;

  public static final String UNKNOWN_DATA = "?";

  private JLabel nameLbl;
  private JLabel otherNamesLbl;
  private JLabel birthLbl;
  private RelationsPanel relationsPnl;
  private JLabel deathLbl;
  private JLabel ageLbl;
  private JTextArea commentLbl;

  /**
   * Creates an empty panel.
   */
  public DetailsPanel() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(300, 200));
    setLayout(new BorderLayout());

    JPanel namesPnl = new JPanel(new GridLayout(2, 1));
    this.nameLbl = new JLabel();
    this.nameLbl.setHorizontalAlignment(JLabel.CENTER);
    this.nameLbl.setBorder(new EmptyBorder(0, 10, 0, 10));
    namesPnl.add(this.nameLbl);
    this.otherNamesLbl = new JLabel();
    this.otherNamesLbl.setHorizontalAlignment(JLabel.CENTER);
    this.otherNamesLbl.setFont(this.otherNamesLbl.getFont().deriveFont(Font.ITALIC));
    namesPnl.add(this.otherNamesLbl);
    add(namesPnl, BorderLayout.NORTH);

    JPanel infoPnl = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets = new Insets(5, 10, 0, 10);
    infoPnl.add(new JLabel(Images.BABY), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    infoPnl.add(this.birthLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    JScrollPane scroll = new JScrollPane(this.relationsPnl = new RelationsPanel());
    scroll.setBorder(null);
    infoPnl.add(scroll, gbc);

    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    infoPnl.add(new JLabel(Images.TOMBSTONE), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    infoPnl.add(this.deathLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    infoPnl.add(this.ageLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = gbc.weighty = 1.;
    scroll = new JScrollPane(this.commentLbl = new JTextArea());
    this.commentLbl.setFont(Font.getFont("Tahoma"));
    this.commentLbl.setEditable(false);
    infoPnl.add(scroll, gbc);

    add(infoPnl, BorderLayout.CENTER);
  }

  /**
   * Sets displayed data.
   * 
   * @param member the person
   * @param relation the relations it is part of
   */
  public void setInfo(FamilyMember member, Set<Relationship> relations) {
    String name = member.toString() + (member.getUseName().isPresent() ? " (" + member.getUseName().get() + ")" : "");
    String birth = formatDateAndLocation(member.getBirthDate(), member.getBirthLocation());
    String death = formatDateAndLocation(member.getDeathDate(), member.getDeathLocation());

    this.nameLbl.setText(name);
    this.nameLbl.setIcon(member.isDead() ? Images.TOMBSTONE : null);
    this.otherNamesLbl.setText(member.getOtherNames().orElse(""));
    this.birthLbl.setText(birth);
    this.relationsPnl.setRelations(relations);
    this.deathLbl.setText(death);
    this.ageLbl.setText(getAge(member.getAge()));
    this.commentLbl.setText(member.getComment().orElse(""));
    revalidate();
  }

  /**
   * Returns the age of the dislayed member.
   * 
   * @param period the time period
   * @return the formatted age
   */
  private String getAge(Optional<Period> period) {
    if (period.isPresent()) {
      Period p = period.get();
      int years = p.getYears();
      int months = p.getMonths();
      int days = p.getDays();
      StringJoiner res = new StringJoiner(" ");

      if (years > 0 || years == 0 && months == 0 && days == 0)
        res.add(years + " " + I18n.getLocalizedWord("year", false, years > 1));
      if (months > 0)
        res.add(months + " " + I18n.getLocalizedWord("month", false, months > 1));
      if (days > 0)
        res.add(days + " " + I18n.getLocalizedWord("day", false, days > 1));

      return res.toString();
    }

    return UNKNOWN_DATA;
  }

  /**
   * Formats the given date and location.
   * 
   * @param date the date
   * @param location the location
   * @return the formatted string
   */
  private String formatDateAndLocation(Optional<Date> date, Optional<String> location) {
    if (date.isPresent() && location.isPresent()) {
      return String.format("%s (%s)", CalendarUtil.formatDate(date).get(), location.get());
    }
    if (!date.isPresent() && location.isPresent()) {
      return location.get();
    }
    if (date.isPresent() && !location.isPresent()) {
      return CalendarUtil.formatDate(date).get();
    }

    return UNKNOWN_DATA;
  }

  private class RelationsPanel extends JPanel {
    private static final long serialVersionUID = 5243823817553503487L;

    public RelationsPanel() {
      super(new GridBagLayout());
    }

    public void setRelations(Set<Relationship> relations) {
      Set<Relationship> rel = new TreeSet<>(relations);

      removeAll();
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 10, 0, 10);

      int i = 0;
      for (Relationship r : rel) {
        gbc.gridy = i;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets.left = 0;
        add(new JLabel(r.hasEnded() ? Images.HEART_BROKEN : Images.HEART), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.BASELINE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets.left = 10;
        String relationStr = formatDateAndLocation(r.getDate(), r.getLocation());
        if (r.hasEnded())
          relationStr += " - " + CalendarUtil.formatDate(r.getEndDate()).orElse(UNKNOWN_DATA);
        add(new JLabel(relationStr), gbc);
        i++;
      }

      revalidate();
    }
  }
}
