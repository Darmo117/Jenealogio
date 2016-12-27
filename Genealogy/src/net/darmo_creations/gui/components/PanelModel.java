package net.darmo_creations.gui.components;

public class PanelModel {
  private boolean selected;
  private boolean enabled;
  private final long id;

  public PanelModel(long id, boolean enabled) {
    this.selected = false;
    this.enabled = enabled;
    this.id = id;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public long getId() {
    return this.id;
  }
}
