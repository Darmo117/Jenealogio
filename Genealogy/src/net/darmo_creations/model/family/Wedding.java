package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import net.darmo_creations.model.Date;

public final class Wedding implements Cloneable {
  private Date date;
  private String location;
  private FamilyMember spouse1, spouse2;
  private Set<FamilyMember> children;

  public Wedding(Date date, String location, FamilyMember spouse1, FamilyMember spouse2, FamilyMember... children) {
    if (spouse1.equals(spouse2))
      throw new IllegalArgumentException("husband and wife must be different");
    setDate(date != null ? date.clone() : null);
    setLocation(location);
    setSpouse1(spouse1);
    setSpouse2(spouse2);
    this.children = new HashSet<>();
    for (FamilyMember child : children)
      addChild(child);
  }

  public Optional<Date> getDate() {
    return Optional.ofNullable(this.date);
  }

  void setDate(Date date) {
    this.date = date;
  }

  public Optional<String> getLocation() {
    return Optional.ofNullable(this.location);
  }

  void setLocation(String location) {
    this.location = location;
  }

  public FamilyMember getSpouse1() {
    return this.spouse1;
  }

  void setSpouse1(FamilyMember husband) {
    this.spouse1 = husband;
  }

  public FamilyMember getSpouse2() {
    return this.spouse2;
  }

  void setSpouse2(FamilyMember wife) {
    this.spouse2 = wife;
  }

  public boolean isChild(FamilyMember member) {
    return this.children.contains(member);
  }

  public Set<FamilyMember> getChildren() {
    return new TreeSet<>(this.children);
  }

  public void addChild(FamilyMember child) {
    Objects.requireNonNull(child);
    if (child.equals(getSpouse1()) || child.equals(getSpouse2()))
      throw new IllegalArgumentException("can't be their own child");
    if (this.children.contains(child.getId()))
      throw new IllegalArgumentException("child already present");
    this.children.add(child);
  }

  public void removeChild(FamilyMember child) {
    this.children.remove(child);
  }

  public boolean isMarried(FamilyMember member) {
    return getSpouse1().equals(member) || getSpouse2().equals(member);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + getSpouse1().hashCode();
    result = prime * result + getSpouse2().hashCode();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Wedding) {
      Wedding wedding = (Wedding) o;
      return getSpouse1() == wedding.getSpouse1() && getSpouse2() == wedding.getSpouse2();
    }
    return false;
  }

  @Override
  public Wedding clone() {
    try {
      Wedding w = (Wedding) super.clone();

      getDate().ifPresent(date -> w.setDate(date));
      w.setSpouse1(getSpouse1().clone());
      w.setSpouse2(getSpouse2().clone());
      w.children = new HashSet<>();
      getChildren().forEach(child -> w.addChild(child));

      return w;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public String toString() {
    return getSpouse1() + "<->" + getSpouse2();
  }
}
