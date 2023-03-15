package view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import model.MerelleStageModel;

public class HoleStageView extends GameStageView {
    public HoleStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        MerelleStageModel model = (MerelleStageModel)gameStageModel;

        /*
        TO FULFILL:
            using the model of the board, pots and pawns
            - create & add the look of the main board using cells of size 4x2
            - create & add the look of the two pots using cells of size 4x2
            - crate & add the look of the 8 pawns
         */
    }
}
