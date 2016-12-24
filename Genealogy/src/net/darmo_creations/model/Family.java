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
}