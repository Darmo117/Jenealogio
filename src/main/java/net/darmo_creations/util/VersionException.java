package net.darmo_creations.util;

public class VersionException extends Exception {
  private static final long serialVersionUID = 6117608149659458527L;

  private Version targetVersion, actualVersion;

  public VersionException(Version targetVersion, Version actualVersion) {
    super("version mismatch: target '" + targetVersion + "', actual '" + actualVersion + "'");
    this.targetVersion = targetVersion;
    this.actualVersion = actualVersion;
  }

  public Version getTargetVersion() {
    return this.targetVersion;
  }

  public Version getActualVersion() {
    return this.actualVersion;
  }
}
