package model;

import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

public class MerellePawnPot extends GridElement {
    public MerellePawnPot(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 4x1 grid, named "pawnpot", and in x,y in space
        super("pawnpot", x, y, 4, 1, gameStageModel);
    }
}
