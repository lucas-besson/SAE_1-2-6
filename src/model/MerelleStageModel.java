package model;

import boardifier.model.*;

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
        for(int i=0;i<blackPawns.length;i++) {
            addElement(blackPawns[i]);
        }
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }
    public void setRedPawns(Pawn[] redPawns) {
        this.redPawns = redPawns;
        for(int i=0;i<redPawns.length;i++) {
            addElement(redPawns[i]);
        }
    }

    public int getStatus(){
        if (blackPot.isEmpty() && redPot.isEmpty()) return MerelleGameStatus.MOVING;
        else return MerelleGameStatus.PLACING;
    }

    private void setupCallbacks() {
        onMoveInGrid((element, gridDest, rowDest, colDest)->{
            ((Pawn) element).setInAMill(false); // FIXME : ne marche pas ?
        });
        onRemoveFromGrid((element, gridDest, rowDest, colDest)->{
            if (gridDest != board) return;
            Pawn p = (Pawn) element;
            if (p.getColor() == 0) {
                blackPawnsToPlay--;
            }
            else {
                redPawnsToPlay--;
            }
            if (blackPawnsToPlay == 2) {
                model.setIdWinner(1);
                model.stopStage();
            }
            else if (redPawnsToPlay == 2){
                model.setIdWinner(0);
                model.stopStage();
            };
        });
    }


    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MerelleStageFactory(this);
    }
}
