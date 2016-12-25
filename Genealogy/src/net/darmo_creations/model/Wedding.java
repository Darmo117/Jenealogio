package net.darmo_creations.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import net.darmo_creations.model.graph.Link;

public final class Wedding extends Link<FamilyMember> {
  private Date date;
  private FamilyMember husband;
  private FamilyMember wife;
  private List<FamilyMember> children;

  public Wedding(Date date, FamilyMember husband, FamilyMember wife, FamilyMember... children) {
    if (husband.equals(wife))
      throw new IllegalArgumentException("husband and wife must be different");
    setDate(date);
    setHusband(husband);
    setWife(wife);
    this.children = new ArrayList<>();
    for (FamilyMember child : this.children)
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
    this.husband = Objects.requireNonNull(husband);
  }

  public FamilyMember getWife() {
    return this.wife;
  }

  void setWife(FamilyMember wife) {
    this.wife = Objects.requireNonNull(wife);
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
    if (this.children.contains(child))
      throw new IllegalArgumentException("child already present");
    this.children.add(child);
  }

  public void removeChild(FamilyMember child) {
    this.children.remove(child);
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
      return getHusband().equals(wedding.getHusband()) && getWife().equals(wedding.getWife());
    }
    return false;
  }
}
