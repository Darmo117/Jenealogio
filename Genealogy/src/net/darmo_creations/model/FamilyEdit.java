package net.darmo_creations.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import net.darmo_creations.controllers.Undoable;
import net.darmo_creations.model.family.Family;

public class FamilyEdit implements Undoable {
  private final Family family;
  private final Map<Long, Point> locations;

  public FamilyEdit(Family family, Map<Long, Point> locations) {
    this.family = family.clone();
    this.locations = new HashMap<>(locations);
  }

  public Family getFamily() {
    return this.family.clone();
  }

  public Map<Long, Point> getLocations() {
    return new HashMap<>(this.locations);
  }
}
