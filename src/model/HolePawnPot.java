package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class HolePawnPot extends GridElement {
    public static final int PAWNS_IN_POT = 9;
    public HolePawnPot(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 4x1 grid, named "pawnpot", and in x,y in space
        super("pawnpot", x, y, PAWNS_IN_POT, 1, gameStageModel);
    }
}