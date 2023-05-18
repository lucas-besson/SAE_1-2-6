package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import model.HoleStageModel;

public class HoleController extends Controller {

    public HoleController(Model model, View view) {
        super(model, view);
        setControlKey(new MerelleControllerKey(model, view, this));
        setControlMouse(new MerelleControllerMouse(model, view, this));
        setControlAction (new MerelleControllerAction(model, view, this));
    }

    public void nextPlayer() {
        // use the default method to compute next player
        model.setNextPlayer();
        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        HoleStageModel stageModel = (HoleStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            HoleDecider decider = new HoleDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
    }
}
