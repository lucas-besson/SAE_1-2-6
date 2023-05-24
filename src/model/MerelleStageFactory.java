package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class MerelleStageFactory extends StageElementsFactory {
    private final MerelleStageModel stageModel;

    public MerelleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MerelleStageModel) gameStageModel;
    }

    @Override
    public void setup() {

//         TODO creat constant for X position


        // create the board
        stageModel.setBoard(new MerelleBoard(0, 0, stageModel));
        //create the pots
        MerellePawnPot blackPot = new MerellePawnPot(400, 0, stageModel);
        stageModel.setBlackPot(blackPot);
        MerellePawnPot redPot = new MerellePawnPot(500, 0, stageModel);
        stageModel.setRedPot(redPot);

        // create the pawns
        Pawn[] blackPawns = new Pawn[MerellePawnPot.PAWNS_IN_POT];
        for (int i = 0; i < blackPawns.length; i++) {
            blackPawns[i] = new Pawn(i + 1, Pawn.PAWN_BLACK, stageModel);
        }
        stageModel.setBlackPawns(blackPawns);
        Pawn[] redPawns = new Pawn[MerellePawnPot.PAWNS_IN_POT];
        for (int i = 0; i < redPawns.length; i++) {
            redPawns[i] = new Pawn(i + 1, Pawn.PAWN_RED, stageModel);
        }
        stageModel.setRedPawns(redPawns);

        // assign pawns to their pot : they will be put at the center
        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            blackPot.putElement(blackPawns[i], i, 0);
            redPot.putElement(redPawns[i], i, 0);
        }

        // create the text
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(10,30);
        text.setLocationType(GameElement.LOCATION_TOPLEFT);
        stageModel.setPlayerName(text);
    }
}
