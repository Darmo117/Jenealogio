package net.darmo_creations.model.graph;

import java.util.Set;

public abstract class Link<X extends Node<X>> {
  protected abstract Set<X> getChildren();
}
