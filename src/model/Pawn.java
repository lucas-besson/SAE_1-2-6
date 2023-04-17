package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.animation.AnimationStep;
import boardifier.view.GridGeometry;

public class Pawn extends GameElement {

    private int number;
    private int color;
    public static int PAWN_BLACK = 0;
    public static int PAWN_RED = 1;

    public Pawn(int number, int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("pawn",50);
        type = ElementTypes.getType("pawn");
        this.number = number;
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public int getColor() {
        return color;
    }

    public int getCol(){
        // Each cells are 5 units wide, the x being stored at the center
        // Return the pawn column starting from 1 
        return (int)(x+2.5)/5;
    }
    public int getRow(){
        // Each cells are 2 units tall, the y being stored at the center
        // Return the pawn row starting from 1 
        return (int)(y+1)/2;
    }
}
