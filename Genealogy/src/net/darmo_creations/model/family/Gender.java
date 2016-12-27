package net.darmo_creations.model.family;

public enum Gender {
  UNKNOW("Inconnu"),
  MAN("Homme"),
  WOMAN("Femme");

  private final String name;

  private Gender(String name) {
    this.name = name;
  }

  public boolean isMan() {
    return this == MAN;
  }

  public boolean isWoman() {
    return this == WOMAN;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
