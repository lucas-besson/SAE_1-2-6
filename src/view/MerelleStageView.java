package view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import model.MerellePawnPot;
import model.MerelleStageModel;

public class MerelleStageView extends GameStageView {

    public static final int BOARD_CELL_WIDTH = 5;
    public static final int BOARD_CELL_HEIGHT = 2;
    public static final int POT_CELL_WIDTH = 4;
    public static final int POT_CELL_HEIGHT = 2;

    public MerelleStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        MerelleStageModel model = (MerelleStageModel)gameStageModel;

        addLook(new MerelleGridLook(BOARD_CELL_WIDTH, BOARD_CELL_HEIGHT, model.getBoard(), -1, true));
        addLook(new PawnPotLook(POT_CELL_WIDTH, POT_CELL_HEIGHT, model.getBlackPot()));
        addLook(new PawnPotLook(POT_CELL_WIDTH, POT_CELL_HEIGHT, model.getRedPot()));

        for(int i = 0; i< MerellePawnPot.PAWNS_IN_POT; i++) {
            addLook(new PawnLook(model.getBlackPawns()[i]));
            addLook(new PawnLook(model.getRedPawns()[i]));
        }
    }
}
