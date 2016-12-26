package net.darmo_creations.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import net.darmo_creations.model.graph.Link;

public final class Wedding extends Link<FamilyMember> implements Cloneable {
  private Date date;
  private FamilyMember husband;
  private FamilyMember wife;
  private Set<FamilyMember> children;

  public Wedding(Date date, FamilyMember husband, FamilyMember wife, FamilyMember... children) {
    if (husband.equals(wife))
      throw new IllegalArgumentException("husband and wife must be different");
    setDate(date != null ? date.clone() : null);
    setHusband(husband);
    setWife(wife);
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

  public FamilyMember getHusband() {
    return this.husband;
  }

  void setHusband(FamilyMember husband) {
    this.husband = husband;
  }

  public FamilyMember getWife() {
    return this.wife;
  }

  void setWife(FamilyMember wife) {
    this.wife = wife;
  }

  public boolean isChild(FamilyMember member) {
    return this.children.contains(member);
  }

  @Override
  public Set<FamilyMember> getChildren() {
    return new TreeSet<>(this.children);
  }

  public void addChild(FamilyMember child) {
    Objects.requireNonNull(child);
    if (child.equals(getHusband()) || child.equals(getWife()))
      throw new IllegalArgumentException("can't be their own child");
    if (this.children.contains(child.getId()))
      throw new IllegalArgumentException("child already present");
    this.children.add(child);
  }

  public void removeChild(FamilyMember child) {
    this.children.remove(child.getId());
  }

  public boolean isMarried(FamilyMember member) {
    return getHusband().equals(member) || getWife().equals(member);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + getHusband().hashCode();
    result = prime * result + getWife().hashCode();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Wedding) {
      Wedding wedding = (Wedding) o;
      return getHusband() == wedding.getHusband() && getWife() == wedding.getWife();
    }
    return false;
  }

  @Override
  public Wedding clone() {
    try {
      Wedding w = (Wedding) super.clone();

      getDate().ifPresent(date -> w.setDate(date));
      w.setHusband(getHusband().clone());
      w.setWife(getWife().clone());
      w.children = new HashSet<>();
      getChildren().forEach(child -> w.addChild(child));

      return w;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
