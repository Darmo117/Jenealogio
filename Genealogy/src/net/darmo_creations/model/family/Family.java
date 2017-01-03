package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.darmo_creations.model.graph.Graph;
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
    // @f0
    return this.members
        .stream()
        .filter(member -> member.getId() == id)
        .findAny();
    // @f1
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
      // @f0
      FamilyMember[] children =
          wedding.getChildren()
          .stream()
          .map(child -> getMember(child.getId()).get())
          .toArray(FamilyMember[]::new);

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
      // @f0
      wedding.getChildren()
          .forEach(child -> w.addChild(Family.this.getMember(child.getId())
          .orElseThrow(IllegalStateException::new)));
      // @f1
    }
  }

  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  public boolean isMarried(FamilyMember member) {
    return this.weddings.stream().anyMatch(wedding -> wedding.isMarried(member));
  }

  /**
   * Retourne tous les maris potentiels pour la femme donnée.
   * 
   * @return les hommes célibataires
   */
  public Set<FamilyMember> getPotentialHusbandsForWife(FamilyMember member) {
    if (member != null && member.isMan())
      return new HashSet<>();
    // @f0
    return getAllMembers()
        .stream()
        .filter(m -> m.isMan() && (member == null || !isMarried(member)) && !m.equals(member))
        .collect(Collectors.toCollection(HashSet::new));
    // @f1
  }

  /**
   * Retourne toutes les femmes potentielles pour l'homme donné.
   * 
   * @return les femmes célibataires
   */
  public Set<FamilyMember> getPotentialWivesForHusband(FamilyMember member) {
    if (member != null && member.isWoman())
      return new HashSet<>();
    // @f0
    return getAllMembers()
        .stream()
        .filter(m -> m.isWoman() && (member == null || !isMarried(member)) && !m.equals(member))
        .collect(Collectors.toCollection(HashSet::new));
    // @f1
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
    // @f0
    return getAllMembers()
        .stream()
        .filter(m -> !m.equals(wedding.getHusband()) && !m.equals(wedding.getWife()) && !wedding.isChild(m))
        .collect(Collectors.toCollection(HashSet::new));
    // @f1
  }

  @Override
  protected Optional<Wedding> getLinkForNode(Node<FamilyMember> node) {
    // @f0
    return this.weddings.stream()
        .filter(wedding -> wedding.getHusband().equals(node) || wedding.getWife().equals(node))
        .findAny();
    // @f1
  }

  @Override
  protected Optional<FamilyMember> getLeftAncestorForNode(Node<FamilyMember> node) {
    // @f0
    Optional<Wedding> optional =
        this.weddings
        .stream()
        .filter(wedding -> wedding.getChildren().contains(node))
        .findAny();
    // @f1

    if (optional.isPresent())
      return Optional.of(optional.get().getHusband());

    return Optional.empty();
  }

  @Override
  protected Optional<FamilyMember> getRightAncestorForNode(Node<FamilyMember> node) {
    // @f0
    Optional<Wedding> optional =
        this.weddings
        .stream()
        .filter(wedding -> wedding.getChildren().contains(node))
        .findAny();
    // @f1

    if (optional.isPresent())
      return Optional.of(optional.get().getWife());

    return Optional.empty();
  }

  private long getNextMemberId() {
    return this.globalId++;
  }
}