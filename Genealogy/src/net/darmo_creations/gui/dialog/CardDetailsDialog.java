package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.darmo_creations.controllers.DefaultDialogController;
import net.darmo_creations.gui.components.FamilyMemberPanel;
import net.darmo_creations.gui.components.ImageLabel;
import net.darmo_creations.model.family.DummyFamilyMember;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;

public class CardDetailsDialog extends AbstractDialog {
  private static final long serialVersionUID = 4772771687761193691L;

  private ImageLabel imageLbl;
  private FamilyMemberPanel infoPnl;

  public CardDetailsDialog(JFrame owner) {
    super(owner, Mode.CLOSE_OPTION, true);
    setResizable(false);

    JPanel p = new JPanel();
    p.add(this.imageLbl = new ImageLabel(null));
    this.imageLbl.setPreferredSize(new Dimension(120, 150));
    this.imageLbl.setMaximumSize(this.imageLbl.getPreferredSize());
    this.imageLbl.setBorder(new LineBorder(Color.GRAY));
    add(p, BorderLayout.NORTH);
    add(this.infoPnl = new FamilyMemberPanel(new DummyFamilyMember(""), null), BorderLayout.CENTER);

    setActionListener(new DefaultDialogController<CardDetailsDialog>(this));

    pack();
    setLocationRelativeTo(owner);
  }

  public void setInfo(FamilyMember member, Wedding wedding) {
    setTitle(member.toString());
    member.getImage().ifPresent(img -> this.imageLbl.setIcon(new ImageIcon(img)));
    this.infoPnl.setInfo(member, wedding);
  }
}
