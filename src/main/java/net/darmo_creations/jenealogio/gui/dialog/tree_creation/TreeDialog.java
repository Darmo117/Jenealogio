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
package net.darmo_creations.jenealogio.gui.dialog.tree_creation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.darmo_creations.jenealogio.util.Images;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.dialog.AbstractDialog;

/**
 * This dialog prompts a name for the tree being created.
 *
 * @author Damien Vergnet
 */
public class TreeDialog extends AbstractDialog {
  private static final long serialVersionUID = -521752892964228483L;

  private JTextField nameFld;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public TreeDialog(JFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);
    setIconImage(Images.JENEALOGIO.getImage());

    TreeController controller = new TreeController(this);

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
   * Sets tree's info.
   * 
   * @param familyName current tree's name
   */
  public void setInfo(String familyName) {
    if (familyName == null)
      setTitle(I18n.getLocalizedString("dialog.new_tree.title"));
    else
      setTitle(I18n.getLocalizedString("dialog.edit_tree.title"));
    this.nameFld.setText(familyName);
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
