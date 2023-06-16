package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerKey;
import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.GameMode;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;
import view.MerelleView;

/**
 * A basic keystrokes handler.
 * Generally useless for board games, but it can still be used if needed
 */
public class MerelleControllerKey extends ControllerKey implements EventHandler<KeyEvent> {

    public MerelleControllerKey(Model model, View view, Controller control) {
        super(model, view, control);
    }

    @Override
    public void handle(KeyEvent event) {
        if (!model.isCaptureKeyEvent() || !event.getEventType().equals(KeyEvent.KEY_PRESSED))
            return;


        GameMode selectedGameMode = ((MerelleView) view).selectedGameMode;

        if (event.getCode().equals(KeyCode.LEFT) && selectedGameMode.type != GameMode.PVP) {
            MerelleDecider.decreaseAnimationSpeed();
        }
        if (event.getCode().equals(KeyCode.RIGHT) && selectedGameMode.type != GameMode.PVP) {
            MerelleDecider.increaseAnimationSpeed();
        }
        if (event.getCode().equals(KeyCode.SPACE) && selectedGameMode.type == GameMode.AIVAI) {
            if (model.isStageStarted()) model.pauseGame();
            else model.resumeGame();
        }

        if (event.getCode().equals(KeyCode.DIGIT1) && selectedGameMode.type == GameMode.PVP) {
            MerellePawnPot blackPot =  ((MerelleStageModel) model.getGameStage()).getBlackPot();
            MerellePawnPot redPot =  ((MerelleStageModel) model.getGameStage()).getRedPot();
            ActionList actions = new ActionList(true);
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(0,0), "merelleboard", 0, 0));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(0,0), "merelleboard", 4, 4));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(1,0), "merelleboard", 3, 0));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(1,0), "merelleboard", 0, 6));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(2,0), "merelleboard", 0, 3));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(2,0), "merelleboard", 1, 5));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(3,0), "merelleboard", 1, 1));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(3,0), "merelleboard", 5, 1));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(4,0), "merelleboard", 1, 3));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(4,0), "merelleboard", 2, 4));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(5,0), "merelleboard", 3, 1));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(5,0), "merelleboard", 4, 2));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(6,0), "merelleboard", 2, 2));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(6,0), "merelleboard", 6, 6));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(7,0), "merelleboard", 3, 2));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(7,0), "merelleboard", 5, 5));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(8,0), "merelleboard", 2, 3));
            ActionPlayer play = new ActionPlayer(model, control, actions);
            play.start();
        }
        if (event.getCode().equals(KeyCode.DIGIT2) && selectedGameMode.type == GameMode.PVP) {
            MerellePawnPot blackPot =  ((MerelleStageModel) model.getGameStage()).getBlackPot();
            MerellePawnPot redPot =  ((MerelleStageModel) model.getGameStage()).getRedPot();
            ActionList actions = new ActionList(true);
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(0,0), "merelleboard", 0, 0));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(0,0), "merelleboard", 6, 6));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(1,0), "merelleboard", 0, 3));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(1,0), "merelleboard", 6, 3));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(2,0), "merelleboard", 0, 6));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(2,0), "merelleboard", 3, 6));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(3,0), "merelleboard", 1, 1));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(3,0), "merelleboard", 5, 5));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(4,0), "merelleboard", 1, 3));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(4,0), "merelleboard", 3, 5));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(5,0), "merelleboard", 1, 5));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(5,0), "merelleboard", 5, 3));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(6,0), "merelleboard", 2, 2));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(6,0), "merelleboard", 4, 4));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(7,0), "merelleboard", 2, 3));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(7,0), "merelleboard", 3, 0));
            actions.addSingleAction(new MoveAction(model, blackPot.getFirstElement(8,0), "merelleboard", 3, 4));
            actions.addSingleAction(new MoveAction(model, redPot.getFirstElement(8,0), "merelleboard", 3, 1));
            ActionPlayer play = new ActionPlayer(model, control, actions);
            play.start();
        }
        if (event.getCode().equals(KeyCode.DIGIT3) && selectedGameMode.type == GameMode.PVP) {
            MerellePawnPot blackPot =  ((MerelleStageModel) model.getGameStage()).getBlackPot();
            MerellePawnPot redPot =  ((MerelleStageModel) model.getGameStage()).getRedPot();
            ActionList actions = new ActionList(true);
//            System.out.println(((MerelleStageModel) model.getGameStage()).getBoard().getFirstElement(3,0));
            actions.addSingleAction(new RemoveAction(model,((MerelleStageModel) model.getGameStage()).getBoard().getFirstElement(3,0)));
            actions.addSingleAction(new RemoveAction(model,((MerelleStageModel) model.getGameStage()).getBoard().getFirstElement(3,1)));
            actions.addSingleAction(new RemoveAction(model,((MerelleStageModel) model.getGameStage()).getBoard().getFirstElement(6,3)));
            ActionPlayer play = new ActionPlayer(model, control, actions);
            play.start();
        }
    }
}

