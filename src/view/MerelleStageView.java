package view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import model.MerellePawnPot;
import model.MerelleStageModel;

public class MerelleStageView extends GameStageView {
    public MerelleStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        MerelleStageModel model = (MerelleStageModel)gameStageModel;

        addLook(new MerelleGridLook(5, 2, model.getBoard(), -1, true));
        addLook(new PawnPotLook(4,2, model.getBlackPot()));
        addLook(new PawnPotLook(4, 2, model.getRedPot()));

        for(int i = 0; i< MerellePawnPot.PAWNS_IN_POT; i++) {
            addLook(new PawnLook(model.getBlackPawns()[i]));
            addLook(new PawnLook(model.getRedPawns()[i]));
        }
    }
}
