package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.GridElement;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class GridLook extends ElementLook {

    protected int width;
    protected int height;
    protected int cellWidth;
    protected int cellHeight;
    protected int borderWidth;
    protected String borderColor;
    private GridGeometry geometry;

    public GridLook(int width, int height, int cellWidth, int cellHeight, int borderWidth, String borderColor, GameElement element) {
        super(element);
        this.width = width;
        this.height = height;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;

        if (borderWidth > 0) {
            Rectangle back = new Rectangle(width, height, Color.valueOf(borderColor));
            addNode(back);
        }

        geometry = new GridGeometry(this);
    }

    public GridGeometry getGeometry() {
        return geometry;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    /**
     * Get the row,col of a cell in this grid from a location in the root pane of the scene (cf. View class)
     * @param x the location in the root pane (this, not including a menubar if it exists)
     * @param y the location in the root pane (this, not including a menubar if it exists)
     * @return the cell row and col in the grid
     */
    public int[] getCellFromRootPaneLocation(double x, double y) {
        /* x,y of element (which is the grid element associated to this grid look) are coordinates of the top-left corner of the grid in the root pane
           Thus, a simple translation of -x,-y is sufficient to convert x,y in root pane into
           the grid coordinate space
         */
        Point2D inMyGroup = new Point2D(x,y);
        inMyGroup = inMyGroup.subtract(element.getX(), element.getY());
        return getCellFromLocalLocation(inMyGroup);
    }

    /**
     * Get the row,col of a cell in this grid from a location in the whole scene.
     * @param x the location in the scene (including a menubar if it exists)
     * @param y the location in the scene (including a menubar if it exists)
     * @return the cell row and col in the grid
     */
    public int[] getCellFromSceneLocation(double x, double y) {
        // get the group node that contains the shapes/nodes of this grid and get the coordinates of p within this group
        Point2D inMyGroup = getGroup().sceneToLocal(x, y);
        return getCellFromLocalLocation(inMyGroup);
    }
    /**
     * Get the row,col of a cell in this grid from a location in the whole scene.
     * @param p the location in the scene (including a menubar if it exists)
     * @return the cell row and col in the grid
     */
    public int[] getCellFromSceneLocation(Point2D p) {
        // get the group node that contains the shapes/nodes of this grid and get the coordinates of p within this group
        Point2D inMyGroup = getGroup().sceneToLocal(p.getX(), p.getY());
        return getCellFromLocalLocation(inMyGroup);
    }

    /* default computation, may be overridden in subclasses :
       just divide the width,height by  cellWidth,cellHeight to find the correct cell
     */
    public int[] getCellFromLocalLocation(Point2D p) {
        if ((p.getX() < 0) || (p.getX() >= width) || (p.getY() < 0) || (p.getY() >= height)) return null;
        // must remove the border width
        p = p.subtract(borderWidth, borderWidth);
        int[] tab = {(int)p.getY() / cellHeight, (int)p.getX() / cellWidth}; // row first, columns in second
        return tab;
    }

    /* *********************************************
       TRAMPOLINE METHODS
       NB: gain access to the current grid geometry
     ********************************************* */
    /*Law of Demeter*/
    public Point2D getRootPaneLocationForCellCenter(int row, int col) {
        return geometry.getRootPaneLocationForCellCenter(row, col);
    }
    public Point2D getRootPaneLocationForCell(int row, int col, int position) {
        return geometry.getRootPaneLocationForCell(row, col, position);
    }
}
