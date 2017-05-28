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
package net.darmo_creations.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Period;
import java.util.Optional;
import java.util.StringJoiner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.CalendarUtil;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

/**
 * This class displays all data of a person.
 *
 * @author Damien Vergnet
 */
public class DetailsPanel extends JPanel {
  private static final long serialVersionUID = 2109771883765681092L;

  public static final String UNKNOWN_DATA = "?";

  private JLabel nameLbl;
  private JLabel birthLbl;
  private JLabel weddingLbl;
  private JLabel deathLbl;
  private JLabel ageLbl;
  private JTextArea commentLbl;

  /**
   * Creates an empty panel.
   */
  public DetailsPanel() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(300, 180));
    setLayout(new BorderLayout());

    this.nameLbl = new JLabel();
    this.nameLbl.setHorizontalAlignment(JLabel.CENTER);
    this.nameLbl.setBorder(new EmptyBorder(0, 10, 0, 10));
    add(this.nameLbl, BorderLayout.NORTH);

    JPanel infoPnl = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets.left = 10;
    infoPnl.add(new JLabel(Images.BABY), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 10, 0, 10);
    infoPnl.add(this.birthLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets.left = 10;
    infoPnl.add(new JLabel(Images.HEART), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 10, 0, 10);
    infoPnl.add(this.weddingLbl = new JLabel(), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets.left = 10;
    infoPnl.add(new JLabel(Images.CROSS), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.BASELINE;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 10, 0, 10);
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
    JScrollPane scroll = new JScrollPane(this.commentLbl = new JTextArea());
    this.commentLbl.setFont(Font.getFont("Tahoma"));
    this.commentLbl.setEditable(false);
    // scroll.setPreferredSize(new Dimension(-1, 80));
    infoPnl.add(scroll, gbc);

    add(infoPnl, BorderLayout.CENTER);
  }

  /**
   * Sets displayed data.
   * 
   * @param member the person
   * @param wedding the wedding it is part of
   */
  public void setInfo(FamilyMember member, Wedding wedding) {
    String name = member.toString() + (member.getUseName().isPresent() && member.getFamilyName().isPresent()
        ? " " + I18n.getLocalizedWord("born", member.isWoman(), false) + " " + member.getFamilyName().get() : "");
    String birth = formatDateAndLocation(member.getBirthDate(), member.getBirthLocation());
    String weddingS = wedding != null ? formatDateAndLocation(wedding.getDate(), wedding.getLocation()) : UNKNOWN_DATA;
    String death = formatDateAndLocation(member.getDeathDate(), member.getDeathLocation());

    this.nameLbl.setText(name);
    this.nameLbl.setIcon(member.isDead() ? Images.CROSS : null);
    this.birthLbl.setText(birth);
    this.weddingLbl.setText(weddingS);
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
    String inWord = I18n.getLocalizedWord("in", false, false);

    if (date.isPresent() && location.isPresent()) {
      return String.format("%s %s %s", CalendarUtil.formatDate(date).get(), inWord, location.get());
    }
    if (!date.isPresent() && location.isPresent()) {
      return String.format("%s %s", inWord, location.get());
    }
    if (date.isPresent() && !location.isPresent()) {
      return CalendarUtil.formatDate(date).get();
    }

    return UNKNOWN_DATA;
  }
}
