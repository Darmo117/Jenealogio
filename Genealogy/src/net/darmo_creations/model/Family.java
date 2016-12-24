package net.darmo_creations.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Family {
  private List<FamilyMember> members;
  private List<Wedding> weddings;

  public Family() {
    this.members = new ArrayList<>();
    this.weddings = new ArrayList<>();
  }

  public Optional<FamilyMember> getMember(long id) {
    for (FamilyMember member : this.members)
      if (member.getId() == id)
        return Optional.of(member);
    return Optional.empty();
  }

  public void addMember(FamilyMember member) {
    if (!this.members.contains(member))
      this.members.add(member);
  }

  public void updateMember(FamilyMember member) {
    // TODO
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
    for (Wedding wedding : this.weddings)
      if (wedding.getHusband().equals(member) || wedding.getWife().equals(member))
        return Optional.of(wedding);
    return Optional.empty();
  }

  public void addWedding(Wedding wedding) {
    if (!this.weddings.contains(wedding))
      this.weddings.add(wedding);
  }

  public void updateWedding(Wedding wedding) {
    // TODO
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
   * Retourne toutes le personnes éligibles pour être les enfants du couple donné. Si les deux
   * arguments sont null, tous les membres de la famille sont retournés.
   * 
   * @param husband le mari
   * @param wife l'épouse
   * @return la liste des enfants potentiels
   */
  public List<FamilyMember> getPotentialChildren(FamilyMember husband, FamilyMember wife) {
    if ((husband == null) != (wife == null))
      throw new NullPointerException("husband and wife must not be null separately");
    List<FamilyMember> children = new ArrayList<>();

    // TODO

    return children;
  }
}