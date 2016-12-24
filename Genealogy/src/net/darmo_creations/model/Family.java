package net.darmo_creations.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Family {
  private long id;
  private List<FamilyMember> members;
  private List<Wedding> weddings;

  public Family() {
    this.id = 0;
    this.members = new ArrayList<>();
    this.weddings = new ArrayList<>();
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
      if (member.getFather().isPresent())
        m.setFather(getMember(member.getFather().get().getId()).orElseThrow(IllegalStateException::new));
      else
        m.setFather(null);
      if (member.getMother().isPresent())
        m.setMother(getMember(member.getMother().get().getId()).orElseThrow(IllegalStateException::new));
      else
        m.setMother(null);
    }
  }

  public void removeMember(FamilyMember member) {
    for (Iterator<Wedding> it = this.weddings.iterator(); it.hasNext();) {
      Wedding wedding = it.next();
      if (wedding.isMarried(member))
        it.remove();
    }
    this.members.remove(member);
  }

  public Optional<Wedding> getWedding(FamilyMember member) {
    return this.weddings.stream().filter(wedding -> wedding.getHusband().equals(member) || wedding.getWife().equals(member)).findAny();
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
      List<FamilyMember> children = w.getChildren();
      children.forEach(child -> w.removeChild(child));
      wedding.getChildren().forEach(child -> w.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  /**
   * Retourne tous les hommes éligibles pour être le père de la personne donnée. Si l'argument est
   * null, tous les hommes sont retournés.
   * 
   * @param member la personne de référence
   * @return les pères potentiels
   */
  public List<FamilyMember> getPotentialFathers(FamilyMember member) {
    List<FamilyMember> men = new ArrayList<>();

    // TODO

    return men;
  }

  /**
   * Retourne toutes les femmes éligibles pour être la mère de la personne donnée. Si l'argument est
   * null, toutes les femmes sont retournées.
   * 
   * @param member la personne de référence
   * @return les mères potentielles
   */
  public List<FamilyMember> getPotentialMothers(FamilyMember member) {
    List<FamilyMember> women = new ArrayList<>();

    // TODO

    return women;
  }

  /**
   * Retourne tous les hommes non encore mariés.
   * 
   * @return les hommes célibataires
   */
  public List<FamilyMember> getPotentialHusbands() {
    List<FamilyMember> men = new ArrayList<>();

    // TODO

    return men;
  }

  /**
   * Retourne toutes les femmes non encore mariées.
   * 
   * @return les femmes célibataires
   */
  public List<FamilyMember> getPotentialWives() {
    List<FamilyMember> women = new ArrayList<>();

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
  public List<FamilyMember> getPotentialChildren(Wedding wedding) {
    List<FamilyMember> children = new ArrayList<>();

    // TODO

    return children;
  }
}