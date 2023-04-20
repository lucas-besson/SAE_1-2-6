package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GridElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import boardifier.view.View;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

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
        while (!model.isEndStage()) {
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
        } else {
            firstPlayer = false;
        }
        // get the new player
        Player p = model.getCurrentPlayer();

        // If the actual player doesn't have any available moove, the game end
        if (((MerelleStageModel) model.getGameStage()).getBoard().availableMoove(model.getIdPlayer(), ((MerelleStageModel) model.getGameStage()).getStage()) == 0) {
            model.setIdWinner(((model.getIdPlayer() == 1) ? 0 : 1));
            endGame();
            return;
        }

        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MerelleDecider decider = new MerelleDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    ok = checkLineAndPlay(p, ok, line);
                } catch (IOException e) {
                }
            }
            // If the last move have created a new mill
            if (((MerelleStageModel) model.getGameStage()).getBoard().millsChecker(model.getIdPlayer())) {
                // TODO : Update avec un afichage du board
                System.out.println("You have just formed a mill, and must take an opposing pawn.");
                ok = false;
                while (!ok) {
                    System.out.print(p.getName() + " > ");
                    try {
                        String line = consoleIn.readLine();
                        ok = checkLineAndPlay(p, ok, line);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private boolean checkLineAndPlay(Player p, boolean ok, String line) {
        if (Objects.equals(line, "stop")) {
            System.out.println("The game was stopped by " + p.getName());
            endGame();
            System.exit(0);
        }
        if (line.length() == 1) {
            ok = millAnalyseAndPlay(line);
        }
        if (!ok) {
            System.out.println("incorrect instruction. retry !");
        }
        return ok;
    }

    private boolean analyseAndPlay(String line) {
        MerelleStageModel gameStage = (MerelleStageModel) model.getGameStage();
        // get the pawn value from the first char
        int pawnIndex = (int) (line.charAt(0) - '1');
        if ((pawnIndex < 0) || (pawnIndex > MerellePawnPot.PAWNS_IN_POT)) return false;

        // get the ccords in the board
        int col = (int) (Character.toUpperCase(line.charAt(1)) - 'A');
        int row = (int) (line.charAt(2) - '1');

        // Check if the coordinates are playable
        if (!MerelleBoard.isActiveCell(col, row)) return false;

        // look for the right pot
        int color = model.getIdPlayer();
        GridElement pot = null;
        if (color == 0) {
            pot = gameStage.getBlackPot();
        } else {
            pot = gameStage.getRedPot();
        }

        // Collect the pawn from the correct place depending on the game stage
        Pawn pawn = null;
        if (gameStage.getStage() == MerelleGameStatus.PLACEMENT) {
            // first part of the game : the player have to empty the pot
            if (pot.isEmptyAt(pawnIndex, 0)) return false;
            pawn = (Pawn) pot.getElement(pawnIndex, 0);
            gameStage.getBoard().setValidCells(pawn, gameStage.getStage());
        } else {
            // second part of the game : the player play with the pawns in the board
            pawn = (Pawn) gameStage.getBoard().getPawn(pawnIndex + 1, color);
            if (pawn == null) return false;
            gameStage.getBoard().setValidCells(pawn, gameStage.getStage());
        }

        // See if the move is possible
        if (!gameStage.getBoard().canReachCell(row, col)) return false;


        ActionList actions = new ActionList(true);
        GameAction move = new MoveAction(model, pawn, "merelleboard", row, col);
        // add the action to the action list.
        actions.addSingleAction(move);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }

    private boolean millAnalyseAndPlay(String line) {
        MerelleStageModel gameStage = (MerelleStageModel) model.getGameStage();

        // get the pawn value from the first char
        int pawnIndex = (int) (line.charAt(0) - '1');
        if ((pawnIndex < 0) || (pawnIndex > MerellePawnPot.PAWNS_IN_POT)) return false;

        int color = model.getIdPlayer();

        // Recovery of the pawn supposed to be deleted.
        Pawn pawn = (Pawn) gameStage.getBoard().getPawn(pawnIndex + 1, ((color == 0) ? 1 : 0));
        if (pawn == null) return false;

        // See if the pawn can be deleted 
        gameStage.getBoard().setValidMillCells(color);
        if (!gameStage.getBoard().canReachCell(pawn.getRow() - 1, pawn.getCol() - 1)) return false;

        ActionList actions = new ActionList(true);
        GameAction delete = new RemoveAction(model, pawn);
        // add the action to the action list.
        actions.addSingleAction(delete);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        // FIXME : update not working after the pawn has been deleted

        return true;
    }
}
