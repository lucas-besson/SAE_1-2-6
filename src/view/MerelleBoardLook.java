package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import model.MerelleBoard;

import java.io.IOException;

public class MerelleBoardLook extends GridLook {
    Image millIMG = Images.loadImage("assets\\images\\mill.png");

    // the array of rectangle composing the grid
    private final Shape[][] cells;
    final int linkThickness = 5;
    final int cellRadius = 24;
    private final Color linesColor = Color.BLACK;

    public MerelleBoardLook(int size, GameElement element) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the element parameter.
        // If we create the 3x3 board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/3
        super(size, size, (size - 20) / MerelleBoard.GRIDNBROWS, (size - 20) / MerelleBoard.GRIDNBCOLS, 10, Color.LIGHTSLATEGREY.toString(), element);
        cells = new Shape[MerelleBoard.GRIDNBROWS][MerelleBoard.GRIDNBCOLS];
        Rectangle rectangle;
        Circle circle;

        // Create the background lines.
        int[] linesSize = {6, 4, 2};
        for (int i = 0; i < 2; i++) {
            for (int n : linesSize) {
                addShape(newLine(
                        borderWidth + cellWidth / 2.0 + (n % 3) * cellWidth,
                        borderWidth + cellHeight / 2.0 - linkThickness / 2.0 + cellHeight * (n % 3) + i * n * cellHeight,
                        n * cellWidth,
                        linkThickness
                ));
                addShape(newLine(
                        borderWidth + cellWidth / 2.0 - linkThickness / 2.0 + cellWidth * (n % 3) + i * n * cellWidth,
                        borderWidth + cellHeight / 2.0 + (n % 3) * cellHeight,
                        linkThickness,
                        n * cellWidth
                ));
            }
            addShape(newLine(
                    borderWidth + cellWidth / 2.0 - linkThickness / 2.0 + cellWidth * 3,
                    borderWidth + cellHeight / 2.0 + i * 4 * cellHeight,
                    linkThickness,
                    2 * cellWidth
            ));
            addShape(newLine(
                    borderWidth + cellWidth / 2.0 + i * 4 * cellWidth,
                    borderWidth + cellHeight / 2.0 - linkThickness / 2.0 + cellHeight * 3,
                    2 * cellWidth,
                    linkThickness
            ));
        }


        // Create the circle cells.
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            circle = new Circle(cellRadius);
            circle.setCenterX(borderWidth + cell[0] * cellWidth + cellWidth / 2.0);
            circle.setCenterY(borderWidth + cell[1] * cellHeight + cellHeight / 2.0);
            circle.setFill(Color.BLACK);
            cells[cell[1]][cell[0]] = circle;
            addShape(cells[cell[1]][cell[0]]);
        }
        rectangle = new Rectangle(borderWidth + 3 * cellWidth, borderWidth + 3 * cellHeight, cellWidth, cellHeight);
        rectangle.setFill(new ImagePattern(millIMG));
        cells[3][3] = rectangle;
        addShape(cells[3][3]);
    }

    @Override
    public void onChange() {
        // in a pawn is selected, reachableCells changes. Thus, the look of the board must also changes.
        MerelleBoard board = (MerelleBoard) element;
        boolean[][] reach = board.getReachableCells();
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            if (reach[cell[1]][cell[0]]) {
                cells[cell[1]][cell[0]].setStrokeMiterLimit(10);
                cells[cell[1]][cell[0]].setStrokeType(StrokeType.OUTSIDE);
                cells[cell[1]][cell[0]].setStrokeWidth(3);
                cells[cell[1]][cell[0]].setStroke(Color.RED);

            } else {
                cells[cell[1]][cell[0]].setStrokeWidth(0);
            }
        }
    }

    private Rectangle newLine(double x, double y, double width, double height) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setFill(linesColor);
        return rectangle;
    }
}