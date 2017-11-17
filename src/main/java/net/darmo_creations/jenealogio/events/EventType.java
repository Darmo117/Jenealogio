package net.darmo_creations.jenealogio.events;

import net.darmo_creations.gui_framework.events.UserEvent;

public enum EventType implements UserEvent.Type {
  NEW,
  EDIT_TREE,
  OPEN,
  SAVE,
  SAVE_AS,
  UNDO,
  REDO,
  ADD_CARD,
  ADD_LINK,
  EDIT_OBJECT,
  DELETE_OBJECT,
  EDIT_COLORS,
  EXPORT_IMAGE;
}
