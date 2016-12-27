package net.darmo_creations.dao;

import java.io.IOException;

import net.darmo_creations.model.family.Family;

public class FamilyDao {
  private static FamilyDao instance;

  public static FamilyDao instance() {
    if (instance == null)
      instance = new FamilyDao();
    return instance;
  }

  private FamilyDao() {}

  public Family open(String file) throws IOException {
    // TODO
    return null;
  }

  public void save(Family family) throws IOException {
    // TODO
  }
}
