package net.darmo_creations.model;

import java.awt.Color;
import java.util.Locale;

import net.darmo_creations.util.I18n;

public class GlobalConfig implements Cloneable {
  private Locale locale;

  private Color cardBorderColor, cardSelectionColor;
  private Color unknownGenderColor, femaleColor, maleColor;
  private Color linkColor, childLinkColor, linkHoverColor, linkSelectionColor;

  public GlobalConfig() {
    setLocale(I18n.Language.ENGLISH.getLocale());
    setCardBorderColor(Color.GRAY);
    setCardSelectionColor(Color.BLUE);
    setUnknownGenderColor(Color.GRAY);
    setMaleColor(new Color(117, 191, 255));
    setFemaleColor(new Color(37, 177, 19));
    setLinkColor(Color.BLACK);
    setChildLinkColor(Color.BLUE);
    setLinkHoverColor(Color.RED);
    setLinkSelectionColor(Color.GREEN);
  }

  public Locale getLocale() {
    return this.locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public Color getCardBorderColor() {
    return this.cardBorderColor;
  }

  public void setCardBorderColor(Color cardBorderColor) {
    this.cardBorderColor = cardBorderColor;
  }

  public Color getCardSelectionColor() {
    return this.cardSelectionColor;
  }

  public void setCardSelectionColor(Color cardSelectionColor) {
    this.cardSelectionColor = cardSelectionColor;
  }

  public Color getUnknownGenderColor() {
    return this.unknownGenderColor;
  }

  public void setUnknownGenderColor(Color unknownGenderColor) {
    this.unknownGenderColor = unknownGenderColor;
  }

  public Color getFemaleColor() {
    return this.femaleColor;
  }

  public void setFemaleColor(Color femaleColor) {
    this.femaleColor = femaleColor;
  }

  public Color getMaleColor() {
    return this.maleColor;
  }

  public void setMaleColor(Color maleColor) {
    this.maleColor = maleColor;
  }

  public Color getLinkColor() {
    return this.linkColor;
  }

  public void setLinkColor(Color linkColor) {
    this.linkColor = linkColor;
  }

  public Color getChildLinkColor() {
    return this.childLinkColor;
  }

  public void setChildLinkColor(Color childLinkColor) {
    this.childLinkColor = childLinkColor;
  }

  public Color getLinkHoverColor() {
    return this.linkHoverColor;
  }

  public void setLinkHoverColor(Color linkHoverColor) {
    this.linkHoverColor = linkHoverColor;
  }

  public Color getLinkSelectionColor() {
    return this.linkSelectionColor;
  }

  public void setLinkSelectionColor(Color linkSelectionColor) {
    this.linkSelectionColor = linkSelectionColor;
  }

  @Override
  public GlobalConfig clone() {
    try {
      GlobalConfig config = (GlobalConfig) super.clone();
      config.setLocale((Locale) getLocale().clone());
      return config;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }
}
