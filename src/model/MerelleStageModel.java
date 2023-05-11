package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;

public class MerelleStageModel extends GameStageModel {

    private MerelleBoard board;
    private MerellePawnPot blackPot;
    private MerellePawnPot redPot;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;
    private int blackPawnsToPlay;
    private int redPawnsToPlay;

    public MerelleStageModel(String name, Model model) {
        super(name, model);
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
        for (int i = 0; i < blackPawns.length; i++) {
            addElement(blackPawns[i]);
        }
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }

    public void setRedPawns(Pawn[] redPawns) {
        this.redPawns = redPawns;
        for (int i = 0; i < redPawns.length; i++) {
            addElement(redPawns[i]);
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
        onMoveInGrid((element, gridDest, rowDest, colDest)->{
            ((Pawn) element).setInAMill(false);
        });
        onRemoveFromGrid((element, gridDest, rowDest, colDest) -> {
            if (gridDest != board) return;
            Pawn p = (Pawn) element;
            if (p.getColor() == 0) {
                blackPawnsToPlay--;
            } else {
                redPawnsToPlay--;
            }
        });
    }

    /**
     * Checks whether the Merelle stage is ended or not. If the following player doesn't have any move available,
     * the game ends and the actual player wins. If a player has only two pawns remaining, the other player wins.
     * @return true if the stage is ended, false otherwise
     */
    public boolean isEndStage() {
        MerelleStageModel merelleModel = (MerelleStageModel) model.getGameStage();

        // if the folowing player doesn't have any move available, the game end and the actual player win
        if (merelleModel.getBoard().availableMoves(((model.getIdPlayer() == 1) ? 0 : 1), merelleModel.getStatus()) == 0) {
            model.setIdWinner(model.getIdPlayer());
            model.stopStage();
        }
        if (blackPawnsToPlay == 2) {
            model.setIdWinner(1);
            model.stopStage();
        } else if (redPawnsToPlay == 2) {
            model.setIdWinner(0);
            model.stopStage();
        }
        return model.isEndStage();
    }


    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MerelleStageFactory(this);
    }
}
