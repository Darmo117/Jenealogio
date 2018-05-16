package net.darmo_creations.jenealogio.gui.components.canvas_view;

import java.util.HashMap;
import java.util.Map;

public final class CanvasState implements Cloneable {
  private Map<Long, CardState> cardStates;

  public CanvasState() {
    this.cardStates = new HashMap<>();
  }

  public Map<Long, CardState> getCardStates() {
    return new HashMap<>(this.cardStates);
  }

  public void addCardState(long id, final CardState state) {
    this.cardStates.put(id, state);
  }

  @Override
  public CanvasState clone() {
    try {
      CanvasState state = (CanvasState) super.clone();
      state.cardStates = new HashMap<>(this.cardStates);
      return state;
    }
    catch (CloneNotSupportedException e) {
      throw new Error(e);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.cardStates == null) ? 0 : this.cardStates.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CanvasState other = (CanvasState) obj;
    if (this.cardStates == null) {
      if (other.cardStates != null)
        return false;
    }
    else if (!this.cardStates.equals(other.cardStates))
      return false;
    return true;
  }
}
