package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import model.MerelleStageModel;
import view.MerelleBoardLook;
import view.MerelleView;

public class MerelleController extends Controller {

    public MerelleController(Model model, View view) {
        super(model, view);
        setControlKey(new MerelleControllerKey(model, view, this));
        setControlMouse(new MerelleControllerMouse(model, view, this));
        setControlAction(new MerelleControllerAction(model, view, this));
    }

    /**
     * Changes the current player to the next player in the game.
     * If the current player is a computer, then the next player is automatically set by the model.
     * Otherwise, the current player is asked to input their move through the console.
     * If the last move made by the player created a new mill, the player is asked to remove an opposing pawn.
     */
    public void nextPlayer() {


        MerelleStageModel stageModel = (MerelleStageModel) model.getGameStage();

        // get the current player
        Player p = model.getCurrentPlayer();

        // If the last move introduce a mill, the same player as last move play again to remove an opponent pawn
        if (stageModel.getBoard().millsChecker(model.getIdPlayer()) && p.getType() == Player.HUMAN) {
            stageModel.setState(MerelleStageModel.STATE_SELECTMILL);
            ((MerelleView) view).millAlert().show();
        } else {
            if (((MerelleStageModel) model.getGameStage()).isEndStage()) return;
            model.setNextPlayer();
        }
        // get the new player
        p = model.getCurrentPlayer();
        ((MerelleBoardLook) view.getGameStageView().getElementLook(stageModel.getBoard())).setMillCell(model.getIdPlayer());


        if (p.getType() == Player.COMPUTER) {
            System.out.println(p.getName() + " PLAYS");
            Decider decider;
            if (p.getName().equals("computer") || p.getName().equals("computer1"))
                decider = new IntelligentDecider(model, this);
            else decider = new BasicDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
    }

    @Override
    public void startGame() throws GameException {
        // Let the previous winner restart the next game.
        int previousWinner = model.getIdWinner();
        super.startGame();
        if (previousWinner != -1) model.setIdPlayer(previousWinner);

        // Make sure the AI will make their first move by calling nextPlayer method.
        // To avoid perturbing the player order, we set the next player once, as nextPlayer() will also do it once -> 1 + 1 % 2 = 0
        model.setNextPlayer();
        nextPlayer();
    }
}
