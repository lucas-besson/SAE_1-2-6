package boardifier.view;

import boardifier.model.GridElement;
import javafx.geometry.Point2D;

public class GridGeometry {

    private final GridLook look;
    // constant to defined which point within the cell is required
    public final static int GRIDGEOMETRY_CENTER = 0;
    public final static int GRIDGEOMETRY_TOPLEFT = 1;
    public final static int GRIDGEOMETRY_TOPRIGHT = 2;
    public final static int GRIDGEOMETRY_BOTTOMRIGHT = 3;
    public final static int GRIDGEOMETRY_BOTTOMLEFT = 4;

    public GridGeometry(GridLook look) {
        this.look = look;
    }


    public Point2D getRootPaneLocationForCellCenter(int row, int col) {
        return getRootPaneLocationForCell(row, col, GRIDGEOMETRY_CENTER);
    }
    /*
       It is used to compute the location x,y in the root pane in the scene (cf. View) of the cell
        in row,col. This method just calls the method computeLocationIfMove() and
       convert the result to obtain coordinates in the root pane.
     */
    public Point2D getRootPaneLocationForCell(int row, int col, int position) {
        Point2D local = getLocalLocationForCell(row, col, position);
        return local.add(look.getElement().getX(), look.getElement().getY());
    }

    /*
       default computation, may be overridden in subclasses :
       return the center of the cell
     */
    private Point2D getLocalLocationForCell(int row, int col, int position) {
        // IMPORTANT : checking if it is possible to put the pawn is not done here
        GridElement grid = (GridElement) look.getElement();
        if ((row < 0) || (row >= grid.getNbRows()) || (col < 0) || (col >= grid.getNbCols())) return null;
        Point2D p = null;
        if (position == GRIDGEOMETRY_CENTER) {
            p = new Point2D((col + 0.5) * look.getCellWidth(), (row + 0.5) * look.getCellHeight());
        }
        else if (position == GRIDGEOMETRY_TOPLEFT) {
            p = new Point2D(col * look.getCellWidth(), row * look.getCellHeight());
        }
        else if (position == GRIDGEOMETRY_TOPRIGHT) {
            p = new Point2D((col + 1) * look.getCellWidth(), row * look.getCellHeight());
        }
        else if (position == GRIDGEOMETRY_BOTTOMRIGHT) {
            p = new Point2D((col + 1) * look.getCellWidth(), (row + 1) * look.getCellHeight());
        }
        else if (position == GRIDGEOMETRY_BOTTOMLEFT) {
            p = new Point2D(col * look.getCellWidth(), (row + 1) * look.getCellHeight());
        }
        // must add the border width
        return p.add(look.getBorderWidth(), look.getBorderWidth());
    }

}
