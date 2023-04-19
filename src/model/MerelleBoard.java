package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;
import view.PawnPotLook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.*;

public class MerelleBoard extends GridElement {
    protected static int[][] ACTIVCELL = {
        {0,0},{0,3},{0,6},
        {1,1},{1,3},{1,5},
        {2,2},{2,3},{2,4},
        {3,0},{3,1},{3,2},{3,4},{3,5},{3,6},
        {4,2},{4,3},{4,4},
        {5,1},{5,3},{5,5},
        {6,0},{6,3},{6,6}
    };
    public static final int[][][] mills = {
            // Vertical mills
            {{0,0},{0,3},{0,6}},
            {{1,1},{1,3},{1,5}},
            {{2,2},{2,3},{2,4}},
            {{3,0},{3,1},{3,2}},
            {{3,4},{3,5},{3,6}},
            {{4,2},{4,3},{4,4}},
            {{5,1},{5,3},{5,5}},
            {{6,0},{6,3},{6,6}},
            // Horizontal mills
            {{0,0},{3,0},{6,0}},
            {{1,1},{3,1},{5,1}},
            {{2,2},{3,2},{4,2}},
            {{0,3},{1,3},{2,3}},
            {{4,3},{5,3},{6,3}},
            {{2,4},{3,4},{4,4}},
            {{1,5},{3,5},{5,5}},
            {{0,6},{3,6},{6,6}}
        }; ;

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

            int [][][] jumpTable = {
                {{3,3},{},{},{3,1},{},{},{3,3}},
                {{},{2,2},{},{2,1},{},{2,2},{}},
                {{},{},{1,1},{1,1},{1,1},{},{}},
                {{1,3},{1,2},{1,1},{},{1,1},{1,2},{1,3}},
                {{},{},{1,1},{1,1},{1,1},{},{}},
                {{},{2,2},{},{2,1},{},{2,2},{}},
                {{3,3},{},{},{3,1},{},{},{3,3}}
            };

            int jumpX = jumpTable[y][x][0];
            int jumpY = jumpTable[y][x][1];
            
            int[][] offsetTable = {
                {-jumpX, 0},
                {jumpX, 0},
                {0,-jumpY},
                {0,jumpY}
            };
            
            // Look arround the pawn
            for (int[] offfsetCoords : offsetTable){
                int newX = x + offfsetCoords[0];
                int newY = y + offfsetCoords[1];

                if (newX < 0 || newY < 0 || newX >= GRIDNBCOLS || newY >= GRIDNBROWS) {
                    continue; // Do nothing if the cell is unreachable
                }

                List<GameElement> elements = getElements(newY, newX);
                if (elements.size() == 0) {
                    lst.add(new Point(newX, newY));
                }
            }
            return lst;
        }
    }

    public boolean millsChecker(int color){
        
        // Historique
        // faire une variable sur les pawn isInAMill qui est a true si il est dans un moullin, comme ça si on parcour deux fois le moullin sans qu'il ait été changer, on l'ignore. Cela implique alors de changer la variable a chaque fois que un pawn du moullin est déplacer et casse le moullin déjà utiliser.
        
        // TODO : Studie how the move function, so we can implement the isInAMill variables cahnge of the pawn that is beeing moved

        int newPawnInMill;
        for (int[][] mill : mills) {
            newPawnInMill = 0;
            for (int[] coord : mill) {
                Pawn pawn = (Pawn) getFirstElement(coord[0], coord[1]);
                if (pawn.getColor() != color) break;
                if (!pawn.isInAMill()) newPawnInMill++;
            }
            if (newPawnInMill==3) return true;
        }
        return false;
    }
}
