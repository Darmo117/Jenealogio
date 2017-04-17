package net.darmo_creations.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.components.DetailsPanel;
import net.darmo_creations.gui.components.ImageLabel;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Observable;
import net.darmo_creations.util.Observer;

/**
 * This dialog dislays details about a person.
 *
 * @author Damien Vergnet
 */
public class CardDetailsDialog extends AbstractDialog implements Observable {
  private static final long serialVersionUID = 4772771687761193691L;

  private List<Observer> observers;

  private ImageLabel imageLbl;
  private DetailsPanel infoPnl;

  /**
   * Creates a dialog.
   * 
   * @param owner the owner
   */
  public CardDetailsDialog(MainFrame owner) {
    super(owner, Mode.CLOSE_OPTION, true);
    setResizable(false);

    this.observers = new ArrayList<>();

    JPanel imagePnl = new JPanel();
    imagePnl.add(this.imageLbl = new ImageLabel(null));
    this.imageLbl.setPreferredSize(new Dimension(120, 150));
    this.imageLbl.setMaximumSize(this.imageLbl.getPreferredSize());
    this.imageLbl.setBorder(new LineBorder(Color.GRAY));
    add(imagePnl, BorderLayout.NORTH);
    add(this.infoPnl = new DetailsPanel(), BorderLayout.CENTER);
    JButton editBtn = new JButton(I18n.getLocalizedString("button.edit.text"));
    addButton(editBtn);
    editBtn.setActionCommand("edit");

    setActionListener(new DefaultDialogController<CardDetailsDialog>(this) {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("edit")) {
          this.dialog.setVisible(false);
          notifyObservers("edit");
          return;
        }
        super.actionPerformed(e);
      }
    });

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Sets displayed information.
   * 
   * @param member the person
   * @param wedding the wedding it is part of
   */
  public void setInfo(FamilyMember member, Wedding wedding) {
    setTitle(member.toString());
    member.getImage().ifPresent(img -> this.imageLbl.setIcon(new ImageIcon(img)));
    this.infoPnl.setInfo(member, wedding);
  }

  @Override
  public void addObserver(Observer observer) {
    this.observers.add(Objects.requireNonNull(observer));
  }

  @Override
  public void removeObserver(Observer observer) {
    if (observer != null)
      this.observers.remove(observer);
  }

  @Override
  public void notifyObservers(Object o) {
    this.observers.forEach(obs -> obs.update(this, o));
  }
}
