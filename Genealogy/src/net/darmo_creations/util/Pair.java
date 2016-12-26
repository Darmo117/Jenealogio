package net.darmo_creations.util;

public class Pair<T1, T2> {
  private T1 leftElement;
  private T2 rightElement;

  public Pair(T1 leftElement, T2 rightElement) {
    this.leftElement = leftElement;
    this.rightElement = rightElement;
  }

  public T1 getLeftElement() {
    return this.leftElement;
  }

  public void setLeftElement(T1 leftElement) {
    this.leftElement = leftElement;
  }

  public boolean isLeftElementPresent() {
    return getLeftElement() != null;
  }

  public T2 getRightElement() {
    return this.rightElement;
  }

  public void setRightElement(T2 rightElement) {
    this.rightElement = rightElement;
  }

  public boolean isRightElementPresent() {
    return getRightElement() != null;
  }
}