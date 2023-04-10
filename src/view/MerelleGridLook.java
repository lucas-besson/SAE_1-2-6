package view;

import boardifier.model.GridElement;
import boardifier.view.GridLook;
import model.MerelleBoard;

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
        // SEE ALL THE UNICODE CHAR HERE : https://www.w3.org/TR/xml-entity-names/025.html
        // use the activeCell static variable of MerelleBoard to draw the lines/corner/border of the right cells
        // do the oposite to link unused cells
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (MerelleBoard.isActiveCell(i, j)){
                    if (i==0 && j==0) {
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2554";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u256C";
                    }
                    else if (j==0 && i==nbRows-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2554";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u256C";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";
                    }
                    else if (i==0 && j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2554";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u256C";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";
                    }
                    else if (i==nbRows-1 && j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u256C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";
                    }
                    else if (i==nbRows-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2560";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2563";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";
                    }
                    else if (j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2566";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2569";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u255D";
                    }
                    else if (i==0) {
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2554";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2557";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2560";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2563";
                    }
                    else if (j==0) {
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u2554";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2566";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u255A";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2569";
                    }
                    else if (i>=2 && j>=2 && i<nbRows-2 && j<nbCols-2){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u256C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u256C";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u256C";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u256C";
                    }
                    
                    // Borders
                    for(int k=1;k<cellWidth;k++) {
                        shape[i*cellHeight][j*cellWidth+k] = "\u2550";
                        shape[(i+1)*cellHeight][j*cellWidth+k] = "\u2550";
                    }
                    for(int k=1;k<cellHeight;k++) {
                        shape[i*cellHeight+k][j*cellWidth] = "\u2551";
                        shape[i*cellHeight+k][(j+1)*cellWidth] = "\u2551";
                    }
                }
            }
        }
        //  last corners
        shape[5*cellHeight][1*cellWidth] = "\u2554";
        shape[1*cellHeight][5*cellWidth] = "\u2554";
        shape[1*cellHeight][(1+1)*cellWidth] = "\u2557";
        shape[5*cellHeight][(5+1)*cellWidth] = "\u2557";
        shape[(1+1)*cellHeight][1*cellWidth] = "\u255A";
        shape[(5+1)*cellHeight][5*cellWidth] = "\u255A";
        shape[(1+1)*cellHeight][(5+1)*cellWidth] = "\u255D";
        shape[(5+1)*cellHeight][(1+1)*cellWidth] = "\u255D";
        // draw coordinates
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
