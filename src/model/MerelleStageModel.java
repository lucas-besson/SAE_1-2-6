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

    public int getStage(){
        if (blackPot.isEmpty() && redPot.isEmpty()) return 2;
        else return 1;
    }

    private void setupCallbacks() {
        onPutInGrid( (element, gridDest, rowDest, colDest) -> {
            // just check when pawns are put in 3x3 board
            if (gridDest != board) return;
            Pawn p = (Pawn) element;
            if (p.getColor() == 0) {
                blackPawnsToPlay--;
            }
            else {
                redPawnsToPlay--;
            }
            // FIXME : ne dois pas s'arrêter losrque les pot sont vide | Ajouter / utiliser un compteur de pieces sur les joueurs pour les vérif ? (si il y en a un à 2 pièces == fin)
            // if ((blackPawnsToPlay == 0) && (redPawnsToPlay == 0)) {
            //     computePartyResult();
            // }
        });
    }
    // FIXME : plus simple, on pourrait même suppr la méthode : le perdant = selui qui à plus que 2 pièces en mains
    private void computePartyResult() {
        int idWinner = -1;
        // get the empty cell, which should be in 2D at [0,0], [0,2], [1,1], [2,0] or [2,2]
        // i.e. or in 1D at index 0, 2, 4, 6 or 8
        int i = 0;
        int nbBlack = 0;
        int nbRed = 0;
        int countBlack = 0;
        int countRed = 0;
        Pawn p = null;
        int row, col;
        for (i = 0; i < 9; i+=2) {
            if (board.isEmptyAt(i / 3, i % 3)) break;
        }
        // get the 4 adjacent cells (if they exist) starting by the upper one
        row = (i / 3) - 1;
        col = i % 3;
        for (int j = 0; j < 9; j++) {
            // skip invalid cells
            if ((row >= 0) && (row <= 2) && (col >= 0) && (col <= 2)) {
                p = (Pawn) (board.getElement(row, col));
                if (p.getColor() == Pawn.PAWN_BLACK) {
                    nbBlack++;
                    countBlack += p.getNumber();
                } else {
                    nbRed++;
                    countRed += p.getNumber();
                }
            }
            // change row & col to set them at the correct value for the next iteration
            if ((j==0) || (j==2)) {
                row++;
                col--;
            }
            else if (j==1) {
                col += 2;
            }
        }

        System.out.println("nb black: "+nbBlack+", nb red: "+nbRed+", count black: "+countBlack+", count red: "+countRed);
        // decide whose winning
        if (nbBlack < nbRed) {
            idWinner = 0;
        }
        else if (nbBlack > nbRed) {
            idWinner = 1;
        }
        else {
            if (countBlack < countRed) {
                idWinner = 0;
            }
            else if (countBlack > countRed) {
                idWinner = 1;
            }
        }
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new MerelleStageFactory(this);
    }
}
