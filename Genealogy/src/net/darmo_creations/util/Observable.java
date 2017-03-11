package net.darmo_creations.util;

public interface Observable {
  void addObserver(Observer observer);

  void removeObserver(Observer observer);
}
