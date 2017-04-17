package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.darmo_creations.gui.components.DisplayPanel;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
public class DisplayController extends MouseAdapter implements ActionListener {
  private static final Pattern SELECT_PATTERN = Pattern.compile("^select:(\\d+)$");

  private DisplayPanel panel;
  private Point mouseLocation;

  public DisplayController(DisplayPanel panel) {
    this.panel = panel;
    this.mouseLocation = new Point();
  }

  /**
   * @return the last location of the mouse
   */
  public Point getMouseLocation() {
    return (Point) this.mouseLocation.clone();
  }

  /**
   * Called when a card panel is selected. Tells the DisplayPanel what card was selected.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    Matcher m = SELECT_PATTERN.matcher(cmd);

    if (m.matches()) {
      this.panel.selectPanel(Integer.parseInt(m.group(1)));
    }
  }

  /**
   * Called the mouse is clicked inside the DisplayPanel. Deselects all cards and checks if a link
   * was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    this.panel.selectPanel(-1);
    this.panel.selectLinkIfHovered();
    if (e.getClickCount() == 2)
      this.panel.editLinkIfHovered();
  }

  /**
   * Repaints the DisplayPanel everytime the mouse moves.
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    this.mouseLocation = e.getPoint();
    this.panel.repaint();
  }
}
