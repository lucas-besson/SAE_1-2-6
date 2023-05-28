package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.*;
import javafx.scene.input.*;
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

    //    FIXME : adapt this method to the Merelle
    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;

        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D click = new Coord2D(event.getSceneX(), event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(click);
        // for debug, uncomment next instructions to display x,y and elements at that postion
        /*
        System.out.println("click in "+event.getSceneX()+","+event.getSceneY());
        for(GameElement element : list) {
            System.out.println(element);
        }
         */
        MerelleStageModel stageModel = (MerelleStageModel) model.getGameStage();

        if (stageModel.getState() == MerelleStageModel.STATE_SELECTPAWN) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    Pawn pawn = (Pawn) element;
                    // check if color of the pawn corresponds to the current player id
                    if (pawn.getColor() == model.getIdPlayer()) {
                        element.toggleSelected();
                        stageModel.setState(MerelleStageModel.STATE_SELECTDEST);
                        return; // do not allow another element to be selected
                    }
                }
            }
        } else if (stageModel.getState() == MerelleStageModel.STATE_SELECTDEST) {
            // first check if the click is on the current selected pawn. In this case, unselect it
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(MerelleStageModel.STATE_SELECTPAWN);
                    return;
                }
            }
            // secondly, search if the board has been clicked. If not just return
            boolean boardClicked = false;
            for (GameElement element : list) {
                if (element == stageModel.getBoard()) {
                    boardClicked = true;
                    break;
                }
            }
            if (!boardClicked) return;
            // get the board, pot,  and the selected pawn to simplify code in the following
            MerelleBoard board = stageModel.getBoard();
            // by default get black pot
            MerellePawnPot pot = stageModel.getBlackPot();
            // but if it's player2 that plays, get red pot
            if (model.getIdPlayer() == 1) {
                pot = stageModel.getRedPot();
            }
            Pawn pawn = (Pawn) model.getSelected().get(0);

            // thirdly, get the clicked cell in the board
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(click);
            // get the cell in the pot that owns the selected pawn
            int[] from = pot.getElementCell(pawn);
            System.out.println("try to move pawn from pot " + from[0] + "," + from[1] + " to board " + dest[0] + "," + dest[1]);

            board.setValidCells(pawn, ((MerelleStageModel) model.getGameStage()).getStatus());

            // if the destination cell is valid for the selected pawn
            if (board.canReachCell(dest[0], dest[1])) {

                // build the list of actions to do, and pass to the next player when done
                ActionList actions = new ActionList(true);
                // determine the destination point in the root pane
                Coord2D center = lookBoard.getRootPaneLocationForCellCenter(dest[0], dest[1]);
                System.out.println(pawn.getX()+" "+pawn.getY()+" "+center.getX()+" "+center.getY());
                // create an action with a linear move animation, with 10 pixel/frame
                GameAction move = new MoveAction(model, pawn, "merelleboard", dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, center.getX(), center.getY(), 10);
                // add the action to the action list.
                actions.addSingleAction(move);
                stageModel.unselectAll();
                stageModel.setState(MerelleStageModel.STATE_SELECTPAWN);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();
            }

            if (board.millsChecker(model.getIdPlayer())) {
                System.out.println("Logique pour la mill");
            }
        }
    }
}