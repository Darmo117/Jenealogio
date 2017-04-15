package net.darmo_creations.controllers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.darmo_creations.gui.components.DisplayPanel;

public class DisplayController extends MouseAdapter implements ActionListener {
  private static final Pattern SELECT_PATTERN = Pattern.compile("^select:(\\d+)$");

  private DisplayPanel panel;

  private Point mouseLocation;

  public DisplayController(DisplayPanel panel) {
    this.panel = panel;
    this.mouseLocation = new Point();
  }

  public Point getMouseLocation() {
    return (Point) this.mouseLocation.clone();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    Matcher m = SELECT_PATTERN.matcher(cmd);

    if (m.matches()) {
      this.panel.selectPanel(Integer.parseInt(m.group(1)));
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.panel.selectPanel(-1);
    this.panel.selectLinkIfHovered();
    if (e.getClickCount() == 2)
      this.panel.editLinkIfHovered();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    this.mouseLocation = e.getPoint();
    this.panel.repaint();
  }
}
