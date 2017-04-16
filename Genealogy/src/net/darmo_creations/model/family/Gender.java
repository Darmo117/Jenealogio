package net.darmo_creations.model.family;

public enum Gender {
  UNKNOW("Inconnu", ""),
  MAN("Homme", "M"),
  WOMAN("Femme", "F");

  private final String name;
  private final String code;

  private Gender(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public boolean isMan() {
    return this == MAN;
  }

  public boolean isWoman() {
    return this == WOMAN;
  }

  public String getCode() {
    return this.code;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public static Gender fromCode(String code) {
    for (Gender g : Gender.values()) {
      if (g.getCode().equals(code))
        return g;
    }
    return null;
  }
}
