package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Pawn;

public class PawnLook extends ElementLook {

    private Circle circle;
    public PawnLook(int radius, GameElement element) {
        super(element);
        Pawn pawn = (Pawn)element; // unless you did somthing stupid in HoleStageView, it IS really a Pawn instance !
        circle = new Circle();
        circle.setRadius(radius);
        if (pawn.getColor() == Pawn.PAWN_BLACK) {
            circle.setFill(Color.BLACK);
        }
        else {
            circle.setFill(Color.RED);
        }
        circle.setCenterX(radius);
        circle.setCenterY(radius);
        // add the circle to the look
        addShape(circle);
        // to fulfill ...
    }
    @Override
    public void onSelectionChange() {
        // to fulfill ...
    }
    @Override
    public void onChange() {
//        FIXME
    }
}
