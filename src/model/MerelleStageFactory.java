package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

public class MerelleStageFactory extends StageElementsFactory {
    private MerelleStageModel stageModel;

    public MerelleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (MerelleStageModel) gameStageModel;
    }

    @Override
    public void setup() {

        /*
        TO FULFILL:
            - create the board, pots, pawns and set them in the stage model
            - assign the pawns to their cells in the pots
         */
    }
}
