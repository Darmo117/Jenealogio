package net.darmo_creations.util;

import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.darmo_creations.model.Date;

/**
 * Useful functions to handle dates.
 * 
 * @author Damien Vergnet
 */
public class CalendarUtil {
  /**
   * @return the current date
   */
  public static Date getCurrentDate() {
    long offset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
    // Avoid null if no ID found
    String id = "";

    for (String i : TimeZone.getAvailableIDs()) {
      TimeZone tz = TimeZone.getTimeZone(i);

      if (tz.getRawOffset() == offset) {
        id = i;
        break;
      }
    }
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone(id), Locale.FRENCH);

    return new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Formats the given date.
   * 
   * @param date the date
   * @return the formatted date or nothing if there is no date
   */
  public static Optional<String> formatDate(Optional<Date> date) {
    if (date.isPresent())
      return Optional.of(I18n.getFormattedDate(date.get()));
    return Optional.empty();
  }

  private static final Pattern DATE_PATTERN = Pattern.compile("(\\d+{1,2})/(\\d+{2})/(\\d+{4})");

  /**
   * Returns the date from the given string. It uses localized date pattern.
   * 
   * @param field the textfield
   * @return the date
   * @throws DateTimeParseException if the date was in the wrong format
   */
  public static Date parseDate(String str) throws DateTimeParseException {
    if (str.length() > 0) {
      Matcher matcher = DATE_PATTERN.matcher(str);
      if (matcher.matches()) {
        String format = I18n.getLocalizedString("date.format");
        boolean monthFirst = format.startsWith("M");

        int year = Integer.parseInt(matcher.group(3));
        int month = Integer.parseInt(matcher.group(monthFirst ? 1 : 2));
        int date = Integer.parseInt(matcher.group(monthFirst ? 2 : 1));

        return new Date(year, month, date);
      }

      throw new DateTimeParseException("wrong date format", str, -1);
    }

    return null;
  }

  private CalendarUtil() {}
}
