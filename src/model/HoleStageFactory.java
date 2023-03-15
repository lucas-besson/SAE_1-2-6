package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;

public class HoleStageFactory extends StageElementsFactory {
    private HoleStageModel stageModel;

    public HoleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (HoleStageModel) gameStageModel;
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
