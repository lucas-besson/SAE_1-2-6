package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.MerelleBoard;

import java.net.FileNameMap;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class MerelleBoardLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[][] cells;
    final int EMPTYCELL = -1;
    final int ACTIVCELL = 0;
    final int HORIZONTAL = 1;
    final int VERTICAL = 2;
    final int[][] GRIDLOOK = {
            {ACTIVCELL, HORIZONTAL, HORIZONTAL, ACTIVCELL, HORIZONTAL, HORIZONTAL, ACTIVCELL},
            {VERTICAL, ACTIVCELL, HORIZONTAL, ACTIVCELL, HORIZONTAL, ACTIVCELL, VERTICAL},
            {VERTICAL, VERTICAL, ACTIVCELL, ACTIVCELL, ACTIVCELL, VERTICAL, VERTICAL},
            {ACTIVCELL, ACTIVCELL, ACTIVCELL, EMPTYCELL, ACTIVCELL, ACTIVCELL, ACTIVCELL},
            {VERTICAL, VERTICAL, ACTIVCELL, ACTIVCELL, ACTIVCELL, VERTICAL, VERTICAL},
            {VERTICAL, ACTIVCELL, HORIZONTAL, ACTIVCELL, HORIZONTAL, ACTIVCELL, VERTICAL},
            {ACTIVCELL, HORIZONTAL, HORIZONTAL, ACTIVCELL, HORIZONTAL, HORIZONTAL, ACTIVCELL}};
    final int linkWidth = 5;

    public MerelleBoardLook(int size, GameElement element) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the element parameter.
        // If we create the 3x3 board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/3
        super(size, size, (size - 20) / MerelleBoard.GRIDNBROWS, (size - 20) / MerelleBoard.GRIDNBCOLS, 10, Color.WHITE.toString(), element);
        cells = new Rectangle[MerelleBoard.GRIDNBROWS][MerelleBoard.GRIDNBCOLS];
        // create the rectangles.
        for (int row = 0; row < MerelleBoard.GRIDNBROWS; row++) {
            for (int col = 0; col < MerelleBoard.GRIDNBCOLS; col++) {
                int cell = GRIDLOOK[row][col];
                if (cell == EMPTYCELL) {
                    cells[row][col] = new Rectangle(cellWidth, cellHeight, Color.WHITE);
                    cells[row][col].setX(borderWidth + col * cellWidth);
                    cells[row][col].setY(borderWidth + row * cellHeight);
                } else if (cell == ACTIVCELL) {
                    cells[row][col] = new Rectangle(cellWidth, cellHeight, Color.DARKGRAY);
                    cells[row][col].setStroke(Color.WHITE);
                    cells[row][col].setStrokeWidth(3);
                    cells[row][col].setX(borderWidth + col * cellWidth);
                    cells[row][col].setY(borderWidth + row * cellHeight);
                } else if (cell == HORIZONTAL) {
                    cells[row][col] = new Rectangle(cellWidth, linkWidth, Color.RED);
                    cells[row][col].setX(borderWidth + col * cellWidth);
                    cells[row][col].setY(borderWidth + row * cellHeight + cellHeight / 2.0);

                } else if (cell == VERTICAL) {
                    cells[row][col] = new Rectangle(linkWidth, cellHeight, Color.RED);
                    cells[row][col].setX(borderWidth + col * cellWidth + cellWidth / 2.0);
                    cells[row][col].setY(borderWidth + row * cellHeight);
                }
                addShape(cells[row][col]);
            }
        }
    }

    @Override
    public void onChange() {
        // in a pawn is selected, reachableCells changes. Thus, the look of the board must also changes.
        MerelleBoard board = (MerelleBoard) element;
        boolean[][] reach = board.getReachableCells();
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            if (reach[cell[1]][cell[0]]) {
                cells[cell[1]][cell[0]].setStrokeMiterLimit(10);
                cells[cell[1]][cell[0]].setStrokeType(StrokeType.INSIDE);
                cells[cell[1]][cell[0]].setStroke(Color.RED);
            } else {
                cells[cell[1]][cell[0]].setStroke(Color.WHITE);
            }
        }
//        for(int i=0;i<MerelleBoard.GRIDNBROWS;i++) {
//            for(int j=0;j<MerelleBoard.GRIDNBCOLS;j++) {
//                if (reach[i][j]) {
//                    cells[i][j].setStrokeWidth(3);
//                    cells[i][j].setStrokeMiterLimit(10);
//                    cells[i][j].setStrokeType(StrokeType.CENTERED);
//                    cells[i][j].setStroke(Color.valueOf("0x333333"));
//                } else {
//                    cells[i][j].setStrokeWidth(0);
//                }
//            }
//        }
    }
}
