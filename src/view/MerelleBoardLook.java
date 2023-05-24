package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.HoleBoard;
import model.MerelleBoard;

public class MerelleBoardLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[][] cells;

    public MerelleBoardLook(int size, GameElement element) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the element parameter.
        // If we create the 3x3 board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/3
        super(size, size, (size-20)/MerelleBoard.GRIDNBROWS, (size-20)/MerelleBoard.GRIDNBCOLS, 10, "0X000000", element);
        cells = new Rectangle[MerelleBoard.GRIDNBROWS][MerelleBoard.GRIDNBCOLS];
        // create the rectangles.
        for (int i=0;i<MerelleBoard.GRIDNBROWS;i++) {
            for(int j=0;j<MerelleBoard.GRIDNBCOLS;j++) {
                Color c;
                if (MerelleBoard.isActiveCell(j,i)) {
                    c = Color.DARKGRAY;
                }
                else {
                    c = Color.BEIGE;
                }
                cells[i][j] = new Rectangle(cellWidth, cellHeight, c);
                cells[i][j].setX(j*cellWidth+borderWidth);
                cells[i][j].setY(i*cellHeight+borderWidth);
                addShape(cells[i][j]);
            }
        }
    }

    @Override
    public void onChange() {
        // in a pawn is selected, reachableCells changes. Thus, the look of the board must also changes.
        MerelleBoard board = (MerelleBoard)element;
        boolean[][] reach = board.getReachableCells();
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            if (reach[cell[1]][cell[0]]) {
                cells[cell[1]][cell[0]].setStrokeWidth(3);
                cells[cell[1]][cell[0]].setStrokeMiterLimit(10);
                cells[cell[1]][cell[0]].setStrokeType(StrokeType.CENTERED);
                cells[cell[1]][cell[0]].setStroke(Color.valueOf("0x333333"));
            } else {
                cells[cell[1]][cell[0]].setStrokeWidth(0);
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
