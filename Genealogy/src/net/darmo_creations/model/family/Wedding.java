package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import net.darmo_creations.model.Date;
import net.darmo_creations.util.Nullable;

/**
 * This class represents a wedding between two persons. It holds the date, location and children of
 * the marriage.
 * 
 * @author Damien Vergnet
 */
public final class Wedding {
  private Date date;
  private String location;
  private FamilyMember spouse1, spouse2;
  private Set<FamilyMember> children;

  /**
   * Creates a new wedding. The two spouses must be different or else an IllegalArgumentException
   * will be thrown.
   * 
   * @param date the date
   * @param location the location
   * @param spouse1 one spouse
   * @param spouse2 the other spouse
   * @param children the children
   */
  public Wedding(@Nullable Date date, @Nullable String location, FamilyMember spouse1, FamilyMember spouse2, FamilyMember... children) {
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

  /**
   * @return the wedding's date
   */
  public Optional<Date> getDate() {
    return Optional.ofNullable(this.date);
  }

  /**
   * Sets the wedding's date. May be null.
   * 
   * @param date the new date
   */
  void setDate(@Nullable Date date) {
    this.date = date;
  }

  /**
   * @return the wedding's location
   */
  public Optional<String> getLocation() {
    return Optional.ofNullable(this.location);
  }

  /**
   * Sets the wedding's location. May be null.
   * 
   * @param location the new location
   */
  void setLocation(@Nullable String location) {
    this.location = location;
  }

  /**
   * @return the first spouse
   */
  public FamilyMember getSpouse1() {
    return this.spouse1;
  }

  /**
   * Sets the first spouse.
   * 
   * @param spouse1 the new spouse
   */
  void setSpouse1(FamilyMember spouse1) {
    this.spouse1 = spouse1;
  }

  /**
   * @return the second spouse
   */
  public FamilyMember getSpouse2() {
    return this.spouse2;
  }

  /**
   * Sets the second spouse.
   * 
   * @param spouse2 the second spouse
   */
  void setSpouse2(FamilyMember spouse2) {
    this.spouse2 = spouse2;
  }

  /**
   * Tells if the given person is a child from this wedding.
   * 
   * @param member the person
   * @return true if and only if the person is a child from this wedding
   */
  public boolean isChild(FamilyMember member) {
    return this.children.contains(member);
  }

  /**
   * @return a set of all the children
   */
  public Set<FamilyMember> getChildren() {
    return new TreeSet<>(this.children);
  }

  /**
   * Adds a child to this wedding. The child must be different from the two spouses and not already
   * present in the children list.
   * 
   * @param child the child to add
   */
  public void addChild(FamilyMember child) {
    Objects.requireNonNull(child);
    if (child.equals(getSpouse1()) || child.equals(getSpouse2()))
      throw new IllegalArgumentException("can't be their own child");
    if (this.children.contains(child.getId()))
      throw new IllegalArgumentException("child already present");
    this.children.add(child);
  }

  /**
   * Deletes the given child.
   * 
   * @param child the child to delete
   */
  public void removeChild(FamilyMember child) {
    this.children.remove(child);
  }

  /**
   * Tells if the given person is one of the two spouses from this wedding.
   * 
   * @param member the person
   * @return true if and only if the person is one of the spouses
   */
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
  public String toString() {
    return getSpouse1() + "<->" + getSpouse2();
  }
}
