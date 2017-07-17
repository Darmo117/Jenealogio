package net.darmo_creations.events;

/**
 * This event is fired when an edit is going to be made.
 *
 * @author Damien Vergnet
 */
public class EditEvent extends AbstractEvent {
  private final Type type;

  /**
   * Creates an event.
   * 
   * @param type edit type
   * @see Type
   */
  public EditEvent(Type type) {
    this.type = type;
  }

  @Override
  public boolean isCancelable() {
    return false;
  }

  /**
   * @return edit type
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Edit types.
   *
   * @author Damien Vergnet
   */
  public static enum Type {
    CARD,
    LINK;
  }
}
