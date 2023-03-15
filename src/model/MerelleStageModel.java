package model;

import boardifier.model.*;

public class HoleStageModel extends GameStageModel {

    private HoleBoard board;
    private HolePawnPot blackPot;
    private HolePawnPot redPot;
    private Pawn[] blackPawns;
    private Pawn[] redPawns;
    private int blackPawnsToPlay;
    private int redPawnsToPlay;

    public HoleStageModel(String name, Model model) {
        super(name, model);
        blackPawnsToPlay = 4;
        redPawnsToPlay = 4;
        setupCallbacks();
    }

    public HoleBoard getBoard() {
        return board;
    }

    public HolePawnPot getBlackPot() {
        return blackPot;
    }

    public HolePawnPot getRedPot() {
        return redPot;
    }

    public Pawn[] getBlackPawns() {
        return blackPawns;
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }

    /*
    TO FULFILL:
        - create setters for all attributes. NB: in setters, do not forget to add elements to the stage (see addGrid() & addElement())
     */

    private void setupCallbacks() {

    /*
    TO FULFILL:
        - setup the onPutInGrid callback in order to decrease the number of pawns to play, and eventually to get the party result
     */
    }

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
        for (int j = 0; j < 4; j++) {
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
        return new HoleStageFactory(this);
    }
}
