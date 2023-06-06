package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.RemoveAction;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class MerelleDecider extends Decider {

    MerelleStageModel stage;
    MerelleBoard board;
    MerellePawnPot aIpot;

    ActionList actions = new ActionList(true);
    Pawn pawnToMove;
    Point destPoint;

    Random rand = new Random();

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    int[][] grid;
    int[][] secondGrid;
    public int test;

    public int getTest() {
        return test;
    }

    public int[][] getGrid() {
        return grid;
    }

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
        stage = (MerelleStageModel) model.getGameStage();
        board = stage.getBoard();// get the board
    }

    /**
     * return the active cells base on the given grid
     *
     * @param grid actual grid
     * @return list of Point of empty case
     */
    public List<Point> getFreePoints(int[][] grid) {
        ArrayList<Point> lst = new ArrayList<>();
        for (int[] activePoint : MerelleBoard.ACTIVECELLS)
            if (grid[activePoint[0]][activePoint[1]] == 2)
                lst.add(new Point(activePoint[0], activePoint[1]));
        return lst;
    }

    @Override
    public ActionList decide() {

        initGridTable();

        // Prends le bon pot de pions
        if (model.getIdPlayer() == Pawn.PAWN_BLACK) aIpot = stage.getBlackPot();
        else aIpot = stage.getRedPot();

        if (stage.getStatus() == MerelleGameStatus.PLACING) {
            placePawn();
            return actions;
        }

        if (stage.getStatus() == MerelleGameStatus.MOVING) {
            movePawn();
            return actions;
        }
        return null;
    }


    /**
     * Dans la phase de placement des pions, analyse et place un pion du pot -- Methode abstraite, à redéfinir.
     */
    public abstract void placePawn();

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu  -- Methode abstraite, à redéfinir.
     */
    protected abstract void movePawn();

    protected abstract Point removePawn(int[][] actualGrid);


    /**
     * Actualise la grid avec le statut actuel de MerelleBoard
     */
    void initGridTable() {
        grid = new int[MerelleBoard.GRIDNBCOLS][MerelleBoard.GRIDNBROWS];
//        for (int[] activePoint : MerelleBoard.ACTIVECELLS) {
//            if (model.getGrid("merelleboard").getElements(activePoint[1], activePoint[0]).isEmpty())
//                grid[activePoint[1]][activePoint[0]] = 2;
//            else {
//                grid[activePoint[1]][activePoint[0]] = ((Pawn) model.getGrid("merelleboard").getElements(activePoint[1], activePoint[0]).get(0)).getColor();
//            }
//        }
        for (int col = 0; col < grid.length; col++) {
            for (int row = 0; row < grid[col].length; row++) {
                if (model.getGrid("merelleboard").getElements(row, col).isEmpty())
                    grid[col][row] = 2;
                else {
                    grid[col][row] = ((Pawn) model.getGrid("merelleboard").getElements(row, col).get(0)).getColor();
                }
            }
        }
    }

    /**
     * return the possible moves for a given pawn
     *
     * @param point point to move
     * @return list of Point
     */
    public List<Point> computeValidCells(Point point) {
        ArrayList<Point> lst = new ArrayList<>();
        if (grid[point.x][point.y] == 2) {
            return lst;
        }
        int[][][] jumpTable = {{{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}, {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}}, {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}}, {{1, 3}, {1, 2}, {1, 1}, {}, {1, 1}, {1, 2}, {1, 3}}, {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}}, {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}}, {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}};

        int jumpX = jumpTable[point.y][point.x][0];
        int jumpY = jumpTable[point.y][point.x][1];

        int[][] offsetTable = {
                {0, -jumpY},
                {-jumpX, 0},
                {jumpX, 0},
                {0, jumpY}
        };

        for (int[] offsetCoords : offsetTable) {
            int newX = point.x + offsetCoords[0]; //col
            int newY = point.y + offsetCoords[1]; //row

            if (!MerelleBoard.isActiveCell(newX, newY))
                continue; // Do nothing if the cell is unreachable

            if (grid[newX][newY] == 2) {
                lst.add(new Point(newX, newY));
            }
        }
        return lst;
    }

    /**
     * select the next pawn on the pot
     *
     * @return Pawn
     */
    Pawn selectNextInPot() {
        int i = 0;
        while (i < MerellePawnPot.PAWNS_IN_POT && aIpot.getElement(i, 0) == null) i++;
        return (Pawn) aIpot.getElement(i, 0);
    }

    /**
     * return all the pawn possessed by the given player
     *
     * @param playerColor color of the player
     * @param grid        2D int table
     * @return List of Point
     */
    List<Point> getPlayerPawnList(int playerColor, int[][] grid) {
        List<Point> playerPawnList = new ArrayList<>();
        for (int[] activePoint : MerelleBoard.ACTIVECELLS) {
            if (grid[activePoint[0]][activePoint[1]] == playerColor)
                playerPawnList.add(new Point(activePoint[0], activePoint[1]));
        }
        return playerPawnList;
    }


    /**
     * Check if a new mill is present between the two grids in parameters, for the player set in parameters
     *
     * @param previousGrid previous grid
     * @param actualGrid   new grid
     * @param playerColor  color of the player
     * @return true is there is a new mill, false otherwise
     */
    public boolean isNewMill(int[][] previousGrid, int[][] actualGrid, int playerColor) {
        for (int[][] mill : MerelleBoard.MILLS) {
            int x1 = mill[0][0];
            int y1 = mill[0][1];
            int x2 = mill[1][0];
            int y2 = mill[1][1];
            int x3 = mill[2][0];
            int y3 = mill[2][1];

            // Vérifier si les 3 positions du mill sont occupées par le joueur actuel
            if (actualGrid[x1][y1] == actualGrid[x2][y2] && actualGrid[x2][y2] == actualGrid[x3][y3] && actualGrid[x1][y1] == playerColor) {

                // Vérifier si le joueur n'occupait pas les 3 positions du mill avant
                if (previousGrid[x1][y1] != actualGrid[x1][y1] || previousGrid[x2][y2] != actualGrid[x2][y2] || previousGrid[x3][y3] != actualGrid[x3][y3])
                    return true;
            }
        }
        return false;
    }

    /**
     * Verify if a new mill can be created by placing a pawn in a specific cell
     *
     * @param x       int x coordinate
     * @param y       int y coordinate
     * @param plateau 2D int grid
     * @return true if there will be a new mill, false otherwise
     */
    public boolean canMakeMill(int x, int y, int[][] plateau) {
        int joueur = plateau[x][y];
        for (int i = 0; true; i++) {
            if (plateau[x][(y + i) % 3] != joueur) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }
        for (int i = 0; true; i++) {
            if (plateau[(x + i) % 3][y] != joueur) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that return the number of mill that a pawn could form if it was placed on the given coordinates
     *
     * @param x       int x coordinate
     * @param y       int y coordinate
     * @param plateau 2D int grid
     * @return int
     */
    public int millsCount(int x, int y, int[][] plateau) {
        int moulins = 0;
        int joueur = plateau[x][y];
        if (joueur == 0) {
            return 0;
        }
        // Vérifie les moulins horizontaux et verticaux
        if (plateau[x][(y + 1) % 3] == joueur && plateau[x][(y + 2) % 3] == joueur) {
            moulins++;
        }
        if (plateau[(x + 1) % 3][y] == joueur && plateau[(x + 2) % 3][y] == joueur) {
            moulins++;
        }
        // Vérifie les moulins diagonaux
        if ((x == 0 && y == 0) || (x == 1 && y == 1) || (x == 2 && y == 2)) {
            if (plateau[(x + 1) % 3][(y + 1) % 3] == joueur && plateau[(x + 2) % 3][(y + 2) % 3] == joueur) {
                moulins++;
            }
        }
        if ((x == 0 && y == 2) || (x == 1 && y == 1) || (x == 2 && y == 0)) {
            if (plateau[(x + 1) % 3][(y + 2) % 3] == joueur && plateau[(x + 2) % 3][(y + 1) % 3] == joueur) {
                moulins++;
            }
        }
        return moulins;
    }

    /**
     * return the best possible RemoveAction for the given grid, base on the result of the method removePawn.
     *
     * @param actualGrid 2D int table : actual grid
     * @return RemoveAction
     */
    RemoveAction removePawnAction(int[][] actualGrid) {
        Point pawnToRemove = removePawn(actualGrid);
        return new RemoveAction(model, board.getFirstElement(pawnToRemove.y, pawnToRemove.x));
    }

    /**
     * Method that return the cell that can be used to form a mill for the player of the given color.
     *
     * @param playerColor int : color of player
     * @param grid        2D int table : actual grid
     * @return List of Point
     */
    List<Point> getUncompletedMillsForPlayer(int playerColor, int[][] grid) {
        List<Point> emptyCellsForMill = new ArrayList<>();

//        if (model.getIdPlayer() == 1) return emptyCellsForMill;

        // Pour chaque case vide
        for (Point caseVide : getFreePoints(grid)) {
            // Si en placant un pion de la couleur indiquée on fait un moulin, on ajoute le pion à la liste
            if (hasMill(caseVide.x, caseVide.y, grid, playerColor))
                emptyCellsForMill.add(caseVide);
        }

        return emptyCellsForMill;
    }

    /**
     * Verify if a new mill can be created by placing a pawn in a specific cell
     *
     * @param col      int : column coordinates
     * @param row      int : row coordinates
     * @param grid     2D int table : actual grid
     * @param playerId int : color of player
     * @return true if there will be a new mill, false otherwise
     */
    public boolean hasMill(int col, int row, int[][] grid, int playerId) {
        for (int[][] mill : MerelleBoard.MILLS) {
            if (contains(mill, new int[]{col, row})) {
                int count = 1;
                for (int[] position : mill) {
                    int pawnX = position[0];
                    int pawnY = position[1];
                    if (grid[pawnX][pawnY] == playerId) count++;
                }
                if (count == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verify if the cell is in the given mill
     *
     * @param millToCheck int 2D table : mill
     * @param position    int table : cell coordinate
     * @return true if the given mill table contain the cell coordinate, false otherwise
     */
    public boolean contains(int[][] millToCheck, int[] position) {
        for (int[] millPoint : millToCheck) {
            if (millPoint[0] == position[0] && millPoint[1] == position[1]) return true;
        }
        return false;
    }

}

