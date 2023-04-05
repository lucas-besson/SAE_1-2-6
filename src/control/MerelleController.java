package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.GridElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.View;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MerelleController extends Controller {

    BufferedReader consoleIn;
    boolean firstPlayer;

    public MerelleController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
    }

    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     */
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        update();
        while(! model.isEndStage()) {
            nextPlayer();
            update();
        }
        stopStage();
        endGame();
    }

    public void nextPlayer() {
        // for the first player, the id of the player is already set, so do not compute it
        if (!firstPlayer) {
            model.setNextPlayer();
        }
        else {
            firstPlayer = false;
        }
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MerelleDecider decider = new MerelleDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 3) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                }
                catch(IOException e) {}
            }
        }
    }
    private boolean analyseAndPlay(String line) {
        MerelleStageModel gameStage = (MerelleStageModel) model.getGameStage();
        // get the pawn value from the first char
        int pawnIndex = (int) (line.charAt(0) - '1');
        if ((pawnIndex<0)||(pawnIndex> MerellePawnPot.PAWNS_IN_POT)) return false;

        // get the ccords in the board
        int col = (int) (line.charAt(1) - 'A');
        int row = (int) (line.charAt(2) - '1');


        // Faire une liste en paramettre de MerelleBoard avec toute les combinaison de co possible, sachant que tout les case ne sont pas utilisé
        if (!MerelleBoard.isActiveCell(col, row)) return false;


        // check if the pawn is still in its pot
        GridElement pot = null;
        if (model.getIdPlayer() == 0) {
            pot = gameStage.getBlackPot();
        }
        else {
            pot = gameStage.getRedPot();
        }
        if (pot.isEmptyAt(pawnIndex,0)) return false;
        GameElement pawn = pot.getElement(pawnIndex,0);


        // FIXME : compute valid cells for the chosen pawn
        gameStage.getBoard().setValidCells(pawnIndex+1);
        if (!gameStage.getBoard().canReachCell(row,col)) return false;

        ActionList actions = new ActionList(true);
        GameAction move = new MoveAction(model, pawn, "merelleboard", row, col);
        // add the action to the action list.
        actions.addSingleAction(move);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }
}
