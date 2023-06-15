package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

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
        stageModel.setBoard(new MerelleBoard(190, 10, stageModel));
        //create the pots
        MerellePawnPot blackPot = new MerellePawnPot(52, 13, stageModel);
        MerellePawnPot redPot = new MerellePawnPot(942, 13, stageModel);

        // create the pawns
        Pawn[] blackPawns = new Pawn[MerellePawnPot.PAWNS_IN_POT];
        for (int i = 0; i < blackPawns.length; i++) {
            blackPawns[i] = new Pawn(i + 1, Pawn.PAWN_BLACK, stageModel);
        }
        Pawn[] redPawns = new Pawn[MerellePawnPot.PAWNS_IN_POT];
        for (int i = 0; i < redPawns.length; i++) {
            redPawns[i] = new Pawn(i + 1, Pawn.PAWN_RED, stageModel);
        }

        // assign pawns to their pot : they will be put at the center
        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            blackPot.putElement(blackPawns[i], i, 0);
            redPot.putElement(redPawns[i], i, 0);
        }
        stageModel.setBlackPawns(blackPawns);
        stageModel.setBlackPot(blackPot);
        stageModel.setRedPawns(redPawns);
        stageModel.setRedPot(redPot);


        // create the text
//        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
//        text.setLocation(10,30);
//        text.setLocationType(GameElement.LOCATION_TOPLEFT);
//        stageModel.setPlayerName(text);
    }
}
