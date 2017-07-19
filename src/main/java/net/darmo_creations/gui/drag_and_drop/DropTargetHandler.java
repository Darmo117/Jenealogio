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
package net.darmo_creations.gui.drag_and_drop;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This listener handles files drag-and-drop events.
 *
 * @author Damien Vergnet
 */
public class DropTargetHandler extends DropTargetAdapter {
  private DropHandler handler;
  private Component component;

  /**
   * Creates a listener with an associated drop handler. The handler will take decisions to import
   * or reject files.
   * 
   * @param handler the drop handler
   * @param component the component
   */
  public DropTargetHandler(DropHandler handler, Component component) {
    this.handler = handler;
  }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
    processDrag(dtde);
  }

  @Override
  public void dragOver(DropTargetDragEvent dtde) {
    processDrag(dtde);
  }

  @Override
  public void drop(DropTargetDropEvent dtde) {
    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      dtde.acceptDrop(dtde.getDropAction());

      try {
        @SuppressWarnings("unchecked")
        List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

        if (this.handler.acceptFiles(files, this.component)) {
          this.handler.importFiles(files);
          dtde.dropComplete(true);
        }
      }
      catch (IOException | UnsupportedFlavorException __) {
        dtde.rejectDrop();
      }
    }
    else {
      dtde.rejectDrop();
    }
  }

  /**
   * Processes the drag event.
   */
  private void processDrag(DropTargetDragEvent dtde) {
    boolean fileAccepted = false;

    try {
      @SuppressWarnings("unchecked")
      List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
      fileAccepted = this.handler.acceptFiles(files, this.component);
    }
    catch (UnsupportedFlavorException | IOException __) {}

    if (fileAccepted)
      dtde.acceptDrag(DnDConstants.ACTION_COPY);
    else
      dtde.rejectDrag();
  }
}