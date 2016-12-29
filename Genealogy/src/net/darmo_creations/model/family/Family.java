package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import net.darmo_creations.model.graph.Graph;
import net.darmo_creations.model.graph.GraphExplorer;
import net.darmo_creations.model.graph.Node;

public class Family extends Graph<FamilyMember, Wedding> {
  private long globalId;
  private String name;
  private Set<FamilyMember> members;
  private Set<Wedding> weddings;

  public Family(String name) {
    this.globalId = 0;
    this.name = name;
    this.members = new HashSet<>();
    this.weddings = new HashSet<>();
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<FamilyMember> getAllMembers() {
    Set<FamilyMember> members = new HashSet<>();
    this.members.forEach(member -> members.add(member.clone()));
    return members;
  }

  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny();
  }

  public void addMember(FamilyMember member) {
    if (!this.members.contains(member)) {
      this.members.add(member.copy(getNextMemberId()));
    }
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
      m.setBirthPlace(member.getBirthPlace().orElse(null));
      m.setDeathDate(member.getDeathDate().orElse(null));
      m.setDeathPlace(member.getDeathPlace().orElse(null));
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

  public Set<Wedding> getAllWeddings() {
    Set<Wedding> weddings = new HashSet<>();
    this.weddings.forEach(wedding -> weddings.add(wedding.clone()));
    return weddings;
  }

  public Optional<Wedding> getWedding(FamilyMember member) {
    Optional<Wedding> w = getLinkForNode(member);
    return Optional.ofNullable(w.isPresent() ? w.get().clone() : null);
  }

  public void addWedding(Wedding wedding) {
    if (!this.weddings.contains(wedding)) {
      FamilyMember[] children = wedding.getChildren().stream().map(child -> getMember(child.getId()).get()).toArray(FamilyMember[]::new);

      // @f0
      this.weddings.add(new Wedding(
          wedding.getDate().orElse(null),
          wedding.getLocation().orElse(null),
          getMember(wedding.getHusband().getId()).get(),
          getMember(wedding.getWife().getId()).get(),
          children));
      // @f1
    }
  }

  public void updateWedding(Wedding wedding) {
    Optional<Wedding> optional = getLinkForNode(wedding.getHusband());

    if (optional.isPresent()) {
      Wedding w = optional.get();

      w.setDate(wedding.getDate().orElse(null));
      w.setLocation(wedding.getLocation().orElse(null));
      Set<FamilyMember> children = w.getChildren();
      children.forEach(child -> w.removeChild(child));
      wedding.getChildren().forEach(child -> w.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  /**
   * Retourne tous les maris potentiels pour la femme donnée.
   * 
   * @return les hommes célibataires
   */
  public Set<FamilyMember> getPotentialHusbandsForWife(FamilyMember member) {
    if (member != null && member.isMan())
      return new HashSet<>();
    // Condition : les personnes doivent être des hommes célibataires
    Set<FamilyMember> men = new HashSet<>(this.members);

    for (Iterator<FamilyMember> it = men.iterator(); it.hasNext();) {
      FamilyMember m = it.next();
      if (m.isWoman() || m.isMan() && getWedding(m).isPresent())
        it.remove();
    }

    return men;
  }

  /**
   * Retourne toutes les femmes potentielles pour l'homme donné.
   * 
   * @return les femmes célibataires
   */
  public Set<FamilyMember> getPotentialWivesForHusband(FamilyMember member) {
    if (member != null && member.isWoman())
      return new HashSet<>();
    // Condition : les personnes doivent être des femmes célibataires
    Set<FamilyMember> women = new HashSet<>(this.members);

    for (Iterator<FamilyMember> it = women.iterator(); it.hasNext();) {
      FamilyMember m = it.next();
      if (m.isMan() || m.isWoman() && getWedding(m).isPresent())
        it.remove();
    }

    return women;
  }

  /**
   * Retourne toutes les personnes éligibles pour être les enfants du couple donné. Si l'argument
   * est null, tous les membres de la famille sont retournés.
   * 
   * @param wedding le mariage
   * @return la liste des enfants potentiels
   */
  public Set<FamilyMember> getPotentialChildren(Wedding wedding) {
    if (wedding == null)
      return getAllMembers();

    // Condition : les personnes ne doivent pas déjà faire partie de la famille des deux conjoints.
    Set<FamilyMember> children = new HashSet<>(this.members);
    GraphExplorer<FamilyMember, Wedding> explorer = new GraphExplorer<>(this);

    children.removeAll(explorer.explore(wedding.getHusband()));
    children.removeAll(explorer.explore(wedding.getWife()));

    System.out.println(children);
    return children;
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

  private long getNextMemberId() {
    return this.globalId++;
  }
}