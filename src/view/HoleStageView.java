package view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import model.HoleStageModel;

public class HoleStageView extends GameStageView {
    public HoleStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        width = 650;
        height = 450;
    }

    @Override
    public void createLooks() {
        HoleStageModel model = (HoleStageModel)gameStageModel;

        addLook(new HoleBoardLook(320, model.getBoard()));
        addLook(new PawnPotLook(120,420,model.getBlackPot()));
        addLook(new PawnPotLook(120,420,model.getRedPot()));

        for(int i=0;i<4;i++) {
            addLook(new PawnLook(25,model.getBlackPawns()[i]));
            addLook(new PawnLook(25, model.getRedPawns()[i]));
        }

        addLook(new TextLook(24, "0x000000", model.getPlayerName()));
    }
}
