package net.darmo_creations.gui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.darmo_creations.model.Family;
import net.darmo_creations.model.Wedding;
import net.darmo_creations.util.Pair;

// TODO afficher les éléments sur une grille et permettre de les déplacer.
public class DisplayPanel extends JPanel implements Scrollable {
  private static final long serialVersionUID = 8747904983365363275L;

  private Map<Long, Pair<Point, FamilyMemberPanel>> panels;
  private List<Link> links;
  private boolean detailledMode;

  public DisplayPanel() {
    setPreferredSize(new Dimension(4000, 4000));
    setLayout(null);
    this.panels = new HashMap<>();
    this.links = new ArrayList<>();
    this.detailledMode = true;
  }

  public boolean isDetailledMode() {
    return this.detailledMode;
  }

  public void setDetailledMode(boolean detailled) {
    if (isDetailledMode() != detailled) {
      this.detailledMode = detailled;
      this.panels.forEach((id, pair) -> pair.getRightElement().setDetailledMode(detailled));
    }
  }

  public void reset() {
    this.panels.clear();
  }

  public void refresh(Family family) {
    Set<Long> updatedOrAdded = new HashSet<>();
    Set<Long> keysToDelete = new HashSet<>(this.panels.keySet());

    // Mise à jour/ajout des membres.
    family.getAllMembers().forEach(member -> {
      long id = member.getId();
      Wedding w = family.getWedding(member).orElse(null);

      if (keysToDelete.contains(id)) {
        this.panels.get(id).getRightElement().setInfo(member, w);
        updatedOrAdded.add(id);
      }
      else {
        this.panels.put(id, new Pair<>(new Point(), new FamilyMemberPanel(member, w, isDetailledMode())));
        updatedOrAdded.add(id);
      }
    });

    // Suppression des membres supprimés dans le modèle.
    keysToDelete.removeAll(updatedOrAdded);
    keysToDelete.forEach(id -> {
      remove(this.panels.get(id).getRightElement());
      this.panels.remove(id);
    });

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Mise à jour/ajout des liens.
    // TODO

    // Suppression des liens supprimés du modèle.
    List<Link> linksToDelete = new ArrayList<>(this.links);
    linksToDelete.removeAll(updatedOrAddedLinks);
    this.links.removeAll(linksToDelete);

    revalidate();
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    // TODO Auto-generated method stub
  }

  @Override
  public Dimension getPreferredScrollableViewportSize() {
    return getPreferredSize();
  }

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 16;
  }

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    return 25;
  }

  @Override
  public boolean getScrollableTracksViewportWidth() {
    return false;
  }

  @Override
  public boolean getScrollableTracksViewportHeight() {
    return false;
  }

  private class Link {
    private long id1, id2;

    public Link(long id1, long id2) {
      this.id1 = id1;
      this.id2 = id2;
    }

    public long getId1() {
      return this.id1;
    }

    public void setId1(long id1) {
      this.id1 = id1;
    }

    public long getId2() {
      return this.id2;
    }

    public void setId2(long id2) {
      this.id2 = id2;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;

      result = prime * result + (int) (this.id1 ^ (this.id1 >>> 32));
      result = prime * result + (int) (this.id2 ^ (this.id2 >>> 32));

      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Link) {
        Link l = (Link) o;
        return l.getId1() == getId1() && l.getId2() == getId2();
      }

      return false;
    }
  }
}
