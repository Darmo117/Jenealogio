package net.darmo_creations.util;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import net.darmo_creations.model.Date;

public class CalendarUtil {
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

  private CalendarUtil() {}
}
