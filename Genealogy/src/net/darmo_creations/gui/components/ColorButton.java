package net.darmo_creations.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JButton;

public class ColorButton extends JButton {
  private static final long serialVersionUID = 3060407471739094850L;

  public ColorButton() {
    this(Color.BLACK);
  }

  public ColorButton(Color color) {
    setPreferredSize(new Dimension(49, 23));
    setColor(color);
  }

  public Color getColor() {
    return ((ColorIcon) getIcon()).getColor();
  }

  public void setColor(Color color) {
    Dimension size = getPreferredSize();
    int w = size.width - 12;
    int h = size.height - 12;
    setIcon(new ColorIcon(new Dimension(w, h), color));
  }

  private class ColorIcon implements Icon {
    private Dimension size;
    private Color color;

    public ColorIcon(Dimension size, Color color) {
      this.size = size;
      this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor(this.color);
      g2d.fillRect(x, y, this.size.width - 1, this.size.height - 1);
      g2d.setColor(ColorButton.this.isEnabled() ? Color.BLACK : Color.GRAY);
      g2d.drawRect(x, y, this.size.width - 1, this.size.height - 1);
    }

    public Color getColor() {
      return this.color;
    }

    @Override
    public int getIconWidth() {
      return this.size.width;
    }

    @Override
    public int getIconHeight() {
      return this.size.height;
    }
  }
}
