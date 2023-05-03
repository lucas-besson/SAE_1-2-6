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
import model.MerelleGameStatus;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;

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
        while (!((MerelleStageModel) model.getGameStage()).isEndStage()) {
            nextPlayer();
            update();
        }
        stopStage();
        endGame();
    }

    /**
     * Changes the current player to the next player in the game.
     * If the current player is a computer player, then the next player is automatically set
     * by the model. Otherwise, prompts the current player to input their move through the console.
     * If the last move made by the player created a new mill, prompts the player to remove an opposing pawn.
     * @throws  if the game is over and there is no winner.
     */
    
    public void nextPlayer() {
        // for the first player, the id of the player is already set, so do not compute it
        if (!firstPlayer) {
            model.setNextPlayer();
        } else {
            firstPlayer = false;
        }
        // get the new player
        Player p = model.getCurrentPlayer();

        MerelleStageModel merelleModel = (MerelleStageModel) model.getGameStage();

        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            MerelleDecider decider = new MerelleDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.equalsIgnoreCase("stop")) {
                        endGame();
                        System.exit(100);
                    }
                    if (line.length() == 3) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException e) {
                }
            }
            // If the last move have created a new mill
            if (merelleModel.getBoard().millsChecker(model.getIdPlayer())) {
                update();
                System.out.println(p.getName() + ", you have just formed a mill, and must take an opposing pawn.");
                ok = false;
                while (!ok) {
                    System.out.print(p.getName() + " > ");
                    try {
                        String line = consoleIn.readLine();
                        if (line.equalsIgnoreCase("stop")) {
                            endGame();
                            System.exit(100);
                        }
                        if (line.length() == 1) {
                            ok = millAnalyseAndPlay(line);
                        }
                        if (!ok) {
                            System.out.println("incorrect instruction. retry !");
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public boolean AccesAnalyseAndPlay(String line) {
        return analyseAndPlay(line);
    }
    /**
     * Analyzes and processes a player's move based on the given input line.
     * @param line the input line from the player
     * @return true if the move is valid and will be processed, false otherwise
     */
    private boolean analyseAndPlay(String line) {
        MerelleStageModel gameStage = (MerelleStageModel) model.getGameStage();

        // get the pawn value from the first char
        int pawnIndex = (int) (line.charAt(0) - '1');
        if ((pawnIndex < 0) || (pawnIndex > MerellePawnPot.PAWNS_IN_POT)) return false;

        // get the ccords in the board
        int col = (int) (Character.toUpperCase(line.charAt(1)) - 'A');
        int row = (int) (line.charAt(2) - '1');

        // check if the coordinates are playable
        if (!MerelleBoard.isActiveCell(col, row)) return false;

        int color = model.getIdPlayer();
        
        // collect the pawn from the correct place depending on the game stage
        Pawn pawn = null;
        // first part of the game : the player have to empty the pot
        if (gameStage.getStatus() == MerelleGameStatus.PLACING) {

            // look for the right pot
            GridElement pot = null;
            if (color == 0) pot = gameStage.getBlackPot();
            else pot = gameStage.getRedPot();
            
            if (pot.isEmptyAt(pawnIndex, 0)) return false;
            pawn = (Pawn) pot.getElement(pawnIndex, 0);
            gameStage.getBoard().setValidCells(pawn, gameStage.getStatus());

        } else {
            // second part of the game : the player play with the pawns in the board
            pawn = (Pawn) gameStage.getBoard().getPawn(pawnIndex + 1, color);
            if (pawn == null) return false;
            gameStage.getBoard().setValidCells(pawn, gameStage.getStatus());
        }

        // see if the move is possible
        if (!gameStage.getBoard().canReachCell(row, col)) return false;

        // the action is possible and will be processed
        ActionList actions = new ActionList(true);
        GameAction move = new MoveAction(model, pawn, "merelleboard", row, col);
        actions.addSingleAction(move);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }

    /**
     * Analyzes and plays the move for deleting a pawn in a mill.
     * @param line the input string containing the information about the pawn to be deleted
     * @return true if the move was successful, false otherwise
     */
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

        // the action is possible and will be processed
        ActionList actions = new ActionList(true);
        GameAction delete = new RemoveAction(model, pawn);
        actions.addSingleAction(delete);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }
}
