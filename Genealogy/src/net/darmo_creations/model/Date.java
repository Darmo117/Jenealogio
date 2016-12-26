package net.darmo_creations.model;

import java.util.Calendar;

/**
 * Classe représentant une date. La représentation interne est gérée par une instance de Calendar.
 * 
 * @author Darmo
 */
public final class Date implements Comparable<Date>, Cloneable {
  private final Calendar calendar;

  /**
   * Crée une date à partir d'une année, d'un mois et d'un jour dans le mois.
   * 
   * @param year l'année
   * @param month le mois (1 pour janvier)
   * @param date le jour dans le mois (1 pour le premier)
   */
  public Date(int year, int month, int date) {
    this.calendar = Calendar.getInstance();
    this.calendar.set(year, month - 1, date);
  }

  /**
   * @return l'année
   */
  public int getYear() {
    return this.calendar.get(Calendar.YEAR);
  }

  /**
   * @return le mois (1 pour janvier)
   */
  public int getMonth() {
    return this.calendar.get(Calendar.MONTH) + 1;
  }

  /**
   * @return le jour dans le mois (1 pour le premier)
   */
  public int getDate() {
    return this.calendar.get(Calendar.DAY_OF_MONTH);
  }

  public boolean before(Date date) {
    return this.calendar.before(date.calendar);
  }

  public boolean after(Date date) {
    return this.calendar.after(date.calendar);
  }

  @Override
  public int compareTo(Date date) {
    return this.calendar.compareTo(date.calendar);
  }

  @Override
  public Date clone() {
    return new Date(getYear(), getMonth(), getDate());
  }
}
