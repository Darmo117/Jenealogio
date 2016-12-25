package net.darmo_creations.model.graph;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class Node<T extends Node<T>> {
  /**
   * Retourne les enfants du noeud dans le graphe donné.
   */
  protected <U extends Link<T>> Set<T> getChildren(Graph<T, U> graph) {
    Set<T> set = new HashSet<>();
    Optional<U> link = graph.getLinkForNode(this);

    if (link.isPresent())
      set.addAll(link.get().getChildren());

    return set;
  }
}
