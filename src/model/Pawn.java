package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import view.MerelleStageView;

public class Pawn extends GameElement {

    public static int PAWN_BLACK = 0;
    public static int PAWN_RED = 1;
    private final int number;
    private final int color;
    private boolean isInAMill;

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

    public int getCol() {
        // Each cells are 5 units wide, the x being stored at the center
        // Return the pawn column starting from 1
        return (int)(x + MerelleStageView.BOARD_CELL_WIDTH / 2.0) / MerelleStageView.BOARD_CELL_WIDTH;
    }

    public int getRow() {
        // Each cells are 2 units tall, the y being stored at the center
        // Return the pawn row starting from 1
        return (int)(y + MerelleStageView.BOARD_CELL_HEIGHT / 2.0) / MerelleStageView.BOARD_CELL_HEIGHT;
    }
}
