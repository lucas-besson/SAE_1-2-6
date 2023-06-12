package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class MerelleStageModel extends GameStageModel {
    public final static int STATE_SELECTPAWN = 1; // the player must select a pawn
    public final static int STATE_SELECTDEST = 2; // the player must select a destination
    public final static int STATE_SELECTMILL = 3; // the player must select a opponent pawn to be removed
    private MerelleBoard board;
    private MerellePawnPot blackPot;
    private MerellePawnPot redPot;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;
    private int blackPawnsToPlay;
    private int redPawnsToPlay;
    private TextElement playerName;

    public MerelleStageModel(String name, Model model) {
        super(name, model);
        state = STATE_SELECTPAWN;
        blackPawnsToPlay = MerellePawnPot.PAWNS_IN_POT;
        redPawnsToPlay = MerellePawnPot.PAWNS_IN_POT;
        setupCallbacks();
    }


    public int getBlackPawnsToPlay() {
        return blackPawnsToPlay;
    }

    public int getRedPawnsToPlay() {
        return redPawnsToPlay;
    }

    public MerelleBoard getBoard() {
        return board;
    }

    public void setBoard(MerelleBoard board) {
        this.board = board;
        addGrid(board);
    }

    public MerellePawnPot getBlackPot() {
        return blackPot;
    }

    public void setBlackPot(MerellePawnPot blackPot) {
        this.blackPot = blackPot;
        addGrid(blackPot);
    }

    public MerellePawnPot getRedPot() {
        return redPot;
    }

    public void setRedPot(MerellePawnPot redPot) {
        this.redPot = redPot;
        addGrid(redPot);
    }

    public Pawn[] getBlackPawns() {
        return blackPawns;
    }

    public void setBlackPawns(Pawn[] blackPawns) {
        this.blackPawns = blackPawns;
        for (Pawn blackPawn : blackPawns) {
            addElement(blackPawn);
        }
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }

    public void setRedPawns(Pawn[] redPawns) {
        this.redPawns = redPawns;
        for (Pawn redPawn : redPawns) {
            addElement(redPawn);
        }
    }

    public int getStatus() {
        if (blackPot.isEmpty() && redPot.isEmpty()) return MerelleGameStatus.MOVING;
        else return MerelleGameStatus.PLACING;
    }

    /**
     * Sets up the callbacks for the board's events.
     * When a pawn is moved within the grid, its 'in a mill' flag is set to false.
     **/

    private void setupCallbacks() {
        onSelectionChange(() -> {
            // get the selected pawn if any
            if (selected.size() == 0) {
                board.resetReachableCells(false);
                return;
            }
            Pawn pawn = (Pawn) selected.get(0);
            // Previously : board.setValidCells(pawn.getNumber());
            board.setValidCells(pawn, this.getStatus());
        });
        onPutInGrid((element, gridDest, rowDest, colDest) -> {
            Pawn p = ((Pawn) element);
            p.setInAMill(false);
//            LOG :
//            System.out.println("x : " + colDest + ", y : " + rowDest);
            p.setCol(colDest);
            p.setRow(rowDest);

        });
        onMoveInGrid((element, gridDest, rowDest, colDest) -> {
            Pawn p = ((Pawn) element);
            p.setInAMill(false);
//            LOG :
//            System.out.println("x : " + colDest + ", y : " + rowDest);
            p.setCol(colDest);
            p.setRow(rowDest);

        });
        onRemoveFromGrid((element, gridDest, rowDest, colDest) -> {
            if (gridDest != board) return;
            Pawn p = (Pawn) element;
            if (p.getColor() == 0) {
                blackPawnsToPlay--;
            } else {
                redPawnsToPlay--;
            }
            isEndStage();
        });
    }

    /**
     * Checks whether the Merelle stage is ended or not. If the following player doesn't have any move available,
     * the game ends and the actual player wins. If a player has only two pawns remaining, the other player wins.
     *
     * @return true if the stage is ended, false otherwise
     */
    public boolean isEndStage() {

        // if the following player doesn't have any move available, the game end and the actual player win
        if (this.getBoard().availableMoves(((model.getIdPlayer() == 1) ? 0 : 1), this.getStatus()) == 0) {
            model.setIdWinner(model.getIdPlayer());
            model.stopGame();
        }
        if (blackPawnsToPlay < 3) {
            model.setIdWinner(1);
            model.stopGame();
        } else if (redPawnsToPlay < 3) {
            model.setIdWinner(0);
            model.stopGame();
        }
        return model.isEndStage();
    }

    public TextElement getPlayerName() {
        return playerName;
    }

    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MerelleStageFactory(this);
    }
}
