package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;

public class MerelleGridLook extends GridLook {

    public MerelleGridLook(int cellWidth, int cellHeight, GridElement gridElement, int depth, boolean showCoords) {
        super(cellWidth, cellHeight, gridElement, depth, showCoords);
    }
    @Override
    protected void createShape() {
        // draw cells
        GridElement gridElement = (GridElement) element;
        int nbRows = gridElement.getNbRows();
        int nbCols = gridElement.getNbCols();
        // start by drawing the border of each cell, which will be change after 
        // FIXME: Changer le look pour avoir la grid caractéristique de la merelle + utiliser cette GridLook partout
        // TODO : LUCAS
        // use the activeCell static variable of MerelleBoard to draw the lines/corner/border of the right cells
        // do the oposite to link unused cells
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                //top-left corner
                shape[i*cellHeight][j*cellWidth] = "\u2554";
                // top-right corner
                shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                //bottom-left corner
                shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                // bottom-right corner
                shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";

                for(int k=1;k<cellWidth;k++) {
                    shape[i*cellHeight][j*cellWidth+k] = "\u2550";
                    shape[(i+1)*cellHeight][j*cellWidth+k] = "\u2550";
                }
                // draw left & righ vertical lines
                for(int k=1;k<cellHeight;k++) {
                    shape[i*cellHeight+k][j*cellWidth] = "\u2551";
                    shape[i*cellHeight+k][(j+1)*cellWidth] = "\u2551";
                }
            }
        }
        // change intersections on first & last hori. border
        for (int j = 1; j < nbCols; j++) {
            shape[0][j*cellWidth] = "\u2566";
            shape[nbRows*cellHeight][j*cellWidth] = "\u2569";
        }
        // change intersections on first & last vert. border
        for (int i = 1; i < nbRows; i++) {
            shape[i*cellHeight][0] = "\u2560";
            shape[i*cellHeight][nbCols*cellWidth] = "\u2563";
        }
        // change intersections within
        for (int i = 1; i < nbRows; i++) {
            for (int j = 1; j < nbCols; j++) {
                shape[i*cellHeight][j*cellWidth] = "\u256C";
            }
        }
        // draw the coords, if needed
        if (showCoords) {
            for (int i = 0; i < nbRows; i++) {
                shape[(int) ((i + 0.5) * cellHeight)][nbCols * cellWidth + 1] = String.valueOf(i+1);
            }
            for (int j = 0; j < nbCols; j++) {
                char c = (char) (j + 'A');
                shape[nbRows * cellHeight + 1][(int) ((j + 0.5) * cellWidth)] = String.valueOf(c);
            }
        }
    }
}
