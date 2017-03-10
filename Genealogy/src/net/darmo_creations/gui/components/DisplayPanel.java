package net.darmo_creations.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

import net.darmo_creations.controllers.DisplayController;
import net.darmo_creations.controllers.DoubleClickController;
import net.darmo_creations.controllers.DragController;
import net.darmo_creations.gui.components.drag.DragableComponentContainer;
import net.darmo_creations.model.Observable;
import net.darmo_creations.model.Observer;
import net.darmo_creations.model.family.Family;
import net.darmo_creations.model.family.Wedding;

public class DisplayPanel extends JPanel implements Scrollable, Observable, DragableComponentContainer<FamilyMemberPanel> {
  private static final long serialVersionUID = 8747904983365363275L;

  private List<Observer> observers;

  private DisplayController controller;
  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;
  private boolean showVirtualLink;

  public DisplayPanel() {
    setPreferredSize(new Dimension(4000, 4000));
    setLayout(null);

    this.observers = new ArrayList<>();
    this.controller = new DisplayController(this);
    addMouseListener(this.controller);

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();
  }

  public void reset() {
    this.panels.clear();
    this.links.clear();
    removeAll();
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
        FamilyMemberPanel panel = new FamilyMemberPanel(member, w);
        DragController<FamilyMemberPanel> dragController = new DragController<>(this, panel);

        panel.setBounds(new Rectangle(panel.getPreferredSize()));
        panel.addActionListener(this.controller);
        panel.addMouseListener(dragController);
        panel.addMouseListener(new DoubleClickController(id, this.observers));
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
      long id1 = wedding.getSpouse1().getId();
      long id2 = wedding.getSpouse2().getId();
      Link link = new Link(id1, id2, new HashSet<>());
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

  public void showVirtualLink(boolean show) {
    this.showVirtualLink = show;
  }

  public void selectPanel(long id) {
    this.panels.forEach((pId, panel) -> panel.setSelected(pId == id));
    notifyObservers(id);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    if (this.showVirtualLink) {
      Optional<FamilyMemberPanel> optPnl = this.panels.values().stream().filter(p -> p.isSelected()).findAny();

      // FIXME
      if (optPnl.isPresent()) {
        FamilyMemberPanel p = optPnl.get();
        Rectangle r = p.getBounds();
        Point pos = MouseInfo.getPointerInfo().getLocation();

        SwingUtilities.convertPointFromScreen(pos, this);
        g2d.drawLine(r.x + r.width / 2, r.y + r.height / 2, pos.x, pos.y);
      }
    }

    g2d.setColor(Color.BLACK);
    this.links.forEach(link -> {
      // Lien entre les mariés.
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);
      Point middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

      g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
      // Liens vers les enfants.
      link.getChildren().forEach(child -> {
        Rectangle r = this.panels.get(child).getBounds();
        Point p = new Point(r.x + r.width / 2, r.y + r.height / 2);

        g2d.drawLine(middle.x, middle.y, p.x, p.y);
      });
    });
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
    return getLocationOnScreen();
  }

  @Override
  public void addObserver(Observer observer) {
    this.observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    this.observers.remove(observer);
  }

  private void notifyObservers(Object o) {
    this.observers.forEach(obs -> obs.update(o));
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
