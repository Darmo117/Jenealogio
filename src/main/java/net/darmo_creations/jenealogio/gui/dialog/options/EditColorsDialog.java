/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.jenealogio.gui.dialog.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.config.ConfigTags;
import net.darmo_creations.jenealogio.gui.components.ColorButton;
import net.darmo_creations.jenealogio.gui.components.NamedTreeNode;
import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;

/**
 * This dialog lets the user change colors of different components.
 *
 * @author Damien Vergnet
 */
public class EditColorsDialog extends AbstractDialog {
  private static final long serialVersionUID = 3211695244192173657L;

  private EditColorController controller;
  private ColorButton colorButton;
  private JButton defaultBtn;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public EditColorsDialog(JFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setPreferredSize(new Dimension(400, 300));
    setMinimumSize(getPreferredSize());
    setTitle(I18n.getLocalizedString("dialog.edit_colors.title"));
    setIconImage(Images.JENEALOGIO.getImage());

    this.controller = new EditColorController(this);

    JPanel contentPnl = new JPanel(new GridLayout(1, 2));
    JTree tree = new JTree(new DefaultTreeModel(getTree()));
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.addTreeSelectionListener(this.controller);
    tree.setCellRenderer(new DefaultTreeCellRenderer() {
      private static final long serialVersionUID = -489015910861060922L;

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
          boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        NamedTreeNode node = (NamedTreeNode) value;

        switch (node.getName()) {
          case "gender_male_color":
            setIcon(Images.MALE_SYMBOL);
            break;
          case "gender_female_color":
            setIcon(Images.FEMALE_SYMBOL);
            break;
          case "cards":
            setIcon(Images.USER);
            break;
          case "links":
            setIcon(Images.LINK);
            break;
          default:
            if (leaf) {
              setIcon(null);
            }
        }

        return c;
      }
    });
    JScrollPane scrollPnl = new JScrollPane(tree);
    contentPnl.add(scrollPnl, BorderLayout.CENTER);
    JPanel rightPnl = new JPanel();
    rightPnl.add(new JLabel(I18n.getLocalizedString("label.color.text") + ":"));
    rightPnl.add(this.colorButton = new ColorButton(Color.GRAY));
    this.colorButton.setActionCommand("edit_color");
    this.colorButton.addActionListener(this.controller);
    this.defaultBtn = new JButton(I18n.getLocalizedString("button.default_color.text"));
    this.defaultBtn.setActionCommand("default_color");
    this.defaultBtn.addActionListener(this.controller);
    rightPnl.add(this.defaultBtn);
    contentPnl.add(rightPnl, BorderLayout.EAST);
    add(contentPnl);
    setActionListener(this.controller);

    setColorButtonEnabled(false);

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * @return the options tree
   */
  private DefaultMutableTreeNode getTree() {
    NamedTreeNode root = new NamedTreeNode("root");

    NamedTreeNode genders = new NamedTreeNode("genders", I18n.getLocalizedString("node.colors.genders.text"));
    root.add(genders);
    genders.add(new NamedTreeNode(ConfigTags.GENDER_UNKNOWN_COLOR.getName(), I18n.getLocalizedString("node.colors.genders.unknown.text")));
    genders.add(new NamedTreeNode(ConfigTags.GENDER_FEMALE_COLOR.getName(), I18n.getLocalizedString("node.colors.genders.female.text")));
    genders.add(new NamedTreeNode(ConfigTags.GENDER_MALE_COLOR.getName(), I18n.getLocalizedString("node.colors.genders.male.text")));
    NamedTreeNode cards = new NamedTreeNode("cards", I18n.getLocalizedString("node.colors.cards.text"));
    root.add(cards);
    cards.add(new NamedTreeNode(ConfigTags.CARD_BORDER_COLOR.getName(), I18n.getLocalizedString("node.colors.cards.border.text")));
    cards.add(new NamedTreeNode(ConfigTags.CARD_SELECTED_BORDER_COLOR.getName(),
        I18n.getLocalizedString("node.colors.cards.border.selected.text")));
    cards.add(new NamedTreeNode(ConfigTags.CARD_SELECTED_BACKGROUND_BORDER_COLOR.getName(),
        I18n.getLocalizedString("node.colors.cards.border.selected_background.text")));
    NamedTreeNode links = new NamedTreeNode("links", I18n.getLocalizedString("node.colors.links.text"));
    root.add(links);
    links.add(new NamedTreeNode(ConfigTags.LINK_COLOR.getName(), I18n.getLocalizedString("node.colors.links.link.text")));
    links.add(new NamedTreeNode(ConfigTags.LINK_HOVERED_COLOR.getName(), I18n.getLocalizedString("node.colors.links.hovered.text")));
    links.add(new NamedTreeNode(ConfigTags.LINK_SELECTED_COLOR.getName(), I18n.getLocalizedString("node.colors.links.selected.text")));
    links.add(new NamedTreeNode(ConfigTags.LINK_CHILD_COLOR.getName(), I18n.getLocalizedString("node.colors.links.child.text")));
    links.add(
        new NamedTreeNode(ConfigTags.LINK_ADOPTED_CHILD_COLOR.getName(), I18n.getLocalizedString("node.colors.links.child.adopted.text")));
    NamedTreeNode selection = new NamedTreeNode("selection", I18n.getLocalizedString("node.colors.selection.text"));
    root.add(selection);
    selection.add(
        new NamedTreeNode(ConfigTags.SELECTION_BORDER_COLOR.getName(), I18n.getLocalizedString("node.colors.selection.border.text")));
    selection.add(new NamedTreeNode(ConfigTags.SELECTION_BACKGROUND_COLOR.getName(),
        I18n.getLocalizedString("node.colors.selection.background.text")));

    return root;
  }

  /**
   * Shows a color chooser.
   * 
   * @param initialColor color chooser's initial color
   * @return the selected color or nothing if chooser was dismissed
   */
  Optional<Color> showColorChooser(Color initialColor) {
    Color c = JColorChooser.showDialog(this, I18n.getLocalizedString("dialog.color_chooser.title"), initialColor);
    return Optional.ofNullable(c);
  }

  /**
   * Sets the color of the ColorButton.
   * 
   * @param color the new color
   */
  void setButtonColor(Color color) {
    this.colorButton.setColor(color);
  }

  /**
   * Enables/disables the ColorButton.
   * 
   * @param enabled
   */
  void setColorButtonEnabled(boolean enabled) {
    this.colorButton.setEnabled(enabled);
    this.defaultBtn.setEnabled(enabled);
  }

  /**
   * @return the new config or nothing if the dialog was canceled
   */
  public Optional<WritableConfig> getConfig() {
    return Optional.ofNullable(isCanceled() ? null : this.controller.getConfig());
  }

  /**
   * Sets the config.
   * 
   * @param config the new config
   */
  public void setConfig(WritableConfig config) {
    this.controller.setConfig(config);
  }
}
