package net.darmo_creations.gui.components;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import net.darmo_creations.model.Family;

// TODO afficher les éléments sur une grille et permettre de les déplacer.
public class DisplayPanel extends JPanel implements Scrollable {
  private static final long serialVersionUID = 8747904983365363275L;

  private Map<Long, Point> positions;

  public DisplayPanel() {
    setPreferredSize(new Dimension(4000, 4000));
    setLayout(null);
    this.positions = new HashMap<>();
  }

  public void refresh(Family family) {
    // TODO
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
}
