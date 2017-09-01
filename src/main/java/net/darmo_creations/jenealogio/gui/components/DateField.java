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

import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.Optional;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import net.darmo_creations.jenealogio.controllers.FormattedFieldFocusListener;
import net.darmo_creations.jenealogio.model.date.Date;
import net.darmo_creations.jenealogio.model.date.DateBuilder;
import net.darmo_creations.utils.Nullable;

/**
 * This component lets the user type in a date. The format can be specified.
 *
 * @author Damien Vergnet
 */
public class DateField extends JPanel {
  private static final long serialVersionUID = -2920530937602825641L;

  private JFormattedTextField yearFld, monthFld, dateFld;

  /**
   * Creates a date field.
   * 
   * @param format the format (D/M/Y, M/D/Y or Y/M/D)
   * @param alignment FlowLayout alignment
   */
  public DateField(String format, int alignment) {
    ((FlowLayout) getLayout()).setAlignment(alignment);
    ((FlowLayout) getLayout()).setVgap(0);
    ((FlowLayout) getLayout()).setHgap(0);

    DecimalFormat f = new DecimalFormat("0000");
    this.yearFld = new JFormattedTextField(new NumberFormatter(f));
    this.yearFld.setColumns(4);
    this.yearFld.addFocusListener(new FormattedFieldFocusListener(this.yearFld));
    this.yearFld.addFocusListener(new FormattedDatesFocusListener(this.yearFld, 4));

    f = new DecimalFormat("00");
    this.monthFld = new JFormattedTextField(new NumberFormatter(f));
    this.monthFld.setColumns(2);
    this.monthFld.addFocusListener(new FormattedFieldFocusListener(this.monthFld));
    this.monthFld.addFocusListener(new FormattedDatesFocusListener(this.monthFld, 2));

    f = new DecimalFormat("00");
    this.dateFld = new JFormattedTextField(new NumberFormatter(f));
    this.dateFld.setColumns(2);
    this.dateFld.addFocusListener(new FormattedFieldFocusListener(this.dateFld));
    this.dateFld.addFocusListener(new FormattedDatesFocusListener(this.dateFld, 2));

    String sep = " / ";
    switch (format) {
      case "D/M/Y":
        add(this.dateFld);
        add(new JLabel(sep));
        add(this.monthFld);
        add(new JLabel(sep));
        add(this.yearFld);
        break;
      case "M/D/Y":
        add(this.monthFld);
        add(new JLabel(sep));
        add(this.dateFld);
        add(new JLabel(sep));
        add(this.yearFld);
        break;
      case "Y/M/D":
        add(this.yearFld);
        add(new JLabel(sep));
        add(this.monthFld);
        add(new JLabel(sep));
        add(this.dateFld);
        break;
      default:
        throw new IllegalArgumentException("unknown format: " + format);
    }
  }

  /**
   * Registers the given observer to begin receiving notifications when changes are made to the
   * document.
   * 
   * @param listener the observer to register
   */
  public void addDocumentListener(DocumentListener listener) {
    this.yearFld.getDocument().addDocumentListener(listener);
    this.monthFld.getDocument().addDocumentListener(listener);
    this.dateFld.getDocument().addDocumentListener(listener);
  }

  /**
   * Unregisters the given observer from the notification list so it will no longer receive change
   * updates.
   * 
   * @param listener the observer to remove
   */
  public void removeDocumentListener(DocumentListener listener) {
    this.yearFld.getDocument().removeDocumentListener(listener);
    this.monthFld.getDocument().removeDocumentListener(listener);
    this.dateFld.getDocument().removeDocumentListener(listener);
  }

  /**
   * Sets the displayed date. May be null.
   * 
   * @param date the new date
   */
  public void setDate(@Nullable Date date) {
    this.yearFld.setText(date != null && date.isYearSet() ? String.format("%04d", date.getYear()) : "");
    this.monthFld.setText(date != null && date.isMonthSet() ? String.format("%02d", date.getMonth()) : "");
    this.dateFld.setText(date != null && date.isDateSet() ? String.format("%02d", date.getDate()) : "");
  }

  /**
   * @return the date
   */
  public Optional<Date> getDate() {
    DateBuilder builder = new DateBuilder();

    if (this.yearFld.getText().matches("^\\d{4}$"))
      builder.setYear(Integer.parseInt(this.yearFld.getText()));
    if (this.monthFld.getText().matches("^\\d{2}$"))
      builder.setMonth(Integer.parseInt(this.monthFld.getText()));
    if (this.dateFld.getText().matches("^\\d{2}$"))
      builder.setDate(Integer.parseInt(this.dateFld.getText()));

    return Optional.ofNullable(builder.getDate());
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.yearFld.setEnabled(enabled);
    this.monthFld.setEnabled(enabled);
    this.dateFld.setEnabled(enabled);
    super.setEnabled(enabled);
  }

  /**
   * This listener empties the associated field if the input is not a valid positive integer.
   *
   * @author Damien Vergnet
   */
  private class FormattedDatesFocusListener extends FocusAdapter {
    private JFormattedTextField field;
    private int digitsNb;

    /**
     * Creates a listener.
     * 
     * @param field the field to listen to
     * @param digitsNb the number of accepted digits
     */
    public FormattedDatesFocusListener(JFormattedTextField field, int digitsNb) {
      this.field = field;
      this.digitsNb = digitsNb;
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (!this.field.getText().matches("^\\d{0," + this.digitsNb + "}$")) {
        this.field.setText("");
      }
    }
  }
}
