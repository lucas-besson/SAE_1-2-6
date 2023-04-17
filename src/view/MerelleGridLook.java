package view;

import boardifier.model.GridElement;
import boardifier.view.ConsoleColor;
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
                        shape[i*cellHeight][j*cellWidth] = "\u250C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2510";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2514";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u253C";
                    }
                    else if (j==0 && i==nbRows-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u250C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u253C";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2514";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2518";
                    }
                    else if (i==0 && j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u250C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2510";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u253C";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2518";
                    }
                    else if (i==nbRows-1 && j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u253C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2510";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2514";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2518";
                    }
                    else if (i==nbRows-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u251C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2524";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2514";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2518";
                    }
                    else if (j==nbCols-1){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u252C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2510";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2534";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2518";
                    }
                    else if (i==0) {
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u250C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u2510";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u251C";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2524";
                    }
                    else if (j==0) {
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u250C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u252C";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u2514";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u2534";
                    }
                    else if (i>=2 && j>=2 && i<nbRows-2 && j<nbCols-2){
                        // Corners
                        shape[i*cellHeight][j*cellWidth] = "\u253C";
                        shape[i*cellHeight][(j+1)*cellWidth] = "\u253C";
                        shape[(i+1)*cellHeight][j*cellWidth] = "\u253C";
                        shape[(i+1)*cellHeight][(j+1)*cellWidth] = "\u253C";
                    }
                    
                    // Borders
                    for(int k=1;k<cellWidth;k++) {
                        shape[i*cellHeight][j*cellWidth+k] = "\u2500";
                        shape[(i+1)*cellHeight][j*cellWidth+k] = "\u2500";
                    }
                    for(int k=1;k<cellHeight;k++) {
                        shape[i*cellHeight+k][j*cellWidth] = "\u2502";
                        shape[i*cellHeight+k][(j+1)*cellWidth] = "\u2502";
                    }
                }
                // else {
                //     if (i==0) {
                //         for(int k=1;k<cellWidth;k++) {
                //             shape[i*cellHeight + cellHeight/2][j*cellWidth+k] = "\u2500";
                //         }
                //     }
                //     else if (j==0) {
                //         for(int k=1;k<cellHeight;k++) {
                //             shape[i*cellHeight+k][j*cellWidth + cellWidth/2] = "\u2502";
                //         }
                //     }
                //     else if (i==cellHeight-1) {

                //     }
                //     else if (j==cellWidth-1) {

                //     }
                //     else if (i==1) {

                //     }
                //     else if (j==1) {

                //     }
                //     else if (i==cellHeight-2) {

                //     }
                //     else if (j==cellWidth-2) {
                    
                //     }
                // }
            }
            //  last corners
            shape[5*cellHeight][1*cellWidth] = "\u250C";
            shape[1*cellHeight][5*cellWidth] = "\u250C";
            shape[1*cellHeight][(1+1)*cellWidth] = "\u2510";
            shape[5*cellHeight][(5+1)*cellWidth] = "\u2510";
            shape[(1+1)*cellHeight][1*cellWidth] = "\u2514";
            shape[(5+1)*cellHeight][5*cellWidth] = "\u2514";
            shape[(1+1)*cellHeight][(5+1)*cellWidth] = "\u2518";
            shape[(5+1)*cellHeight][(1+1)*cellWidth] = "\u2518";
            // draw coordinates

        // Creation of the lines between the boxes :
            // Definition of the line color :
            String color = ConsoleColor.RED;

            // Vertical :
            shape[(6*(cellHeight)+ cellHeight/2)][4*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth*2; j++) {
                shape[6*cellHeight+ cellHeight/2][4*cellWidth+j] = color +"\u2500" + ConsoleColor.RESET;
            }
            shape[(6*(cellHeight)+ cellHeight/2)][6*cellWidth] = "\u2524";


            shape[(6*(cellHeight)+ cellHeight/2)][cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth*2; j++) {
                shape[6*cellHeight+ cellHeight/2][cellWidth+j] = color + "\u2500" + ConsoleColor.RESET;
            }
            shape[(6*(cellHeight)+ cellHeight/2)][3*cellWidth] = "\u2524";

            shape[(5*(cellHeight)+ cellHeight/2)][2*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth; j++) {
                shape[5*cellHeight+ cellHeight/2][2*cellWidth+j] = color + "\u2500" + ConsoleColor.RESET;
            }
            shape[(5*(cellHeight)+ cellHeight/2)][3*cellWidth] = "\u2524";

            shape[(5*(cellHeight)+ cellHeight/2)][4*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth; j++) {
                shape[5*cellHeight+ cellHeight/2][4*cellWidth+j] = color + "\u2500" + ConsoleColor.RESET;
            }
            shape[(5*(cellHeight)+ cellHeight/2)][5*cellWidth] = "\u2524";

            // Symmetry --------------------------------------------------------

            shape[(1*(cellHeight)+ cellHeight/2)][2*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth; j++) {
                shape[1*cellHeight+ cellHeight/2][2*cellWidth+j] = color+"\u2500"+ ConsoleColor.RESET;
            }
            shape[(1*(cellHeight)+ cellHeight/2)][3*cellWidth] = "\u2524";

            shape[(1*(cellHeight)+ cellHeight/2)][4*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth; j++) {
                shape[1*cellHeight+ cellHeight/2][4*cellWidth+j] = color+ "\u2500" + ConsoleColor.RESET;
            }
            shape[(1*(cellHeight)+ cellHeight/2)][5*cellWidth] = "\u2524";

            shape[cellHeight/2][4*cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth*2; j++) {
                shape[cellHeight/2][4*cellWidth+j] = color + "\u2500" + ConsoleColor.RESET;
            }
            shape[cellHeight/2][6*cellWidth] = "\u2524";

            shape[cellHeight/2][cellWidth] = "\u251C";
            for (int j = 1; j < cellWidth*2; j++) {
                shape[cellHeight/2][cellWidth+j] = color + "\u2500" + ConsoleColor.RESET;
            }
            shape[cellHeight/2][3*cellWidth] = "\u2524";


            // Horizontal :

            shape[cellHeight][cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth; j++) {
                shape[cellHeight+j][cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[3*cellHeight][cellWidth/2] = "\u2534";

            shape[4*cellHeight][cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth; j++) {
                shape[4*cellHeight+j][cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[6*cellHeight][cellWidth/2] = "\u2534";

            shape[2*cellHeight][cellWidth+ cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth/2; j++) {
                shape[2*cellHeight+j][cellWidth+cellWidth/2] = color + "\u2502" +ConsoleColor.RESET;
            }
            shape[3*cellHeight][(cellWidth)+ cellWidth/2]  = "\u2534";

            shape[4*cellHeight][cellWidth+ cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth/2; j++) {
                shape[4*cellHeight+j][cellWidth+cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[5*cellHeight][(cellWidth)+ cellWidth/2]  = "\u2534";

            // Symmetry --------------------------------------------------------

            shape[cellHeight][6*(cellWidth)+cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth; j++) {
                shape[cellHeight+j][6*(cellWidth)+cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[3*cellHeight][6*(cellWidth)+cellWidth/2] = "\u2534";

            shape[4*cellHeight][6*(cellWidth) + cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth; j++) {
                shape[4*cellHeight+j][6*(cellWidth)+cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[6*cellHeight][6*(cellWidth)+cellWidth/2] = "\u2534";

            shape[2*cellHeight][5*(cellWidth)+ cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth/2; j++) {
                shape[2*cellHeight+j][5*(cellWidth)+cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[3*cellHeight][5*(cellWidth)+cellWidth/2]  = "\u2534";

            shape[4*cellHeight][5*(cellWidth)+ cellWidth/2] = "\u252C";
            for (int j = 1; j < cellWidth/2; j++) {
                shape[4*cellHeight+j][5*(cellWidth)+cellWidth/2] = color + "\u2502" + ConsoleColor.RESET;
            }
            shape[5*cellHeight][5*(cellWidth)+ cellWidth/2]  = "\u2534";

        //End of the creation of the lines between the boxes

            // Adding the cross to the center of the tray
            shape[(3*(cellHeight)+ cellHeight/2)][3*(cellWidth)+ cellWidth/2] = ConsoleColor.BLUE + "\u2573" + ConsoleColor.RESET;

        }
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