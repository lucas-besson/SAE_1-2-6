package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MerelleBoard extends GridElement {
    /**
     * 2D table of all active cells
     */
    public static final int[][] ACTIVECELLS = {
            {0, 0}, {0, 3}, {0, 6},
            {1, 1}, {1, 3}, {1, 5},
            {2, 2}, {2, 3}, {2, 4},
            {3, 0}, {3, 1}, {3, 2},
            {3, 4}, {3, 5}, {3, 6},
            {4, 2}, {4, 3}, {4, 4},
            {5, 1}, {5, 3}, {5, 5},
            {6, 0}, {6, 3}, {6, 6}
    };

    /**
     * 3D table of all possible mills
     */
    public static final int[][][] MILLS = {
            // Vertical mills
            {{0, 0}, {0, 3}, {0, 6}},
            {{1, 1}, {1, 3}, {1, 5}},
            {{2, 2}, {2, 3}, {2, 4}},
            {{3, 0}, {3, 1}, {3, 2}},
            {{3, 4}, {3, 5}, {3, 6}},
            {{4, 2}, {4, 3}, {4, 4}},
            {{5, 1}, {5, 3}, {5, 5}},
            {{6, 0}, {6, 3}, {6, 6}},
            // Horizontal mills
            {{0, 0}, {3, 0}, {6, 0}},
            {{1, 1}, {3, 1}, {5, 1}},
            {{2, 2}, {3, 2}, {4, 2}},
            {{0, 3}, {1, 3}, {2, 3}},
            {{4, 3}, {5, 3}, {6, 3}},
            {{2, 4}, {3, 4}, {4, 4}},
            {{1, 5}, {3, 5}, {5, 5}},
            {{0, 6}, {3, 6}, {6, 6}}};
    /**
     * Number of rows and columns in the grid
     */
    public static final int GRIDNBROWS = 7;
    public static final int GRIDNBCOLS = 7;

    public MerelleBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a GRIDNBROWS x GRIDNBCOLS grid, named "merelleboard", and in x,y in space
        super("merelleboard", x, y, GRIDNBROWS, GRIDNBCOLS, gameStageModel);
        resetReachableCells(false);
    }

    /**
     * Copy constructor based on the given MerelleBoard and MerelleStageModel
     *
     * @param board board à copier
     */
    public MerelleBoard(MerelleBoard board, MerelleStageModel stage) {
        // call the super-constructor to create a GRIDNBROWS x GRIDNBCOLS grid, named "merelleboard", and in x,y in space
        super(board.getName(), (int) board.getX(), (int) board.getY(), board.getNbRows(), board.getNbCols(), stage);
        resetReachableCells(false);
    }

    /**
     * Method to check if the given coordinates are reachable
     *
     * @param x position
     * @param y position
     * @return true if the cell is active, false otherwise
     */
    public static boolean isActiveCell(int x, int y) {
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            if (x == cell[0] && y == cell[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that return the pawn of the given number from a given player
     *
     * @param number number that represent the pawn
     * @param color  color of the player
     * @return a pawn if it was found, null otherwise
     */
    public GameElement getPawn(int number, int color) {
        for (int[] cell : ACTIVECELLS) {
            if (!isEmptyAt(cell[1], cell[0])) {
                Pawn pawn = (Pawn) getElement(cell[1], cell[0]);
                if (pawn.getColor() == color && pawn.getNumber() == number) {
                    return pawn;
                }
            }
        }
        return null;
    }

    /**
     * Update the reachableCells table base on the list returned by computeValidCells method.
     *
     * @param pawn       pawn that is being moved
     * @param gameStatus status of the game
     */
    public void setValidCells(Pawn pawn, int gameStatus) {
        resetReachableCells(false);
        List<Point> valid = computeValidCells(pawn, gameStatus);
        if (valid != null) {
            for (Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
    }

    /**
     * Compute the valid cell to play given a pawn and the status of the game
     *
     * @param pawn       pawn that is being moved
     * @param gameStatus status of the game
     * @return list of points (valid cells)
     */
    public List<Point> computeValidCells(Pawn pawn, int gameStatus) {
        List<Point> lst = new ArrayList<>();

        // First stage of the game : all empty cells are valid
        if (gameStatus == MerelleGameStatus.PLACING) {
            for (int[] cell : ACTIVECELLS) {
                if (getElements(cell[1], cell[0]).size() == 0) {
                    lst.add(new Point(cell[0], cell[1]));
                }
            }
        }

        // Second stage of the game : the cells have to be empty and within 1 cells around the initial pawn position
        else {
            int x = pawn.getCol() - 1;
            int y = pawn.getRow() - 1;

            int[][][] jumpTable = {
                    {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}},
                    {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}},
                    {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}},
                    {{1, 3}, {1, 2}, {1, 1}, {}, {1, 1}, {1, 2}, {1, 3}},
                    {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}},
                    {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}},
                    {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}
            };

            int jumpX = jumpTable[y][x][0];
            int jumpY = jumpTable[y][x][1];

            int[][] offsetTable = {{0, -jumpY}, {-jumpX, 0}, {jumpX, 0}, {0, jumpY}};

            // Look arround the pawn
            for (int[] offfsetCoords : offsetTable) {
                int newX = x + offfsetCoords[0];
                int newY = y + offfsetCoords[1];

                if (!isActiveCell(newX, newY)) continue; // Do nothing if the cell is unreachable

                List<GameElement> elements = getElements(newY, newX);
                if (elements.size() == 0) {
                    lst.add(new Point(newX, newY));
                }
            }
        }
        return lst;
    }

    /**
     * @param color      color of the player
     * @param gameStatus the number of available moves for a given player
     * @return the number of move that can be played for a player of the given color
     */
    public int availableMoves(int color, int gameStatus) {
        int availableMoove = 0;
        if (gameStatus == MerelleGameStatus.PLACING) {
            return computeValidCells(null, MerelleGameStatus.PLACING).size();
        }
        for (int[] cell : ACTIVECELLS) {
            Pawn pawn = (Pawn) getFirstElement(cell[1], cell[0]);
            if (pawn == null || pawn.getColor() != color) continue;
            availableMoove += computeValidCells(pawn, gameStatus).size();
        }
        return availableMoove;
    }

    /**
     * @param color color of the player
     * @return true if a new mill has been formed for a given player
     */
    public boolean millsChecker(int color) {

        int pawnInMill, newPawnInMill;
        for (int[][] mill : MILLS) {
            newPawnInMill = 0;
            pawnInMill = 0;
            for (int[] coord : mill) {
                Pawn pawn = (Pawn) getFirstElement(coord[0], coord[1]);
                if (pawn == null || pawn.getColor() != color) break;
                else pawnInMill++;
                if (!pawn.isInAMill()) newPawnInMill++;
            }
            if (pawnInMill == 3 && newPawnInMill > 0) {
                millUpdate(mill);
                return true;
            }
        }
        return false;
    }

    /**
     * Set all the pawn isInAMill variables to true for all the pawn that is in the given mill mill.
     *
     * @param mill table int[][] of the coordinates of the new mill.
     */
    private void millUpdate(int[][] mill) {
        for (int[] coord : mill) {
            Pawn pawn = (Pawn) getFirstElement(coord[0], coord[1]);
            if (pawn != null) pawn.setInAMill(true);
        }
    }

    /**
     * Method that update the reachableCells variable for the cell that can be access for the player when he have to remove a pawn after a mill.
     *
     * @param color color of the player
     */
    public void setValidMillCells(int color) {
        resetReachableCells(false);
        List<Point> valid = computeValidMillCells(color);
        for (Point p : valid) {
            reachableCells[p.y][p.x] = true;
        }
    }

    /**
     * Compute the pawn that can be removed for a player of the given color.
     *
     * @param color color of the player playing the move
     * @return list of points (valid cells)
     */
    private List<Point> computeValidMillCells(int color) {
        List<Point> lst = new ArrayList<>();
        for (int[] cell : ACTIVECELLS) {
            Pawn pawn = (Pawn) getFirstElement(cell[1], cell[0]);
            if (pawn != null && pawn.getColor() != color) {
                lst.add(new Point(cell[0], cell[1]));
            }
        }
        return lst;
    }
}
