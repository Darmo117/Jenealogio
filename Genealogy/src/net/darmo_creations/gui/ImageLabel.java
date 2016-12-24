package net.darmo_creations.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel extends JLabel {
  private static final long serialVersionUID = 3710328037282006238L;

  public ImageLabel(ImageIcon image) {
    super(image);
  }

  @Override
  protected void paintComponent(Graphics g) {
    ImageIcon icon = (ImageIcon) getIcon();

    if (icon != null) {
      Graphics2D g2 = (Graphics2D) g;
      Dimension size = getSize();

      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(icon.getImage(), 0, 0, size.width, size.height, null);
      g2.dispose();
    }

    super.paintComponent(g);
  }
}
