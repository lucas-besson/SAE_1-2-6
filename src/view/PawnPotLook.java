package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class PawnPotLook extends GridLook {

    // the array of rectangle composing the grid
    private Rectangle[] cells;

    public PawnPotLook(int width, int height, GameElement element) {
        super(width, height, (width-20), (width-20), 10, "0X000000", element);
        cells = new Rectangle[4];
        // create the rectangles.
        for(int i=0;i<4;i++) {
            cells[i] = new Rectangle(cellWidth, cellHeight, Color.WHITE);
            cells[i].setStrokeWidth(3);
            cells[i].setStrokeMiterLimit(10);
            cells[i].setStrokeType(StrokeType.CENTERED);
            cells[i].setStroke(Color.valueOf("0x333333"));
            cells[i].setX(borderWidth);
            cells[i].setY(i*cellHeight+borderWidth);
            addShape(cells[i]);
        }
    }

    @Override
    public void onChange() {
    }
}
