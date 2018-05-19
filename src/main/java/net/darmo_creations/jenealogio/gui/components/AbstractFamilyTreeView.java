package net.darmo_creations.jenealogio.gui.components;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.util.TooManyListenersException;

import javax.swing.JComponent;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.gui_framework.config.WritableConfig;
import net.darmo_creations.jenealogio.gui.components.canvas_view.CanvasState;
import net.darmo_creations.jenealogio.gui.components.view.View;
import net.darmo_creations.jenealogio.gui.components.view.ViewController;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropListener;
import net.darmo_creations.utils.swing.drag_and_drop.DragAndDropTarget;
import net.darmo_creations.utils.swing.drag_and_drop.DropTargetHandler;

public abstract class AbstractFamilyTreeView<T extends ViewController<?>> extends View<T> implements DragAndDropTarget {
  private static final long serialVersionUID = -312710767375109600L;

  private DropTarget dropTarget;

  public AbstractFamilyTreeView(String name, T controller) {
    super(name, controller);

    ApplicationRegistry.EVENTS_BUS.register(controller);
  }

  protected void initDropTarget(JComponent target) {
    this.dropTarget = new DropTarget(target, DnDConstants.ACTION_COPY_OR_MOVE, null);
  }

  /**
   * Adds a drag-and-drop listener.
   * 
   * @param handler the new handler
   */
  @Override
  public void addDragAndDropListener(DragAndDropListener l) {
    try {
      this.dropTarget.addDropTargetListener(new DropTargetHandler(l, this));
    }
    catch (TooManyListenersException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Refreshes the display from the given model.
   * 
   * @param family the model
   * @param canvasStates states of internal components
   * @param config global config object
   */
  public abstract void refresh(Family family, CanvasState canvasStates, WritableConfig config);

  /**
   * Returns the state of internal components.
   */
  public abstract CanvasState getState();

  /**
   * Exports this view's content to an image.
   */
  public abstract BufferedImage exportToImage();
}
