package control;

import boardifier.control.Controller;
import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import model.Pawn;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class IntelligentDecider extends MerelleDecider {
    public IntelligentDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public void placePawn() {
        boolean needToRemoveAPawn = false;

        initGridTable();

        // Moulins que l'ia peut completer
        java.util.List<Point> millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer(), grid);

        if (!millsToComplete.isEmpty()) {
            // Si l'IA peut complèter un moulin, elle le complète
            int selectedPoint = rand.nextInt(millsToComplete.size());
            destPoint = new Point(millsToComplete.get(selectedPoint).x, millsToComplete.get(selectedPoint).y);
            needToRemoveAPawn = true;
        } else {
            // Moulins que l'autre joueur peut completer
            millsToComplete = getUncompletedMillsForPlayer((model.getIdPlayer() == 0 ? 1 : 0), grid);

            if (!millsToComplete.isEmpty()) {
                // Si l'IA ne peut pas completer de moulin alors on vérifie si l'autre joueur peut : on le bloque
                int selectedPoint = rand.nextInt(millsToComplete.size());
                destPoint = new Point(millsToComplete.get(selectedPoint).x, millsToComplete.get(selectedPoint).y);
            } else {
                // Sinon on remplit une case aléatoirement
                List<Point> casesVides = board.computeValidCells(null, 1);
                destPoint = casesVides.get(rand.nextInt(casesVides.size()));
            }
        }

        pawnToMove = selectNextInPot();
//        NEW
        GridLook look = (GridLook) control.getElementLook(board);
        Coord2D center = look.getRootPaneLocationForCellCenter(destPoint.y, destPoint.x);
        MoveAction move = new MoveAction(model, pawnToMove, "merelleboard",
                                         destPoint.y, destPoint.x, AnimationTypes.MOVE_LINEARPROP,
                                         center.getX(), center.getY(), MerelleDecider.animationSpeed);
        actions.addSingleAction(move);

        if (needToRemoveAPawn) {
            grid[destPoint.x][destPoint.y] = model.getIdPlayer();
            printGrid(this.grid);
            actions.addSingleAction(removePawnAction(grid));
        }

        printGrid(this.grid);
    }

    @Override
    public void movePawn() {
        int playerColor = model.getIdPlayer();
        int bestScore = Integer.MIN_VALUE;
        MoveAction bestMove = null;

        initGridTable();
        printGrid(this.grid);
        for (Point point : getPlayerPawnList(playerColor, grid)) {
            for (Point positionsToMove : computeValidCells(point)) {
                // Faire la copie de la grid
                int[][] gridCopy = Arrays.copyOf(grid, grid.length);


                // Make the move
                pawnToMove = (Pawn) model.getGrid("merelleboard").getFirstElement(point.y, point.x);
                destPoint = new Point(positionsToMove.x, positionsToMove.y);
//                NEW

                GridLook look = (GridLook) control.getElementLook(board);
                Coord2D center = look.getRootPaneLocationForCellCenter(destPoint.y, destPoint.x);
                MoveAction move = new MoveAction(model, pawnToMove, "merelleboard",
                                                 destPoint.y, destPoint.x, AnimationTypes.MOVE_LINEARPROP,
                                                 center.getX(), center.getY(), MerelleDecider.animationSpeed);

                gridCopy[positionsToMove.x][positionsToMove.y] = gridCopy[point.x][point.y];
                gridCopy[point.x][point.y] = 2;

                int score = minimax(grid, gridCopy, true, 0);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                    secondGrid = grid;
                }
            }
        }
//        System.out.println(pawnToMove);
        if (bestScore == 0 || pawnToMove == null) {
            initGridTable();
//            System.out.println("IF");
            List<Point> playerPawnList = getPlayerPawnList(model.getIdPlayer(), grid);


//            System.out.println(playerPawnList);

            playerPawnList.removeIf(pawn -> computeValidCells(pawn).isEmpty());

//            System.out.println(playerPawnList);

            if (!playerPawnList.isEmpty()) {
                List<Point> destinations;
//                System.out.println("IF 2");
                do {
//                    System.out.println("Do While");
                    Point toMove = playerPawnList.get(rand.nextInt(playerPawnList.size()));
                    pawnToMove = (Pawn) model.getGrid("merelleboard").getFirstElement(toMove.y, toMove.x);

                    destinations = computeValidCells(toMove);

                    destPoint = destinations.get(rand.nextInt(destinations.size()));
                } while (destinations.isEmpty());
//                NEW
                GridLook look = (GridLook) control.getElementLook(board);
                Coord2D center = look.getRootPaneLocationForCellCenter(destPoint.y, destPoint.x);
                bestMove = new MoveAction(model, pawnToMove, "merelleboard",
                                          destPoint.y, destPoint.x, AnimationTypes.MOVE_LINEARPROP,
                                          center.getX(), center.getY(), MerelleDecider.animationSpeed);
            }
        }

        // Make the move
        actions.addSingleAction(bestMove);

        grid[pawnToMove.getCol()][pawnToMove.getRow()] = 2;
        assert bestMove != null;
        secondGrid[bestMove.getColDest()][bestMove.getRowDest()] = playerColor;

        if (isNewMill(grid, secondGrid, playerColor))
            actions.addSingleAction(removePawnAction(secondGrid));

        grid = secondGrid;
        printGrid(this.grid);
    }

    public int minimax(int[][] previousGrid, int[][] actualGrid, boolean isMaximizing, int depth) {
        int result = checkWinner(actualGrid);
        if (result == model.getIdPlayer()) return 1;
        else if (result == (model.getIdPlayer() + 1) % 2) return -1;
        else if (depth > 2000 || result == 2) return 0;

        int playerColor = isMaximizing ? model.getIdPlayer() : (model.getIdPlayer() + 1) % 2;

        if (isNewMill(previousGrid, actualGrid, playerColor)) {
            Point pawnToRemove = removePawn(actualGrid);
            actualGrid[pawnToRemove.x][pawnToRemove.y] = 2;
        }
        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (Point point : getPlayerPawnList(playerColor, actualGrid)) {
                for (Point positionsToMove : computeValidCells(point)) {
                    // Faire la copie de la grid
                    int[][] gridCopy = Arrays.copyOf(actualGrid, actualGrid.length);

                    gridCopy[positionsToMove.x][positionsToMove.y] = gridCopy[point.x][point.y];
                    gridCopy[point.x][point.y] = 2;

                    int score = minimax(actualGrid, gridCopy, false, depth + 1);

                    bestScore = Math.max(score, bestScore);
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (Point point : getPlayerPawnList(playerColor, actualGrid)) {
                for (Point positionsToMove : computeValidCells(point)) {
                    // Faire la copie de la grid
                    int[][] gridCopy = Arrays.copyOf(actualGrid, actualGrid.length);

                    gridCopy[positionsToMove.x][positionsToMove.y] = gridCopy[point.x][point.y];
                    gridCopy[point.x][point.y] = 2;

                    int score = minimax(actualGrid, gridCopy, true, depth + 1);

                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    /**
     * Method that return the best pawn to delete (will prevent the opponent to make a mill first)
     *
     * @param grid 2D int table : grid
     * @return Point
     */
    protected Point removePawn(int[][] grid) {
        List<Point> adversairePions = getPlayerPawnList((model.getIdPlayer() + 1) % 2, grid);
        Point meilleurPion = adversairePions.get(0);
        int joueur = (model.getIdPlayer() + 1) % 2; // On recherche le pion de l'adversaire, qui est représenté par 1

        for (Point pion : adversairePions) {
            int col = pion.x;
            int row = pion.y;
            int nbMoulins = millsCount(col, row, grid);

            if (nbMoulins > 0) {

                if (!canMakeMill(col, row, grid)) {
                    return new Point(col, row);
                } else {

                    if (nbMoulins == 2) {
                        if (meilleurPion == null || millsCount(meilleurPion.x, meilleurPion.y, grid) < 2) {
                            meilleurPion = new Point(col, row);
                        }

                    } else if (nbMoulins == 1) {

                        int nbMoulinsMeilleurPion = millsCount(meilleurPion.x, meilleurPion.y, grid);
                        if (meilleurPion == null || nbMoulinsMeilleurPion == 0 || nbMoulinsMeilleurPion == joueur) {
                            meilleurPion = new Point(col, row);
                        }
                    }
                }
            }
        }

        if (grid[meilleurPion.x][meilleurPion.y] == joueur) {
            meilleurPion = adversairePions.get(0);
        }

        return meilleurPion; // Retourne l'objet Point qui représente la position du pion à retirer, ou null si aucun pion ne peut être retiré
    }


    /**
     * return the winner ID, or 2 if there is no winner
     *
     * @param actualGrid 2D int table : grid
     * @return idPlayer that wins
     */
    int checkWinner(int[][] actualGrid) {
        if (getPlayerPawnList(model.getIdPlayer(), actualGrid).size() < 3)
            return model.getIdPlayer();
        if (getPlayerPawnList((model.getIdPlayer() + 1) % 2, actualGrid).size() < 3)
            return (model.getIdPlayer() + 1) % 2;
        return 2;
    }

}