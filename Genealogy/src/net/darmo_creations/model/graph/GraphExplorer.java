package net.darmo_creations.model.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GraphExplorer<X extends Node<X>, U extends Link<X>> {
  private Graph<X, U> graph;
  private List<X> visitedNodes;

  public GraphExplorer(Graph<X, U> graph) {
    this.visitedNodes = new ArrayList<>();
    this.graph = graph;
  }

  /**
   * Retourne l'ensemble des sommets du sous-graphe connexe contenant le sommet donné.
   * 
   * @param node le noeud de départ
   * @return l'ensemble des sommets du sous-graphe connexe
   */
  public Set<X> explore(X node) {
    this.visitedNodes.clear();
    Set<X> nodes = new HashSet<>();

    this.visitedNodes.add(node);
    nodes.add(node);
    // Exploration récursive des descendants et de leurs ascendants.
    for (X n : node.getChildren(this.graph)) {
      if (!alreadyVisited(n)) {
        this.visitedNodes.add(n);
        nodes.addAll(explore(node));
      }
    }

    Optional<X> leftAncestor = this.graph.getLeftAncestorForNode(node);
    Optional<X> rightAncestor = this.graph.getRightAncestorForNode(node);

    // Exploration récursive des ascendants.
    if (leftAncestor.isPresent() && !alreadyVisited(leftAncestor.get()))
      nodes.addAll(explore(leftAncestor.get()));
    if (rightAncestor.isPresent() && !alreadyVisited(rightAncestor.get()))
      nodes.addAll(explore(rightAncestor.get()));

    return nodes;
  }

  /**
   * Retourne l'ensemble des descendants du sommet donné (et le sommet lui-même).
   * 
   * @param node le noeud de départ
   * @return l'ensemble des descendants du noeud et le noeud lui-même
   */
  public Set<X> exploreChildren(X node) {
    this.visitedNodes.clear();
    Set<X> nodes = new HashSet<>();

    this.visitedNodes.add(node);
    nodes.add(node);
    // Exploration récursive des descendants.
    for (X n : node.getChildren(this.graph)) {
      if (!alreadyVisited(n)) {
        this.visitedNodes.add(n);
        nodes.addAll(exploreChildren(node));
      }
    }

    return nodes;
  }

  private boolean alreadyVisited(X node) {
    return this.visitedNodes.contains(node);
  }
}
