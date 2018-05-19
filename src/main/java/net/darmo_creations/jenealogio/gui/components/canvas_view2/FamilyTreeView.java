package net.darmo_creations.jenealogio.gui.components.canvas_view2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;

import javax.swing.JPanel;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.gui.components.AbstractFamilyTreeView;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasState;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.I18n;

public class FamilyTreeView extends AbstractFamilyTreeView<FamilyTreeViewController> {
  private static final long serialVersionUID = 8741835377741666741L;

  public FamilyTreeView() {
    super(I18n.getLocalizedString("label.canvas.text"), new FamilyTreeViewController());
    this.controller.setView(this);
    JPanel panel = new JPanel();
    panel.setBackground(Color.BLACK);
    panel.addFocusListener(this.controller);
    setViewport(panel);

    initDropTarget(panel);
  }

  @Override
  public void reset() {
    // TODO
  }

  @Override
  public void refresh(Family family, WritableConfig config) {
    // TODO
  }

  @Override
  public void refresh(Family family, CanvasState canvasStates, WritableConfig config) {
    // TODO
  }

  @Override
  public CanvasState getState() {
    // TODO
    return new CanvasState();
  }

  @Override
  public BufferedImage exportToImage() {
    // TODO
    return null;
  }

  @Override
  public Selection getSelection() {
    // TODO Auto-generated method stub
    return new Selection(Collections.emptyList(), Collections.emptyList());
  }

  @Override
  public void deselectAll() {
    // TODO Auto-generated method stub
  }
}
