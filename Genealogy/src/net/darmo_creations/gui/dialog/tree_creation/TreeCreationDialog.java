package net.darmo_creations.gui.dialog.tree_creation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.util.I18n;

/**
 * This dialog prompts a name for the tree being created.
 *
 * @author Damien Vergnet
 */
public class TreeCreationDialog extends AbstractDialog {
  private static final long serialVersionUID = -521752892964228483L;

  private JTextField nameFld;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public TreeCreationDialog(JFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);

    setTitle(I18n.getLocalizedString("dialog.new_tree.title"));

    TreeCreationController controller = new TreeCreationController(this);

    this.nameFld = new JTextField();
    this.nameFld.getDocument().addDocumentListener(controller);

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 1;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.tree_name.text")), gbc);
    gbc.gridwidth = 2;
    gbc.weightx = 1;
    gbc.gridx = 1;
    fieldsPnl.add(this.nameFld, gbc);

    add(fieldsPnl, BorderLayout.CENTER);

    setValidateButtonEnabled(false);

    setActionListener(controller);

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * @return the tree name or nothing if the field is empty or the dialog was canceled
   */
  public Optional<String> getTreeName() {
    String text = this.nameFld.getText().trim();

    if (!isCanceled() && text.length() > 0)
      return Optional.of(text);
    return Optional.empty();
  }
}
