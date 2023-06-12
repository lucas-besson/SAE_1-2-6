package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.animation.AnimationStep;
import boardifier.view.GridGeometry;

public class Pawn extends GameElement {

    public static int PAWN_BLACK = 0;
    public static int PAWN_RED = 1;
    private final int number;
    private final int color;
    private boolean isInAMill;
    private int col;
    private int row;

    public Pawn(int number, int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("pawn", 50);
        type = ElementTypes.getType("pawn");
        this.number = number;
        this.color = color;
        this.isInAMill = false;
    }

    public int getNumber() {
        return number;
    }

    public int getColor() {
        return color;
    }

    public boolean isInAMill() {
        return isInAMill;
    }

    public void setInAMill(boolean inAMill) {
        isInAMill = inAMill;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public void update(double width, double height, GridGeometry gridGeometry) {

        // if must be animated, move the pawn
        if (animation != null) {

            AnimationStep step = animation.next();
            if (step != null) {
                setLocation(step.getInt(0), step.getInt(1));
            } else {
                animation = null;
            }
        }
    }
}
