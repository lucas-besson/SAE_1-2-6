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
        for (int i = 0; i < GRIDNBROWS; i++) {
            for (int j = 0; j < GRIDNBCOLS; j++) {
                if (!isEmptyAt(i,j)){
                    Pawn pawn = (Pawn) getElement(i,j);
                    if (pawn.getNumber() == number && pawn.getColor() == color) {
                        return pawn;
                    }
                }
            }
        }
        // for (List<GameElement>[] gss : grid){
        //     for (List<GameElement> gs: gss) {
        //         for (GameElement g : gs) {
        //             Pawn pawn = (Pawn) g;
        //             if (pawn.getNumber() == number && pawn.getColor() == color) {
        //                 return pawn; // FIXME ne retourne pas le bon object : null | comment marche grid
        //             }
        //         }
        //     }
        // }
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
                    // Why does the getElement(i,j) {grid[i][j]} return a List<GameElement> ???
                    if (getElements(i, j).size() == 0) {
                        lst.add(new Point(j,i));
                    }
                }
            }
            return lst;
        }
        // Second stage of the game : the cells have to be empty and within 1 cells around the initial pawn position
        else {
            int x = pawn.getCol() - 1;
            int y = pawn.getRow() - 1;

            int jumpX = 0;
            int jumpY = 0;
            
            if ((x == 0 || x == 6) && (y == 0 || y == 6)) {
                jumpX = 3;
                jumpY = 3;
            }
            else if ((x == 1 || x == 5) && (y == 1 || y == 5)) {
                jumpX = 2;
                jumpY = 2;
            }
            else if ((x == 0 || x == 6 ) && y == 3){
                jumpX = 1;
                jumpY = 3;
            }
            else if ((y == 0 || y == 6 ) && x == 3){
                jumpX = 3;
                jumpY = 1;
            }
            else if ((x == 1 || x == 5) && y == 3) {
                jumpX = 1;
                jumpY = 2;
            }
            else if ((y == 1 || y == 5) && x == 3) {
                jumpX = 2;
                jumpY = 1;
            }
            else {
                jumpX = 1;
                jumpY = 1;
            }
            
            // Look arround the pawn
            for (int offsetY = 0-jumpY; offsetY <= 2*jumpY; offsetY += jumpY){
                for (int offsetX = 0-jumpX; offsetX <= 2*jumpX; offsetX += jumpX){
                    if ((offsetY==0 && offsetX==0) || x+offsetX < 0 || y+offsetY < 0 || x+offsetX >= GRIDNBCOLS || y+offsetY >= GRIDNBROWS) continue; // Do nothing if the cell is the initial one or unreachable
                    
                    List<GameElement> elements = getElements(y + offsetY, x + offsetX);
                    // if the cell is empty 
                    if (elements.size() == 0) {
                        lst.add(new Point(x+offsetX,y+offsetY));
                    }
                }
            }
            

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
