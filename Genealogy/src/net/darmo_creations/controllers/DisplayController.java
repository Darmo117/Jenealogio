package net.darmo_creations.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.darmo_creations.gui.components.DisplayPanel;

public class DisplayController implements ActionListener {
  private static final Pattern SELECT_PATTERN = Pattern.compile("^select:(\\d+)$");

  private DisplayPanel panel;

  public DisplayController(DisplayPanel panel) {
    this.panel = panel;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    Matcher m = SELECT_PATTERN.matcher(cmd);

    if (m.matches()) {
      this.panel.selectPanel(Integer.parseInt(m.group(1)));
    }
  }
}
