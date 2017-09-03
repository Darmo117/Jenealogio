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
package net.darmo_creations.jenealogio.gui.dialog.card;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Gender;
import net.darmo_creations.utils.I18n;
import net.darmo_creations.utils.swing.dialog.DefaultDialogController;

/**
 * This controller handles events from the CardDialog class.
 *
 * @author Damien Vergnet
 */
class CardController extends DefaultDialogController<CardDialog> implements DocumentListener, ItemListener {
  /**
   * Creates a controller.
   * 
   * @param dialog the dialog
   */
  CardController(CardDialog dialog) {
    super(dialog);
  }

  /**
   * Resets the associated dialog.
   * 
   * @param member the member to display
   */
  void reset(FamilyMember member) {
    if (member != null) {
      this.dialog.setTitle(I18n.getLocalizedString("dialog.update_card.title"));
      this.dialog.setId(member.getId());
      this.dialog.setImage(member.getImage());
      this.dialog.setFamilyName(member.getFamilyName().orElse(""));
      this.dialog.setUseName(member.getUseName().orElse(""));
      this.dialog.setFirstName(member.getFirstName().orElse(""));
      this.dialog.setOtherNames(member.getOtherNames().orElse(""));
      this.dialog.setGender(member.getGender());
      this.dialog.setBirthDate(member.getBirthDate());
      this.dialog.setBirthLocation(member.getBirthLocation().orElse(""));
      this.dialog.setDeathDate(member.getDeathDate());
      this.dialog.setDeathLocation(member.getDeathLocation().orElse(""));
      this.dialog.setDead(member.isDead());
      this.dialog.setComment(member.getComment().orElse(""));

      this.dialog.setValidateButtonEnabled(this.dialog.atLeastOneFieldCompleted());
    }
    else {
      this.dialog.setTitle(I18n.getLocalizedString("dialog.add_card.title"));
      this.dialog.setId(-1);
      this.dialog.setImage(Optional.empty());
      this.dialog.setFamilyName("");
      this.dialog.setUseName("");
      this.dialog.setFirstName("");
      this.dialog.setOtherNames("");
      this.dialog.setGender(Gender.UNKNOW);
      this.dialog.setBirthDate(Optional.empty());
      this.dialog.setBirthLocation("");
      this.dialog.setDeathDate(Optional.empty());
      this.dialog.setDeathLocation("");
      this.dialog.setDead(false);
      this.dialog.setComment("");

      this.dialog.setValidateButtonEnabled(false);
    }

    this.dialog.setCanceled(false);
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
    this.dialog.setCheckBoxEnabled(this.dialog.getDeathDate() == null && this.dialog.getDeathLocation() == null);
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
  public void changedUpdate(DocumentEvent e) {}
}
