package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class HoleStageFactory extends StageElementsFactory {
    private HoleStageModel stageModel;

    public HoleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (HoleStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        // create the board
        stageModel.setBoard(new HoleBoard(10, 60, stageModel));
        //create the pots
        HolePawnPot blackPot = new HolePawnPot(350,10, stageModel);
        stageModel.setBlackPot(blackPot);
        HolePawnPot redPot = new HolePawnPot(500,10, stageModel);
        stageModel.setRedPot(redPot);

        // create the pawns
        Pawn[] blackPawns = new Pawn[4];
        for(int i=0;i<4;i++) {
            blackPawns[i] = new Pawn(i + 1, Pawn.PAWN_BLACK, stageModel);
        }
        stageModel.setBlackPawns(blackPawns);
        Pawn[] redPawns = new Pawn[4];
        for(int i=0;i<4;i++) {
            redPawns[i] = new Pawn(i + 1, Pawn.PAWN_RED, stageModel);
        }
        stageModel.setRedPawns(redPawns);

        // assign pawns to their pot
        for (int i=0;i<4;i++) {
            blackPot.putElement(blackPawns[i], i,0);
            redPot.putElement(redPawns[i], i,0);
        }
        // create the text
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(10,30);
        text.setLocationType(GameElement.LOCATION_TOPLEFT);
        stageModel.setPlayerName(text);
    }
}
