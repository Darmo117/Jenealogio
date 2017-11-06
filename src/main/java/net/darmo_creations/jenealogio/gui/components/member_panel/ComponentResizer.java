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
package net.darmo_creations.jenealogio.gui.components.member_panel;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.CardResizeEvent;

/**
 * The ComponentResizer allows you to resize a component by dragging its borders.
 * 
 * @see <a href="https://tips4java.wordpress.com/2009/09/13/resizing-components/">Resizing
 *      Components</a>
 */
public class ComponentResizer extends MouseAdapter {
  private final static Dimension MINIMUM_SIZE = new Dimension(10, 10);
  private final static Dimension MAXIMUM_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

  protected static final int NORTH = 1;
  protected static final int WEST = 2;
  protected static final int SOUTH = 4;
  protected static final int EAST = 8;

  private static Map<Integer, Integer> cursors = new HashMap<>();

  static {
    cursors.put(1, Cursor.N_RESIZE_CURSOR);
    cursors.put(2, Cursor.W_RESIZE_CURSOR);
    cursors.put(4, Cursor.S_RESIZE_CURSOR);
    cursors.put(8, Cursor.E_RESIZE_CURSOR);
    cursors.put(3, Cursor.NW_RESIZE_CURSOR);
    cursors.put(9, Cursor.NE_RESIZE_CURSOR);
    cursors.put(6, Cursor.SW_RESIZE_CURSOR);
    cursors.put(12, Cursor.SE_RESIZE_CURSOR);
  }

  private Insets dragInsets;
  private Dimension snapSize;
  private Dimension minimumSize;
  private Dimension maximumSize;

  private int direction;
  private Cursor sourceCursor;
  private Rectangle bounds;
  private Point pressed;
  private boolean resizing;
  private boolean autoscrolls;
  private boolean onBorder;
  private boolean dragging;

  private FamilyMemberPanel panel;

  /**
   * Convenience contructor. All borders are resizable in increments of a single pixel. Component
   * can be registered when the class is created or it can be registered separately afterwards.
   *
   * @param component the component to be automatically registered
   */
  public ComponentResizer(FamilyMemberPanel component) {
    this(new Insets(5, 5, 5, 5), new Dimension(1, 1), component);
  }

  /**
   * Convenience contructor. Eligible borders are resisable in increments of a single pixel.
   * Component can be registered when the class is created or it can be registered separately
   * afterwards.
   *
   * @param dragInsets Insets specifying which borders are eligible to be resized.
   * @param component the component to be automatically registered
   */
  public ComponentResizer(Insets dragInsets, FamilyMemberPanel component) {
    this(dragInsets, new Dimension(1, 1), component);
  }

  /**
   * Creates a ComponentResizer.
   *
   * @param dragInsets Insets specifying which borders are eligible to be resized.
   * @param snapSize Specify the dimension to which the border will snap to when being dragged.
   *          Snapping occurs at the halfway mark.
   * @param component the component to be automatically registered
   */
  public ComponentResizer(Insets dragInsets, Dimension snapSize, FamilyMemberPanel component) {
    this.minimumSize = MINIMUM_SIZE;
    this.maximumSize = MAXIMUM_SIZE;
    setDragInsets(dragInsets);
    setSnapSize(snapSize);
    this.panel = component;
    this.panel.addMouseListener(this);
    this.panel.addMouseMotionListener(this);
  }

  /**
   * @return the drag insets
   */
  public Insets getDragInsets() {
    return this.dragInsets;
  }

  /**
   * Set the drag dragInsets. The insets specify an area where mouseDragged events are recognized
   * from the edge of the border inwards. A value of 0 for any size will imply that the border is
   * not resizable. Otherwise the appropriate drag cursor will appear when the mouse is inside the
   * resizable border area.
   *
   * @param dragInsets Insets to control which borders are resizeable.
   */
  public void setDragInsets(Insets dragInsets) {
    validateMinimumAndInsets(this.minimumSize, dragInsets);

    this.dragInsets = dragInsets;
  }

  /**
   * @return the component's maximum size
   */
  public Dimension getMaximumSize() {
    return this.maximumSize;
  }

  /**
   * Specifies the maximum size for the component. The component will still be constrained by the
   * size of its parent.
   *
   * @param maximumSize the maximum size for the component
   */
  public void setMaximumSize(Dimension maximumSize) {
    this.maximumSize = maximumSize;
  }

  /**
   * @return the component's minimum size
   */
  public Dimension getMinimumSize() {
    return this.minimumSize;
  }

  /**
   * Specifies the minimum size for the component. The minimum size is constrained by the drag
   * insets.
   *
   * @param minimumSize the minimum size for the component
   */
  public void setMinimumSize(Dimension minimumSize) {
    validateMinimumAndInsets(minimumSize, this.dragInsets);

    this.minimumSize = minimumSize;
  }

  /**
   * @return the snap size.
   */
  public Dimension getSnapSize() {
    return this.snapSize;
  }

  /**
   * Controls how many pixels a border must be dragged before the size of the component is changed.
   * The border will snap to the size once dragging has passed the halfway mark.
   *
   * @param snapSize dimension object allows you to separately specify a horizontal and vertical
   *          snap size
   */
  public void setSnapSize(Dimension snapSize) {
    this.snapSize = snapSize;
  }

  /**
   * Tells if the mouse is on the border.
   */
  public boolean isMouseOnBorder() {
    return this.onBorder;
  }

  /**
   * When the component's minimum size is less than the drag insets then we can't determine which
   * border should be resized so we need to prevent this from happening.
   */
  private void validateMinimumAndInsets(Dimension minimum, Insets drag) {
    int minimumWidth = drag.left + drag.right;
    int minimumHeight = drag.top + drag.bottom;

    if (minimum.width < minimumWidth || minimum.height < minimumHeight) {
      String message = "Minimum size cannot be less than drag insets";
      throw new IllegalArgumentException(message);
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Point location = e.getPoint();
    this.direction = 0;

    if (location.x < this.dragInsets.left)
      this.direction += WEST;

    if (location.x > this.panel.getWidth() - this.dragInsets.right - 1)
      this.direction += EAST;

    if (location.y < this.dragInsets.top)
      this.direction += NORTH;

    if (location.y > this.panel.getHeight() - this.dragInsets.bottom - 1)
      this.direction += SOUTH;

    if (this.direction == 0) {
      this.panel.setCursor(this.sourceCursor);
      this.onBorder = false;
    }
    else {
      int cursorType = cursors.get(this.direction);
      Cursor cursor = Cursor.getPredefinedCursor(cursorType);
      this.panel.setCursor(cursor);
      this.onBorder = true;
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    if (!this.resizing) {
      this.sourceCursor = this.panel.getCursor();
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if (!this.resizing) {
      this.panel.setCursor(this.sourceCursor);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e))
      return;

    // The mouseMoved event continually updates this variable
    if (this.direction == 0)
      return;

    // Setup for resizing. All future dragging calculations are done based
    // on the original bounds of the component and mouse pressed location.

    this.resizing = true;

    this.pressed = e.getPoint();
    SwingUtilities.convertPointToScreen(this.pressed, this.panel);
    this.bounds = this.panel.getBounds();

    // Making sure autoscrolls is false will allow for smoother resizing
    // of components
    this.autoscrolls = this.panel.getAutoscrolls();
    this.panel.setAutoscrolls(false);
  }

  /**
   * Restores the original state of the component.
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e))
      return;

    this.resizing = false;

    this.panel.setCursor(this.sourceCursor);
    this.panel.setAutoscrolls(this.autoscrolls);
    if (this.dragging) {
      this.dragging = false;
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardResizeEvent.Post(this.panel.getMemberId()));
    }
  }

  /**
   * Resize the component ensuring location and size is within the bounds of the parent container
   * and that the size is within the minimum and maximum constraints.
   *
   * All calculations are done using the bounds of the component when the resizing started.
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e))
      return;

    if (this.resizing) {
      Point dragged = e.getPoint();
      SwingUtilities.convertPointToScreen(dragged, this.panel);

      changeBounds(this.panel, this.direction, this.bounds, this.pressed, dragged);
    }
  }

  protected void changeBounds(FamilyMemberPanel source, int direction, Rectangle bounds, Point pressed, Point current) {
    int x = bounds.x;
    int y = bounds.y;
    int width = bounds.width;
    int height = bounds.height;

    // Resizing the West or North border affects the size and location
    if (WEST == (direction & WEST)) {
      int drag = getDragDistance(pressed.x, current.x, this.snapSize.width);
      int maximum = Math.min(width + x, this.maximumSize.width);
      drag = getDragBounded(drag, this.snapSize.width, width, this.minimumSize.width, maximum);

      x -= drag;
      width += drag;
    }

    if (NORTH == (direction & NORTH)) {
      int drag = getDragDistance(pressed.y, current.y, this.snapSize.height);
      int maximum = Math.min(height + y, this.maximumSize.height);
      drag = getDragBounded(drag, this.snapSize.height, height, this.minimumSize.height, maximum);

      y -= drag;
      height += drag;
    }

    // Resizing the East or South border only affects the size
    if (EAST == (direction & EAST)) {
      int drag = getDragDistance(current.x, pressed.x, this.snapSize.width);
      Dimension boundingSize = getBoundingSize(source);
      int maximum = Math.min(boundingSize.width - x, this.maximumSize.width);
      drag = getDragBounded(drag, this.snapSize.width, width, this.minimumSize.width, maximum);
      width += drag;
    }

    if (SOUTH == (direction & SOUTH)) {
      int drag = getDragDistance(current.y, pressed.y, this.snapSize.height);
      Dimension boundingSize = getBoundingSize(source);
      int maximum = Math.min(boundingSize.height - y, this.maximumSize.height);
      drag = getDragBounded(drag, this.snapSize.height, height, this.minimumSize.height, maximum);
      height += drag;
    }

    Rectangle newBounds = new Rectangle(x, y, width, height);
    source.setBounds(newBounds);
    source.validate();

    if (!bounds.equals(newBounds)) {
      if (!this.dragging) {
        this.dragging = true;
        ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardResizeEvent.Pre(this.panel.getMemberId()));
      }
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardResizeEvent.Resizing(this.panel.getMemberId(), bounds, newBounds));
    }
  }

  /**
   * Determines how far the mouse has moved from where dragging started.
   */
  private int getDragDistance(int larger, int smaller, int snapSize) {
    int halfway = snapSize / 2;
    int drag = larger - smaller;

    drag += (drag < 0) ? -halfway : halfway;

    return (drag / snapSize) * snapSize;
  }

  /**
   * Adjusts the drag value to be within the minimum and maximum range.
   */
  private int getDragBounded(int drag, int snapSize, int dimension, int minimum, int maximum) {
    while (dimension + drag < minimum)
      drag += snapSize;

    while (dimension + drag > maximum)
      drag -= snapSize;

    return drag;
  }

  /**
   * Keeps the size of the component within the bounds of its parent.
   */
  private Dimension getBoundingSize(FamilyMemberPanel source) {
    return source.getParent().getSize();
  }
}
