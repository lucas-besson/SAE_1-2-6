package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;
import view.PawnPotLook;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class MerelleBoard extends GridElement {
    protected static int[][] ACTIVCELL = {
        {0,0},{0,6},{6,0},{6,6},{0,3},{3,0},{3,6},{6,3},
        {1,1},{1,5},{5,1},{5,5},{1,3},{3,1},{3,5},{5,3},
        {2,2},{2,4},{4,2},{4,4},{3,2},{2,3},{3,4},{4,3}
    };
    public static boolean isActiveCell(int x, int y) {
        for (int[] i : MerelleBoard.ACTIVCELL) {
            if (x == i[0] && y == i[1]){
                return true;
            }
        }
        return false;
    }
    
    private final static int GRIDNBROWS = 7;
    private final static int GRIDNBCOLS = 7;
    public MerelleBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a GRIDNBROWS x GRIDNBCOLS grid, named "merelleboard", and in x,y in space
        super("merelleboard", x, y, GRIDNBROWS , GRIDNBCOLS, gameStageModel);
        resetReachableCells(false);
    }
    // Method for the second part of the game : get the pawn of number `number` from the player of the color `color`
    public GameElement getPawn(int number, int color){
        for (List<GameElement>[] gss : grid){
            for (List<GameElement> gs: gss) {
                for (GameElement g : gs) {
                    Pawn pawn = (Pawn) g;
                    if (pawn.getNumber() == number && pawn.getColor() == color) {
                        return pawn;
                    }
                }
            }
        }
        return null;
    }
    //FIXME
    public void setValidCells(Pawn pawn, int gameStage) {
        resetReachableCells(false);
        List<Point> valid = computeValidCells(pawn,gameStage);
        if (valid != null) {
            for(Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
    }
    public List<Point> computeValidCells(Pawn pawn, int gameStage) {
        List<Point> lst = new ArrayList<>();
        Pawn p = null;

        // First stage of the game : all empty cells are valid
        if (gameStage == 1) {
            for (int i = 0; i < GRIDNBROWS; i++) {
                for (int j = 0; j < GRIDNBCOLS; j++) {
                    // FIXME : n'arrive pas à faire la comparaison ? à quoi ressemble la grid quand elle est vide ? que retourne réelement getElement ?
                    if ((Pawn) getElements(i, j) == null) {
                        lst.add(new Point(j,i));
                    }
                }
            }
            return lst;
        }
        // Second stage of the game : the cells have to be empty and within 1 cells around the initial pawn position
        else {
            return lst;
        }
        




        // // if the grid is empty, is it the first turn and thus, all cells are valid
        // if (isEmpty()) {
        //     // i are rows
        //     for(int i = 0; i < GRIDNBROWS; i++) {
        //         // j are cols
        //         for (int j = 0; j < GRIDNBCOLS; j++) {
        //             // cols is in x direction and rows are in y direction, so create a point in (j,i)
        //             lst.add(new Point(j,i));
        //         }
        //     }
        //     return lst;
        // }
        // // else, take each empty cell and check if it is valid
        // // FIXME : C'est ici que la logique / rêgle du jeu actuellement celle du Hole
        // for(int i=0;i<3;i++) {
        //     for(int j=0;j<3;j++) {
        //         if (isEmptyAt(i,j)) {
        //             // check adjacence in row-1
        //             if (i-1 >= 0) {
        //                 if (j-1>=0) {
        //                     p = (Pawn)getElement(i-1,j-1);

        //                     // check if same parity
        //                     if ((p != null) && ( p.getNumber()%2 == number%2)) {
        //                         lst.add(new Point(j,i));
        //                         continue; // go to the next point
        //                     }
        //                 }
        //                 p = (Pawn)getElement(i-1,j);
        //                 // check if different parity
        //                 if ((p != null) && ( p.getNumber()%2 != number%2)) {
        //                     lst.add(new Point(j,i));
        //                     continue; // go to the next point
        //                 }
        //                 if (j+1<=2) {
        //                     p = (Pawn)getElement(i-1,j+1);
        //                     // check if same parity
        //                     if ((p != null) && ( p.getNumber()%2 == number%2)) {
        //                         lst.add(new Point(j,i));
        //                         continue; // go to the next point
        //                     }
        //                 }
        //             }
        //             // check adjacence in row+1
        //             if (i+1 <= 2) {
        //                 if (j-1>=0) {
        //                     p = (Pawn)getElement(i+1,j-1);
        //                     // check if same parity
        //                     if ((p != null) && ( p.getNumber()%2 == number%2)) {
        //                         lst.add(new Point(j,i));
        //                         continue; // go to the next point
        //                     }
        //                 }
        //                 p = (Pawn)getElement(i+1,j);
        //                 // check if different parity
        //                 if ((p != null) && ( p.getNumber()%2 != number%2)) {
        //                     lst.add(new Point(j,i));
        //                     continue; // go to the next point
        //                 }
        //                 if (j+1<=2) {
        //                     p = (Pawn)getElement(i+1,j+1);
        //                     // check if same parity
        //                     if ((p != null) && ( p.getNumber()%2 == number%2)) {
        //                         lst.add(new Point(j,i));
        //                         continue; // go to the next point
        //                     }
        //                 }
        //             }
        //             // check adjacence in row
        //             if (j-1>=0) {
        //                 p = (Pawn)getElement(i,j-1);
        //                 // check if different parity
        //                 if ((p != null) && ( p.getNumber()%2 != number%2)) {
        //                     lst.add(new Point(j,i));
        //                     continue; // go to the next point
        //                 }
        //             }
        //             if (j+1<=2) {
        //                 p = (Pawn)getElement(i,j+1);
        //                 // check if different parity
        //                 if ((p != null) && ( p.getNumber()%2 != number%2)) {
        //                     lst.add(new Point(j,i));
        //                     continue; // go to the next point
        //                 }
        //             }
        //         }
        //     }
        // }
        // return lst;
    }

}
