package net.darmo_creations.gui.dialog.card;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.darmo_creations.gui.dialog.DefaultDialogController;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Gender;
import net.darmo_creations.util.I18n;

/**
 * This controller handles events from the CardDialog class.
 *
 * @author Damien Vergnet
 */
public class CardController extends DefaultDialogController<CardDialog> implements KeyListener, DocumentListener, ItemListener {
  /**
   * Creates a controller.
   * 
   * @param dialog the dialog
   */
  public CardController(CardDialog dialog) {
    super(dialog);
  }

  /**
   * Resets the associated dialog.
   * 
   * @param member the member to display
   */
  public void reset(FamilyMember member) {
    if (member != null) {
      this.dialog.setTitle(I18n.getLocalizedString("dialog.update_card.title"));
      this.dialog.setId(member.getId());
      this.dialog.setImage(member.getImage());
      this.dialog.setMemberName(member.getName().orElse(""));
      this.dialog.setFirstName(member.getFirstName().orElse(""));
      this.dialog.setGender(member.getGender());
      this.dialog.setBirthDate(member.getBirthDate());
      this.dialog.setBirthLocation(member.getBirthLocation().orElse(""));
      this.dialog.setDeathDate(member.getDeathDate());
      this.dialog.setDeathLocation(member.getDeathLocation().orElse(""));
    }
    else {
      this.dialog.setTitle(I18n.getLocalizedString("dialog.add_card.title"));
      this.dialog.setId(-1);
      this.dialog.setImage(Optional.empty());
      this.dialog.setMemberName("");
      this.dialog.setFirstName("");
      this.dialog.setGender(Gender.UNKNOW);
      this.dialog.setBirthDate(Optional.empty());
      this.dialog.setBirthLocation("");
      this.dialog.setDeathDate(Optional.empty());
      this.dialog.setDeathLocation("");
    }

    this.dialog.setCanceled(false);
    this.dialog.setValidateButtonEnabled(false);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    switch (e.getActionCommand()) {
      case "choose-image":
        setImage();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    char c = e.getKeyChar();

    if (!(c >= '0' && c <= '9' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SLASH))
      e.consume();
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    update();
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    update();
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    update();
  }

  private void update() {
    this.dialog.setValidateButtonEnabled(this.dialog.atLeastOneFieldCompleted());
  }

  /**
   * Opens up a file chooser then set the profile image on the dialog.
   */
  private void setImage() {
    File file = this.dialog.showOpenFileChooser();
    if (file != null) {
      Optional<BufferedImage> image;
      try {
        image = Optional.ofNullable(ImageIO.read(file));
      }
      catch (IOException e) {
        this.dialog.showErrorDialog(I18n.getLocalizedString("popup.open_image_error.text"));
        image = Optional.empty();
      }
      this.dialog.setImage(image);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void changedUpdate(DocumentEvent e) {}
}
