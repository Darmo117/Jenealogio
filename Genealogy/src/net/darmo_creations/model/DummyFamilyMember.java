package net.darmo_creations.model;

import java.util.Objects;

public class DummyFamilyMember extends FamilyMember {
  public DummyFamilyMember(String name) {
    super(-1, null, Objects.requireNonNull(name), null, Gender.UNKNOW, null, null);
  }

  @Override
  public String toString() {
    return getName().get();
  }
}
