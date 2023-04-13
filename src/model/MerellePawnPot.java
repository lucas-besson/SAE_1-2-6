package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

import java.util.List;

public class MerellePawnPot extends GridElement {
    public static final int PAWNS_IN_POT = 9;
    public MerellePawnPot(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 4x1 grid, named "pawnpot", and in x,y in space
        super("pawnpot", x, y, PAWNS_IN_POT, 1, gameStageModel);
    }
    @Override
    public boolean isEmpty() {
        for (List<GameElement>[] gs : grid){
            for (List<GameElement> g: gs) {
                if (!g.isEmpty()) return false;
            }
        }
        return true;
    }
}