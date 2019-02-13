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
package net.darmo_creations.jenealogio.gui.components.canvas_view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

import net.darmo_creations.gui_framework.ApplicationRegistry;
import net.darmo_creations.jenealogio.events.CardDoubleClickEvent;
import net.darmo_creations.jenealogio.events.GoToObjectEvent;
import net.darmo_creations.jenealogio.events.LinkDoubleClickEvent;
import net.darmo_creations.jenealogio.events.SelectionChangeEvent;
import net.darmo_creations.jenealogio.gui.components.view.ViewController;
import net.darmo_creations.jenealogio.model.ViewType;
import net.darmo_creations.jenealogio.model.family.Family;
import net.darmo_creations.jenealogio.model.family.FamilyMember;
import net.darmo_creations.jenealogio.model.family.Relationship;
import net.darmo_creations.jenealogio.util.Pair;
import net.darmo_creations.jenealogio.util.Selection;
import net.darmo_creations.utils.events.SubscribeEvent;

/**
 * This controller handles cards and links selection and notifies the DisplayPanel.
 * 
 * @author Damien Vergnet
 */
class CanvasViewController extends ViewController<CanvasView> {
  /** The maximum distance away from a link the mouse must be to count as a hover. */
  private static final int HOVER_DISTANCE = 5;

  private static final int DEFAULT_COLUMN_WIDTH = 100;
  private static final int DEFAULT_ROW_HEIGHT = 32;

  private Point mouseLocation, absoluteMouseLocation;
  private Point selectionStart;
  private Rectangle selection;

  private Map<Long, FamilyMemberPanel> panels;
  private List<Link> links;
  /** All currently selected cards. Lis to keep selection order. */
  private List<GraphicalObject> selectedObjects;

  private GridCell[][] grid;
  private GridCell dropTargetCell;

  CanvasViewController() {
    super(ViewType.CANVAS);

    this.mouseLocation = new Point();
    this.absoluteMouseLocation = new Point();
    this.selectionStart = null;
    this.selection = null;

    this.panels = new HashMap<>();
    this.links = new ArrayList<>();
    this.selectedObjects = new ArrayList<>();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);

    if (!getView().hasFocus())
      getView().requestFocus();

    if (SwingUtilities.isLeftMouseButton(e)) {
      this.selectionStart = e.getPoint();
      if (!isMouseOverObject(this.mouseLocation)) {
        this.selection = new Rectangle(this.selectionStart);
        if (!getHoveredPanel(this.mouseLocation).isPresent() && !getHoveredLink(this.mouseLocation).isPresent())
          deselectAll();
      }
      else {
        Optional<FamilyMemberPanel> p = getHoveredPanel(this.mouseLocation);
        if (p.isPresent()) {
          FamilyMemberPanel panel = p.get();
          if (!panel.isSelected() && !panel.isSelectedBackground()) {
            objectClicked(panel, false);
          }
        }
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    super.mouseReleased(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      if (this.selection != null) {
        objectsSelected(getPanelsInsideRectangle(this.selection));
        this.selection = null;
        getView().repaint();
      }
    }
  }

  /**
   * Deselects all cards and checks if a link was clicked or double-clicked.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    super.mouseClicked(e);

    if (SwingUtilities.isLeftMouseButton(e)) {
      Optional<FamilyMemberPanel> p = getHoveredPanel(this.mouseLocation);
      boolean ctrlDown = e.isControlDown();

      if (p.isPresent()) {
        FamilyMemberPanel member = p.get();
        objectClicked(member, ctrlDown);
        if (!ctrlDown && e.getClickCount() == 2)
          ApplicationRegistry.EVENTS_BUS.dispatchEvent(new CardDoubleClickEvent(member.getId()));
      }
      else {
        Optional<Link> l = getHoveredLink(this.mouseLocation);

        if (l.isPresent()) {
          Link link = l.get();
          objectClicked(link, ctrlDown);
          if (!ctrlDown && e.getClickCount() == 2)
            ApplicationRegistry.EVENTS_BUS.dispatchEvent(new LinkDoubleClickEvent(link.getParent1(), link.getParent2()));
        }
      }
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    super.mouseMoved(e);
    updateMouseLocation(e);

    this.panels.forEach((id, p) -> p.setHovered(false));
    this.links.forEach(l -> l.setHovered(false));

    getHoveredLink(this.mouseLocation).ifPresent(l -> l.setHovered(true));
    getHoveredPanel(this.mouseLocation).ifPresent(p -> p.setHovered(true));
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    super.mouseDragged(e);

    Point prevAbsoluteLocation = this.absoluteMouseLocation;

    updateMouseLocation(e);

    if (SwingUtilities.isMiddleMouseButton(e)) {
      Point newLocation = this.absoluteMouseLocation;

      int xTrans = newLocation.x - prevAbsoluteLocation.x;
      int yTrans = newLocation.y - prevAbsoluteLocation.y;

      getView().setHorizontalScroll(getView().getHorizontalScroll() - xTrans);
      getView().setVerticalScroll(getView().getVerticalScroll() - yTrans);
    }
  }

  @SubscribeEvent
  public void onGoToObject(GoToObjectEvent e) {
    Object o = e.getObject();
    GraphicalObject obj = null;

    if (o instanceof FamilyMember) {
      Optional<FamilyMemberPanel> m = this.panels.entrySet().stream().map(entry -> entry.getValue()).filter(
          p -> p.getId() == ((FamilyMember) o).getId()).findAny();
      if (m.isPresent())
        obj = m.get();
    }
    else if (o instanceof Relationship) {
      Relationship r = (Relationship) o;
      Optional<Link> link = this.links.stream().filter(
          l -> l.getParent1() == r.getPartner1() || l.getParent2() == r.getPartner2()).findAny();
      if (link.isPresent())
        obj = link.get();
    }

    scrollToObject(obj);
  }

  void reset() {
    resetGrid();
    deselectAll();
    this.panels.clear();
    this.links.clear();
  }

  void refresh(Family family, CanvasState canvasStates) {
    Set<Long> updatedOrAddedPanels = new HashSet<>();
    Set<Long> panelsToDelete = new HashSet<>(this.panels.keySet());

    // Add/update members
    family.getAllMembers().forEach(member -> {
      long id = member.getId();
      CardState state = canvasStates.getCardStates().get(id);
      FamilyMemberPanel panel;

      if (this.panels.containsKey(id)) {
        panel = this.panels.get(id);
        panel.setInfo(member);
        getCell(panel.getLocationInGrid().y, panel.getLocationInGrid().x).setPanel(null);
      }
      else {
        panel = new FamilyMemberPanel(getView(), member);

        this.panels.put(id, panel);

        Rectangle panelBounds = panel.getBounds();
        Point p = state != null ? state.getLocation() : new Point();
        int addCol, addRow;

        do {
          addCol = 0;
          addRow = 0;
          Dimension size = getView().getPreferredSize();
          if (state != null && state.isPixelLocation()) {
            addCol = p.x + panelBounds.width > size.width ? 1 : 0;
            addRow = p.y + panelBounds.height > size.height ? 1 : 0;
          }
          else {
            int gw = getGridWidth();
            int gh = getGridHeight();
            if (p.x >= gw) {
              addCol = p.x - gw + 3;
            }
            if (p.y >= gh) {
              addRow = p.y - gh + 3;
            }
          }
          resizeGrid(addRow, addCol);
        } while (addCol != 0 || addRow != 0);
      }

      if (state != null) {
        panel.setState(state);
        Point p = state.getLocation();
        if (state.isPixelLocation()) {
          getCellPixel(p).setPanel(panel);
        }
        else {
          getCell(p.y, p.x).setPanel(panel);
        }
      }

      updatedOrAddedPanels.add(id);
    });

    // Delete members removed from the model
    panelsToDelete.removeAll(updatedOrAddedPanels);
    panelsToDelete.forEach(id -> this.panels.remove(id));

    List<Link> updatedOrAddedLinks = new ArrayList<>();
    // Add/update links
    family.getAllRelations().forEach(relation -> {
      FamilyMemberPanel partner1 = this.panels.get(relation.getPartner1());
      FamilyMemberPanel partner2 = this.panels.get(relation.getPartner2());
      Map<Long, Pair<Boolean, FamilyMemberPanel>> children = new HashMap<>();

      relation.getChildren().forEach(id -> children.put(id, new Pair<>(relation.isAdopted(id), this.panels.get(id))));

      Link link = new Link(getView(), partner1, partner2, children, relation.isWedding(), relation.hasEnded());

      if (this.links.contains(link)) {
        Link l = this.links.get(this.links.indexOf(link));

        l.setWedding(link.isWedding());
        l.setEnded(relation.hasEnded());
        l.setChildren(link.getChildren());
      }
      else {
        this.links.add(link);
      }
      updatedOrAddedLinks.add(link);
    });

    // Delete links removed from the model
    this.links.retainAll(updatedOrAddedLinks);

    packGrid();
  }

  /**
   * Deselects all components and sends a ViewEditEvent.
   */
  void deselectAll() {
    Selection old = getSelection();
    this.selectedObjects.clear();
    updateSelection(old);
  }

  Dimension getGridSize() {
    return new Dimension(getGridWidth(), getGridHeight());
  }

  int getGridWidth() {
    return this.grid[0].length;
  }

  int getGridHeight() {
    return this.grid.length;
  }

  GridCell[][] getGrid() {
    return this.grid;
  }

  /**
   * @return selection's starting position
   */
  Optional<Rectangle> getSelectionRectangle() {
    return Optional.ofNullable(this.selection);
  }

  /**
   * Returns the current selection.
   */
  Selection getSelection() {
    return new Selection(getSelectedPanels(), getSelectedLinks());
  }

  Stream<FamilyMemberPanel> getPanels() {
    return this.panels.values().stream();
  }

  Stream<Link> getLinks() {
    return this.links.stream();
  }

  CanvasState getState() {
    CanvasState states = new CanvasState();

    for (Map.Entry<Long, FamilyMemberPanel> e : this.panels.entrySet()) {
      states.addCardState(e.getKey(), e.getValue().getState());
    }

    return states;
  }

  /**
   * Scrolls to the card with the given ID.
   * 
   * @param id the ID
   */
  private void scrollToObject(GraphicalObject obj) {
    if (this.panels.containsValue(obj)) {
      FamilyMemberPanel p = (FamilyMemberPanel) obj;
      Rectangle r = p.getBounds();
      Rectangle v = getView().getCanvasVisibleRect();
      Rectangle r1 = new Rectangle(v.getSize());

      // Centers the component in the panel.
      r1.x = r.x - (v.width - r.width) / 2;
      r1.y = r.y - (v.height - r.height) / 2;
      getView().scrollRectToVisible(r1);
    }
    else if (obj instanceof Link) {
      Link l = (Link) obj;
      Point middle = l.getMiddle();
      Rectangle v = getView().getCanvasVisibleRect();
      Rectangle r = new Rectangle(v.getSize());

      r.x = middle.x - v.width / 2;
      r.y = middle.y - v.height / 2;
      getView().scrollRectToVisible(r);
    }
  }

  Optional<FamilyMemberPanel> getHoveredPanel(Point mouseLocation) {
    for (FamilyMemberPanel m : this.panels.values()) {
      if (m.getBounds().contains(mouseLocation))
        return Optional.of(m);
    }
    return Optional.empty();
  }

  Optional<Link> getHoveredLink(Point mouseLocation) {
    for (Link link : this.links) {
      Rectangle r1 = this.panels.get(link.getParent1()).getBounds();
      Rectangle r2 = this.panels.get(link.getParent2()).getBounds();
      Point p1 = new Point(r1.x + r1.width / 2, r1.y + r1.height / 2);
      Point p2 = new Point(r2.x + r2.width / 2, r2.y + r2.height / 2);

      if (isMouseOnSegment(mouseLocation, p1, p2))
        return Optional.of(link);
    }
    return Optional.empty();
  }

  private boolean isMouseOverObject(Point mouseLocation) {
    return getHoveredPanel(mouseLocation).isPresent() || getHoveredLink(mouseLocation).isPresent();
  }

  /**
   * Tells is the mouse is over a segment.
   * 
   * @param mouseLocation mouse's location
   * @param p1 one end
   * @param p2 the other end
   * @return true if and only if the cursor is within a distance of {@link #HOVER_DISTANCE} pixels
   *         from the link
   * @see <a href=
   *      "https://stackoverflow.com/questions/17581738/check-if-a-point-projected-on-a-line-segment-is-not-outside-it">detect
   *      if point is in segment range</a>
   * @see <a href=
   *      "https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_two_points">distance
   *      between a point and a line</a>
   */
  private boolean isMouseOnSegment(Point mouseLocation, Point p1, Point p2) {
    double dx = p2.getX() - p1.getX();
    double dy = p2.getY() - p1.getY();
    double innerProduct = (mouseLocation.getX() - p1.getX()) * dx + (mouseLocation.getY() - p1.getY()) * dy;
    boolean mouseInSegmentRange = 0 <= innerProduct && innerProduct <= dx * dx + dy * dy;

    double a = p2.getY() - p1.getY();
    double b = -(p2.getX() - p1.getX());
    double c = -a * p1.getX() - b * p1.getY();
    double d = Math.abs(a * mouseLocation.getX() + b * mouseLocation.getY() + c) / Math.hypot(a, b);

    return mouseInSegmentRange && d <= HOVER_DISTANCE;
  }

  void cardDragged(Point translation, Point mouseLocation) {
    this.mouseLocation = mouseLocation;
    this.selectedObjects.stream().filter(o -> o instanceof FamilyMemberPanel).map(o -> (FamilyMemberPanel) o).forEach(
        m -> m.setLocation(new Point(m.getLocation().x + translation.x, m.getLocation().y + translation.y)));
    resizePanelIfOutside();
    scrollIfOutside();
    setDropTargetCell(mouseLocation);
  }

  void cardDropped(long id) {
    this.dropTargetCell.setDropTarget(false);

    FamilyMemberPanel panel = this.panels.get(id);
    Point oldLocation = panel.getLocationInGrid();
    Point newLocation = this.dropTargetCell.getGridLocation();
    Point translation;

    if (oldLocation != null) {
      translation = new Point(newLocation.x - oldLocation.x, newLocation.y - oldLocation.y);
    }
    else {
      translation = new Point();
    }

    Map<Long, Point> newPositions = new HashMap<>();

    for (GraphicalObject o : this.selectedObjects) {
      if (o instanceof FamilyMemberPanel) {
        FamilyMemberPanel pane = (FamilyMemberPanel) o;
        Point p = pane.getLocationInGrid();

        if (p != null) {
          Point newP = new Point(p.x + translation.x, p.y + translation.y);
          if (getCell(newP.y, newP.x) == null || !getCell(newP.y, newP.x).isEmpty()) {
            packGrid();
            return;
          }
          newPositions.put(pane.getId(), newP);
        }
      }
    }

    this.panels.values().stream().filter(p -> newPositions.containsKey(p.getId())).forEach(pane -> {
      Point p = pane.getLocationInGrid();
      Point newP = newPositions.get(pane.getId());
      if (p != null)
        getCell(p.y, p.x).setPanel(null);
      getCell(newP.y, newP.x).setPanel(pane);
    });

    this.dropTargetCell.setPanel(panel);
    this.dropTargetCell = null;
    packGrid();
  }

  private void setDropTargetCell(Point pixel) {
    for (GridCell[] row : this.grid) {
      for (GridCell cell : row) {
        cell.setDropTarget(cell.getBounds().contains(pixel));
        if (cell.isDropTarget())
          this.dropTargetCell = cell;
      }
    }
  }

  private void packGrid() {
    int[] maxRowHeights = new int[getGridHeight()];
    int[] maxColWidths = new int[getGridWidth()];

    // Fetch max height/width for each row/column
    for (int row = 0; row < getGridHeight(); row++) {
      for (int col = 0; col < getGridWidth(); col++) {
        GridCell cell = getCell(row, col);
        cell.pack();
        if (cell.getPanel() != null) {
          maxRowHeights[row] = Math.max(maxRowHeights[row], cell.getHeight());
          maxColWidths[col] = Math.max(maxColWidths[col], cell.getWidth());
        }
      }
    }

    // Update heights
    for (int row = 0; row < maxRowHeights.length; row++) {
      int height = maxRowHeights[row] > 0 ? maxRowHeights[row] : DEFAULT_ROW_HEIGHT;
      for (int col = 0; col < maxColWidths.length; col++) {
        GridCell cell = getCell(row, col);
        cell.setHeight(height);
      }
    }

    // Update widths
    for (int col = 0; col < maxColWidths.length; col++) {
      int width = maxColWidths[col] > 0 ? maxColWidths[col] : DEFAULT_COLUMN_WIDTH;
      for (int row = 0; row < maxRowHeights.length; row++) {
        GridCell cell = getCell(row, col);
        cell.setWidth(width);
      }
    }

    // Update locations
    for (int row = 0; row < getGridHeight(); row++) {
      for (int col = 0; col < getGridWidth(); col++) {
        GridCell cell = getCell(row, col);
        Point p = cell.getGridLocation();
        GridCell topCell = getCell(p.y - 1, p.x);
        GridCell leftCell = getCell(p.y, p.x - 1);
        Point pos = new Point(leftCell != null ? leftCell.getBounds().x + leftCell.getWidth() + 1 : 0,
            topCell != null ? topCell.getBounds().y + topCell.getHeight() + 1 : 0);
        cell.setLocation(pos);
      }
    }

    getView().repaint();
  }

  private GridCell getCell(int row, int col) {
    if (col < 0 || col >= getGridWidth() || row < 0 || row >= getGridHeight())
      return null;
    return this.grid[row][col];
  }

  private GridCell getCellPixel(Point pixel) {
    for (GridCell[] row : this.grid) {
      for (GridCell cell : row) {
        if (cell.getBounds().contains(pixel))
          return cell;
      }
    }
    return null;
  }

  /**
   * Called when several objects are selected.
   * 
   * @param object the object
   */
  private void objectsSelected(List<? extends GraphicalObject> object) {
    Selection old = getSelection();

    this.selectedObjects.clear();
    this.selectedObjects.addAll(object);

    updateSelection(old);
  }

  /**
   * Called when an object is clicked.
   * 
   * @param object the object
   * @param keepSelection if true, the previous selection will be kept
   */
  private void objectClicked(GraphicalObject object, boolean keepSelection) {
    Selection old = getSelection();

    if (keepSelection) {
      if (this.selectedObjects.contains(object)) {
        this.selectedObjects.remove(object);
      }
      else {
        this.selectedObjects.add(object);
      }
    }
    else {
      this.selectedObjects.clear();
      this.selectedObjects.add(object);
    }

    updateSelection(old);
  }

  private void updateSelection(Selection old) {
    Selection current = getSelection();

    if (!current.equals(old)) {
      selectObjects(this.selectedObjects);
      ApplicationRegistry.EVENTS_BUS.dispatchEvent(new SelectionChangeEvent(old, current));
    }
  }

  private void selectObjects(List<GraphicalObject> objects) {
    this.panels.forEach((id, p) -> p.deselect());
    this.links.forEach(l -> l.deselect());

    if (!objects.isEmpty()) {
      objects.forEach(o -> o.setSelectedBackground());
      objects.get(objects.size() - 1).setSelected();
    }
    getView().repaint();
  }

  private void updateMouseLocation(MouseEvent e) {
    this.mouseLocation = e.getPoint();
    this.absoluteMouseLocation = e.getLocationOnScreen();

    // Selection computation
    if (this.selection != null) {
      int width = this.mouseLocation.x - this.selectionStart.x;
      int height = this.mouseLocation.y - this.selectionStart.y;

      if (width < 0) {
        width = -width;
        this.selection.x = this.selectionStart.x - width;
        this.selection.width = width;
      }
      else {
        this.selection.width = width;
      }
      if (height < 0) {
        height = -height;
        this.selection.y = this.selectionStart.y - height;
        this.selection.height = height;
      }
      else {
        this.selection.height = height;
      }
      scrollIfOutside();
    }
    getView().repaint();
  }

  private List<Long> getSelectedPanels() {
    return this.selectedObjects.stream().filter(o -> o instanceof FamilyMemberPanel).map(m -> m.getId()).collect(Collectors.toList());
  }

  private List<Pair<Long, Long>> getSelectedLinks() {
    return this.selectedObjects.stream().filter(o -> o instanceof Link).map(o -> {
      Link l = (Link) o;
      return new Pair<>(l.getParent1(), l.getParent2());
    }).collect(Collectors.toList());
  }

  /**
   * Returns all panels that are fully inside the given zone.
   * 
   * @param r the zone
   * @return all panels inside the zone
   */
  private List<FamilyMemberPanel> getPanelsInsideRectangle(Rectangle r) {
    if (r != null)
      return this.panels.entrySet().stream().filter(e -> r.contains(e.getValue().getBounds())).map(e -> e.getValue()).collect(
          Collectors.toList());
    return Collections.emptyList();
  }

  private static final int LEFT = 1;
  private static final int RIGHT = 2;
  private static final int TOP = 4;
  private static final int BOTTOM = 8;

  private int isOutsideRectangle(Point p, Rectangle r) {
    int res = 0;

    if (p.getX() < r.x)
      res = LEFT;
    else if (p.getX() > r.x + r.width)
      res = RIGHT;
    if (p.getY() < r.y)
      res |= TOP;
    else if (p.getY() > r.y + r.height)
      res |= BOTTOM;

    return res;
  }

  /**
   * Resizes the panel if a component is dragged outside.
   */
  private void resizePanelIfOutside() {
    Rectangle r = getView().getCanvasBounds();
    r.x = r.y = 0;
    int mouse = isOutsideRectangle(this.mouseLocation, r);
    int vAdd = 0;
    int hAdd = 0;
    final int step = 30;

    if ((mouse & RIGHT) != 0)
      hAdd = step;
    if ((mouse & BOTTOM) != 0)
      vAdd = step;

    if (vAdd != 0 || hAdd != 0) {
      int newCols = (int) ((hAdd + DEFAULT_COLUMN_WIDTH / 2.0) / DEFAULT_COLUMN_WIDTH);
      int newRows = (int) ((vAdd + DEFAULT_ROW_HEIGHT / 2.0) / DEFAULT_ROW_HEIGHT);
      resizeGrid(newRows, newCols); // FIXME scollpane not resized
    }
  }

  private void resizeGrid(int newRows, int newCols) {
    GridCell[][] newGrid = new GridCell[getGridHeight() + newRows][getGridWidth() + newCols];
    for (int row = 0; row < newGrid.length; row++) {
      for (int col = 0; col < newGrid[0].length; col++) {
        if (getCell(row, col) != null)
          newGrid[row][col] = getCell(row, col);
        else {
          int id = Long.hashCode(row | (col << 32));
          Rectangle bounds = new Rectangle(0, 0, DEFAULT_COLUMN_WIDTH, DEFAULT_ROW_HEIGHT);
          newGrid[row][col] = new GridCell(getView(), id, new Point(col, row), bounds);
        }
      }
    }
    this.grid = newGrid;
    setCanvasSize();
    packGrid();
  }

  /**
   * Scrolls if the mouse is outside the viewport.
   */
  private void scrollIfOutside() {
    int mouse = isOutsideRectangle(this.mouseLocation, getView().getViewportVisibleRect());
    int vTrans = 0;
    int hTrans = 0;
    final int step = 16;

    if ((mouse & LEFT) != 0)
      hTrans = -step;
    else if ((mouse & RIGHT) != 0)
      hTrans = step;
    if ((mouse & TOP) != 0)
      vTrans = -step;
    else if ((mouse & BOTTOM) != 0)
      vTrans = step;

    if (vTrans != 0)
      getView().setVerticalScroll(getView().getVerticalScroll() + vTrans);
    if (hTrans != 0)
      getView().setHorizontalScroll(getView().getHorizontalScroll() + hTrans);
  }

  private void resetGrid() {
    this.grid = new GridCell[50][30];
    for (int row = 0; row < getGridHeight(); row++) {
      for (int col = 0; col < getGridWidth(); col++) {
        int id = Long.hashCode(row | (col << 32));
        int x = col * DEFAULT_COLUMN_WIDTH;
        int y = row * DEFAULT_ROW_HEIGHT;
        this.grid[row][col] = new GridCell(getView(), id, new Point(col, row),
            new Rectangle(x, y, DEFAULT_COLUMN_WIDTH, DEFAULT_ROW_HEIGHT));
      }
    }
    setCanvasSize();
  }

  private void setCanvasSize() {
    getView().setCanvasPreferredSize(getGridPixelSize());
    getView().repaint();
  }

  private Dimension getGridPixelSize() {
    int width = Arrays.stream(this.grid[0]).map(c -> c.getWidth()).reduce(0, Integer::sum);
    int height = Arrays.stream(this.grid).map(c -> c[0].getHeight()).reduce(0, Integer::sum);
    return new Dimension(width, height);
  }
}
