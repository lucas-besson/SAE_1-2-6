package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.Coord2D;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.*;

import java.util.List;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class MerelleControllerMouse extends ControllerMouse implements EventHandler<MouseEvent> {

    public MerelleControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
    }

    @Override
    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D click = new Coord2D(event.getSceneX(), event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(click);
        // for debug, uncomment next instructions to display x,y and elements at that postion

        MerelleStageModel stageModel = (MerelleStageModel) model.getGameStage();
        MerellePawnPot pot = ((model.getIdPlayer() == 0) ? stageModel.getBlackPot() : stageModel.getRedPot());
        MerelleBoard board = stageModel.getBoard();

        if (stageModel.getState() == MerelleStageModel.STATE_SELECTPAWN) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    Pawn pawn = (Pawn) element;
                    // check if color of the pawn corresponds to the current player id
                    if (pawn.getColor() == model.getIdPlayer() && ((stageModel.getStatus() == MerelleGameStatus.PLACING && pot.contains(pawn)) || stageModel.getStatus() == MerelleGameStatus.MOVING)) {
                        element.toggleSelected();
                        stageModel.setState(MerelleStageModel.STATE_SELECTDEST);
                        return; // do not allow another element to be selected

                    }
                }
            }
        } else if (stageModel.getState() == MerelleStageModel.STATE_SELECTDEST) {
            // If the pawn is selected again, the stage state is changed
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(MerelleStageModel.STATE_SELECTPAWN);
                    return;
                }
            }
            // If the board has been clicked
            boolean boardClicked = false;
            for (GameElement element : list) {
                if (element == stageModel.getBoard()) {
                    boardClicked = true;
                    break;
                }
            }
            if (!boardClicked) return;

            Pawn pawn = (Pawn) model.getSelected().get(0);
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(click);


            board.setValidCells(pawn, ((MerelleStageModel) model.getGameStage()).getStatus());

            // if the destination cell is valid for the selected pawn
            if (board.canReachCell(dest[0], dest[1])) {

                // build the list of actions to do, and pass to the next player when done
                ActionList actions = new ActionList(true);
                // determine the destination point in the root pane
                Coord2D center = lookBoard.getRootPaneLocationForCellCenter(dest[0], dest[1]);
                // create an action with a linear move animation, with 10 pixel/frame
                GameAction move = new MoveAction(model, pawn, "merelleboard", dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, center.getX(), center.getY(), 15);
                // add the action to the action list.
                actions.addSingleAction(move);
                stageModel.unselectAll();
                stageModel.setState(MerelleStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();
            }

        } else if (stageModel.getState() == MerelleStageModel.STATE_SELECTMILL) {

            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    Pawn pawn = (Pawn) element;
                    // check if color of the pawn corresponds to the opponent player
                    if (pawn.getColor() != model.getIdPlayer() && board.contains(pawn)) {
                        element.toggleSelected();
                    }
                }
            }
            if (model.getSelected().isEmpty()) return;
            boolean boardClicked = false;
            for (GameElement element : list) {
                if (element == stageModel.getBoard()) {
                    boardClicked = true;
                    break;
                }
            }
            if (!boardClicked) return;

            Pawn pawn = (Pawn) model.getSelected().get(0);
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(click);

            board.setValidMillCells(model.getIdPlayer());

            if (board.canReachCell(dest[0], dest[1])) {
                ActionList actions = new ActionList(true);
                GameAction deleteAction = new RemoveAction(model, pawn);
                actions.addSingleAction(deleteAction);
                stageModel.unselectAll();
                stageModel.setState(MerelleStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();
            }

        }
    }
}