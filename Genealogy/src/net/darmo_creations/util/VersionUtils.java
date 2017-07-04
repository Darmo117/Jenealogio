package net.darmo_creations.util;

public final class VersionUtils {
  /** Current version */
  public static final int CURRENT_VERSION;
  /** Current version formatted */
  public static final String CURRENT_VERSION_STR;

  /** Version 1.3 */
  public static final int V1_3 = getVersion(1, 3, 0, false);

  static {
    CURRENT_VERSION = getVersion(1, 3, 0, true);
    CURRENT_VERSION_STR = format(CURRENT_VERSION);
  }

  /**
   * @return the value for the given version
   */
  public static int getVersion(int major, int minor, int patch, boolean dev) {
    return (major & 0xff) << 16 | (minor & 0xff) << 8 | (patch & 0xff) | (dev ? 1 : 0) << 31;
  }

  /**
   * Formats a version as {@code major.minor[.patch][d]} (patch is added only if it is not 0). The
   * "d" indicates if the version is in development.
   * 
   * @param version the version
   * @return the formatted version
   */
  public static String format(int version) {
    int major = (version >> 16) & 0xff;
    int minor = (version >> 8) & 0xff;
    int patch = version & 0xff;
    String dev = ((version >> 31) & 1) == 1 ? "d" : "";

    if (patch != 0)
      return String.format("%d.%d.%d%s", major, minor, patch, dev);
    return String.format("%d.%d%s", major, minor, dev);
  }

  private VersionUtils() {}
}
