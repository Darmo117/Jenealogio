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

import net.darmo_creations.controllers.DefaultDialogController;

public class CardController extends DefaultDialogController<CardDialog> implements KeyListener, DocumentListener, ItemListener {
  public CardController(CardDialog dialog) {
    super(dialog);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    switch (e.getActionCommand()) {
      case "chose-image":
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

  private void setImage() {
    File file = this.dialog.showOpenFileChooser();
    if (file != null) {
      Optional<BufferedImage> image;
      try {
        image = Optional.of(ImageIO.read(file));
      }
      catch (IOException e) {
        this.dialog.showErrorDialog("Erreur lors de l'ouverture de l'image !");
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
