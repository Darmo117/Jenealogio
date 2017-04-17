package net.darmo_creations.model;

import java.awt.Color;
import java.util.Locale;

public class GlobalConfig {
  private Locale locale;

  private Color panelBorderColor, panelSelectionColor;
  private Color unknownGenderColor, femaleColor, maleColor;
  private Color linkColor, childLinkColor, linkHoverColor, linkSelectionColor;

  public GlobalConfig() {
    setLocale(Locale.US);
    setPanelBorderColor(Color.GRAY);
    setPanelSelectionColor(Color.BLUE);
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

  public Color getPanelBorderColor() {
    return this.panelBorderColor;
  }

  public void setPanelBorderColor(Color panelBorderColor) {
    this.panelBorderColor = panelBorderColor;
  }

  public Color getPanelSelectionColor() {
    return this.panelSelectionColor;
  }

  public void setPanelSelectionColor(Color panelSelectionColor) {
    this.panelSelectionColor = panelSelectionColor;
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
}
