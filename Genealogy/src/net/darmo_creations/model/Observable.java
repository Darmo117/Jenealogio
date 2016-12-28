package net.darmo_creations.model;

public interface Observable {
  void addObserver(Observer observer);

  void removeObserver(Observer observer);
}
