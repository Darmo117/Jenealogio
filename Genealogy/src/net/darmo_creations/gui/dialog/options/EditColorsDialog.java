package net.darmo_creations.gui.dialog.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.ColorButton;
import net.darmo_creations.gui.components.NamedTreeNode;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.util.I18n;

/**
 * This dialog lets the user change colors of different components.
 *
 * @author Damien Vergnet
 */
public class EditColorsDialog extends AbstractDialog {
  private static final long serialVersionUID = 3211695244192173657L;

  private EditColorController controller;
  private ColorButton colorButton;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public EditColorsDialog(JFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);
    setPreferredSize(new Dimension(400, 300));

    this.controller = new EditColorController(this);

    JPanel contentPnl = new JPanel(new GridLayout(1, 2));
    JTree tree = new JTree(new DefaultTreeModel(getTree()));
    tree.setRootVisible(false);
    tree.setShowsRootHandles(true);
    tree.addTreeSelectionListener(this.controller);
    JScrollPane scrollPnl = new JScrollPane(tree);
    contentPnl.add(scrollPnl, BorderLayout.CENTER);
    JPanel rightPnl = new JPanel();
    rightPnl.add(new JLabel(I18n.getLocalizedString("label.color.text") + ":"));
    rightPnl.add(this.colorButton = new ColorButton(Color.GRAY));
    this.colorButton.setActionCommand("edit_color");
    this.colorButton.addActionListener(this.controller);
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
    genders.add(new NamedTreeNode("gender_unknown", I18n.getLocalizedString("node.colors.genders.unknown.text")));
    genders.add(new NamedTreeNode("gender_female", I18n.getLocalizedString("node.colors.genders.female.text")));
    genders.add(new NamedTreeNode("gender_male", I18n.getLocalizedString("node.colors.genders.male.text")));
    NamedTreeNode cards = new NamedTreeNode("genders", I18n.getLocalizedString("node.colors.cards.text"));
    root.add(cards);
    cards.add(new NamedTreeNode("card_border", I18n.getLocalizedString("node.colors.cards.border.text")));
    cards.add(new NamedTreeNode("card_selected_border", I18n.getLocalizedString("node.colors.cards.border.selected.text")));
    NamedTreeNode links = new NamedTreeNode("links", I18n.getLocalizedString("node.colors.links.text"));
    root.add(links);
    links.add(new NamedTreeNode("link", I18n.getLocalizedString("node.colors.links.link.text")));
    links.add(new NamedTreeNode("link_hovered", I18n.getLocalizedString("node.colors.links.hovered.text")));
    links.add(new NamedTreeNode("link_selected", I18n.getLocalizedString("node.colors.links.selected.text")));
    links.add(new NamedTreeNode("link_child", I18n.getLocalizedString("node.colors.links.child.text")));

    return root;
  }

  /**
   * Shows a color chooser.
   * 
   * @param initialColor color chooser's initial color
   * @return the selected color or nothing if chooser was dismissed
   */
  public Optional<Color> showColorChooser(Color initialColor) {
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
  }

  /**
   * @return the new config or nothing if the dialog was canceled
   */
  public Optional<GlobalConfig> getConfig() {
    return Optional.ofNullable(isCanceled() ? null : this.controller.getConfig());
  }

  /**
   * Sets the config.
   * 
   * @param config the new config
   */
  public void setConfig(GlobalConfig config) {
    this.controller.setConfig(config);
  }
}
