/*
 * Copyright © 2018 Damien Vergnet
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

/**
 * Base class for family tree views.
 * 
 * @author Damien Vergnet
 *
 * @param <T> Controller type
 */
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
