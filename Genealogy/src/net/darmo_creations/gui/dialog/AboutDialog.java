package net.darmo_creations.gui.dialog;

import javax.swing.JFrame;

import net.darmo_creations.util.I18n;

public class AboutDialog extends AbstractDialog {
  private static final long serialVersionUID = -1919314429757828369L;

  public AboutDialog(JFrame owner) {
    super(owner, Mode.CLOSE_OPTION, false);

    setTitle(I18n.getLocalizedString("dialog.about.title"));

    setActionListener(new DefaultDialogController<>(this));

    pack();
    setLocationRelativeTo(owner);
  }
}
