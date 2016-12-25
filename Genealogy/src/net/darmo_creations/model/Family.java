package net.darmo_creations.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import net.darmo_creations.model.graph.Graph;
import net.darmo_creations.model.graph.GraphExplorer;
import net.darmo_creations.model.graph.Node;

public class Family extends Graph<FamilyMember, Wedding> {
  private long id;
  private Set<FamilyMember> members;
  private Set<Wedding> weddings;

  public Family() {
    this.id = 0;
    this.members = new HashSet<>();
    this.weddings = new HashSet<>();
  }

  public long getNextMemberId() {
    return this.id++;
  }

  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny();
  }

  public void addMember(FamilyMember member) {
    if (!this.members.contains(member))
      this.members.add(member);
  }

  public void updateMember(FamilyMember member) {
    Optional<FamilyMember> optional = getMember(member.getId());

    if (optional.isPresent()) {
      FamilyMember m = optional.get();

      m.setImage(member.getImage().orElse(null));
      m.setName(member.getName().orElse(null));
      m.setFirstName(member.getFirstName().orElse(null));
      m.setGender(member.getGender());
      m.setBirthDate(member.getBirthDate().orElse(null));
      m.setDeathDate(member.getDeathDate().orElse(null));
    }
  }

  public void removeMember(FamilyMember member) {
    for (Iterator<Wedding> it = this.weddings.iterator(); it.hasNext();) {
      Wedding wedding = it.next();
      if (wedding.isMarried(member))
        it.remove();
      else if (wedding.isChild(member))
        wedding.removeChild(member);
    }
    this.members.remove(member);
  }

  public Optional<Wedding> getWedding(FamilyMember member) {
    return getLinkForNode(member);
  }

  public void addWedding(Wedding wedding) {
    if (!this.weddings.contains(wedding))
      this.weddings.add(wedding);
  }

  public void updateWedding(Wedding wedding) {
    Optional<Wedding> optional = getWedding(wedding.getHusband());

    if (optional.isPresent()) {
      Wedding w = optional.get();

      w.setDate(wedding.getDate().orElse(null));
      Set<FamilyMember> children = w.getChildren();
      children.forEach(child -> w.removeChild(child));
      wedding.getChildren().forEach(child -> w.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  /**
   * Retourne tous les hommes non encore mariés.
   * 
   * @return les hommes célibataires
   */
  public Set<FamilyMember> getPotentialHusbands() {
    // Condition : les personnes doivent être des hommes célibataires qui ne font pas encore partie
    // de la famille.
    Set<FamilyMember> men = new HashSet<>();

    // TODO

    return men;
  }

  /**
   * Retourne toutes les femmes non encore mariées.
   * 
   * @return les femmes célibataires
   */
  public Set<FamilyMember> getPotentialWives() {
    // Condition : les personnes doivent être des femmes célibataires qui ne font pas encore partie
    // de la famille.
    Set<FamilyMember> women = new HashSet<>();

    // TODO

    return women;
  }

  /**
   * Retourne toutes le personnes éligibles pour être les enfants du couple donné. Si l'argument est
   * null, tous les membres de la famille sont retournés.
   * 
   * @param wedding le mariage
   * @return la liste des enfants potentiels
   */
  public Set<FamilyMember> getPotentialChildren(Wedding wedding) {
    // Condition : les personnes ne doivent pas déjà faire partie de la famille des deux conjoints.
    Set<FamilyMember> candidates = new HashSet<>(this.members);
    Set<FamilyMember> connectedSet = new HashSet<>();
    GraphExplorer<FamilyMember, Wedding> explorer = new GraphExplorer<>(this);

    // Inutile de visiter le conjoint puisqu'il fait forcément partie de la même composante connexe.
    connectedSet.addAll(explorer.explore(wedding.getHusband()));
    // Soustraction ensembliste.
    candidates.removeAll(connectedSet);

    return candidates;
  }

  @Override
  protected Optional<Wedding> getLinkForNode(Node<FamilyMember> node) {
    return this.weddings.stream().filter(wedding -> wedding.getHusband().equals(node) || wedding.getWife().equals(node)).findAny();
  }

  @Override
  protected Optional<FamilyMember> getLeftAncestorForNode(Node<FamilyMember> node) {
    Optional<Wedding> optional = this.weddings.stream().filter(wedding -> wedding.getChildren().contains(node)).findAny();

    if (optional.isPresent())
      return Optional.of(optional.get().getHusband());

    return Optional.empty();
  }

  @Override
  protected Optional<FamilyMember> getRightAncestorForNode(Node<FamilyMember> node) {
    Optional<Wedding> optional = this.weddings.stream().filter(wedding -> wedding.getChildren().contains(node)).findAny();

    if (optional.isPresent())
      return Optional.of(optional.get().getWife());

    return Optional.empty();
  }
}