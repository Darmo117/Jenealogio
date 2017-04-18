package net.darmo_creations.gui.dialog.options;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Optional;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import net.darmo_creations.gui.NamedTreeNode;
import net.darmo_creations.gui.dialog.DefaultDialogController;
import net.darmo_creations.model.GlobalConfig;

public class EditColorController extends DefaultDialogController<EditColorsDialog> implements TreeSelectionListener {
  private GlobalConfig config;
  private String selectedNode;

  public EditColorController(EditColorsDialog dialog) {
    super(dialog);
    this.config = null;
    this.selectedNode = null;
  }

  public GlobalConfig getConfig() {
    return this.config;
  }

  public void setConfig(GlobalConfig config) {
    this.dialog.setCanceled(false);
    this.config = config;
    if (this.selectedNode != null)
      this.dialog.setButtonColor(getColorForNode());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    if ("edit_color".equals(e.getActionCommand())) {
      Optional<Color> opt = this.dialog.showColorChooser(getColorForNode());

      if (opt.isPresent()) {
        setColorForNode(opt.get());
        this.dialog.setButtonColor(opt.get());
      }
    }
  }

  private Color getColorForNode() {
    switch (this.selectedNode) {
      case "card_border":
        return this.config.getCardBorderColor();
      case "card_selected_border":
        return this.config.getCardSelectionColor();
      case "gender_unknown":
        return this.config.getUnknownGenderColor();
      case "gender_male":
        return this.config.getMaleColor();
      case "gender_female":
        return this.config.getFemaleColor();
      case "link":
        return this.config.getLinkColor();
      case "link_hovered":
        return this.config.getLinkHoverColor();
      case "link_child":
        return this.config.getChildLinkColor();
      case "link_selected":
        return this.config.getLinkSelectionColor();
    }
    return null;
  }

  private void setColorForNode(Color c) {
    switch (this.selectedNode) {
      case "card_border":
        this.config.setCardBorderColor(c);
        break;
      case "card_selected_border":
        this.config.setCardSelectionColor(c);
        break;
      case "gender_unknown":
        this.config.setUnknownGenderColor(c);
        break;
      case "gender_male":
        this.config.setMaleColor(c);
        break;
      case "gender_female":
        this.config.setFemaleColor(c);
        break;
      case "link":
        this.config.setLinkColor(c);
        break;
      case "link_hovered":
        this.config.setLinkHoverColor(c);
        break;
      case "link_child":
        this.config.setChildLinkColor(c);
        break;
      case "link_selected":
        this.config.setLinkSelectionColor(c);
        break;
    }
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    NamedTreeNode node = (NamedTreeNode) e.getPath().getLastPathComponent();

    if (node.isLeaf()) {
      this.dialog.setColorButtonEnabled(true);
      this.selectedNode = node.getName();
      this.dialog.setButtonColor(getColorForNode());
    }
    else {
      this.dialog.setColorButtonEnabled(false);
      this.selectedNode = null;
      this.dialog.setButtonColor(Color.GRAY);
    }
  }
}
