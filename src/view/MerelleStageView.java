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
        width = 1080;
        height = 720;
    }

    @Override
    public void createLooks() {
        MerelleStageModel model = (MerelleStageModel) gameStageModel;
        addLook(new MerelleBoardLook(699, model.getBoard()));
        addLook(new PawnPotLook(86, 694, model.getBlackPot()));
        addLook(new PawnPotLook(86, 694, model.getRedPot()));

        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            addLook(new PawnLook(35, model.getBlackPawns()[i]));
            addLook(new PawnLook(35, model.getRedPawns()[i]));
        }
    }
}
