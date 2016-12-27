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
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.darmo_creations.gui.components.drag.DragController;
import net.darmo_creations.gui.components.drag.DragableComponentContainer;
import net.darmo_creations.model.Family;
import net.darmo_creations.model.Wedding;

public class DisplayPanel extends JPanel implements Scrollable, DragableComponentContainer<FamilyMemberPanel> {
  private static final long serialVersionUID = 8747904983365363275L;

  private Map<Long, FamilyMemberPanel> panels;
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
      this.panels.forEach((id, component) -> component.setDetailledMode(detailled));
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
        this.panels.get(id).setInfo(member, w);
      }
      else {
        FamilyMemberPanel panel = new FamilyMemberPanel(member, w, isDetailledMode());
        DragController<FamilyMemberPanel> dragController = new DragController<>(this, panel);

        panel.setBounds(new Rectangle(panel.getPreferredSize()));
        panel.addMouseListener(dragController);
        panel.addMouseMotionListener(dragController);
        this.panels.put(id, panel);
        add(panel);
      }
      updatedOrAdded.add(id);
    });

    // Suppression des membres supprimés dans le modèle.
    keysToDelete.removeAll(updatedOrAdded);
    keysToDelete.forEach(id -> {
      remove(this.panels.get(id));
      this.panels.remove(id);
    });

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Mise à jour/ajout des liens.
    family.getAllWeddings().forEach(wedding -> {
      long id1 = wedding.getHusband().getId();
      long id2 = wedding.getWife().getId();
      Link link = new Link(id1, id2, null);
      Set<Long> children = wedding.getChildren().stream().map(member -> member.getId()).collect(Collectors.toSet());

      if (this.links.contains(link)) {
        this.links.get(this.links.indexOf(link)).setChildren(children);
      }
      else {
        this.links.add(new Link(id1, id2, children));
      }
      updatedOrAddedLinks.add(link);
    });

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
    // TODO dessiner les liens
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

  @Override
  public Point getScrollOffset() {
    return getVisibleRect().getLocation();
  }

  private class Link {
    private final long parent1, parent2;
    private Set<Long> children;

    public Link(long parent1, long parent2, Set<Long> children) {
      this.parent1 = parent1;
      this.parent2 = parent2;
      this.children = new HashSet<>(children);
    }

    public long getParent1() {
      return this.parent1;
    }

    public long getParent2() {
      return this.parent2;
    }

    public Set<Long> getChildren() {
      return this.children;
    }

    public void setChildren(Set<Long> children) {
      this.children = children;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;

      result = prime * result + (int) (this.parent1 ^ (this.parent1 >>> 32));
      result = prime * result + (int) (this.parent2 ^ (this.parent2 >>> 32));

      return result;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Link) {
        Link l = (Link) o;
        return l.getParent1() == getParent1() && l.getParent2() == getParent2();
      }

      return false;
    }
  }
}
