package net.darmo_creations.model.graph;

import java.util.Optional;

public abstract class Graph<X extends Node<X>, U extends Link<X>> {
  protected abstract Optional<X> getLeftAncestorForNode(Node<X> node);

  protected abstract Optional<X> getRightAncestorForNode(Node<X> node);

  protected abstract Optional<U> getLinkForNode(Node<X> node);
}
