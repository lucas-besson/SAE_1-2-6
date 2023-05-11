package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import model.Pawn;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class IntelligentDecider extends MerelleDecider {
    public IntelligentDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    void placePawn() {
        boolean needToRemoveAPawn = false;

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
        MoveAction move = new MoveAction(model, pawnToMove, "merelleboard", destPoint.y, destPoint.x);
        actions.addSingleAction(move);

        // Si un moulin est completé
        if (needToRemoveAPawn) {
            grid[destPoint.x][destPoint.y] = model.getIdPlayer();
            actions.addSingleAction(removePawnAction(grid));
        }
    }

    @Override
    void movePawn() {
        // INPROGRESS pour chaque pions du joueur actuel (IA), créer tous les déplacement possibles et utiliser minimax() pour etudier les scores futurs
        int playerColor = model.getIdPlayer();
        int bestScore = Integer.MIN_VALUE;
        MoveAction bestMove = null;

        initGridTable();

        for (Point point : getPlayerPawnList(playerColor, grid)) {
            for (Point positionsToMove : computeValidCells(point)) {
                // Faire la copie de la grid
                int[][] gridCopy = Arrays.copyOf(grid, grid.length);


                // Make the move
                pawnToMove = (Pawn) model.getGrid("merelleboard").getFirstElement(point.x, point.y);
                MoveAction move = new MoveAction(model, pawnToMove, "merelleboard", positionsToMove.y, positionsToMove.x);

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

        if (bestScore == 0) {
            // TODO si la victoire n'est pas encore à portée du minimax, on fait un mouvement aléatoire
            //      (pour éviter les jeux interminables (déplacements en avant et en arrière, et ainsi de suite)
        }

        // Make the move
        actions.addSingleAction(bestMove);
        grid[pawnToMove.getCol() - 1][pawnToMove.getRow() - 1] = 2;
        secondGrid[bestMove.getColDest()][bestMove.getRowDest()] = playerColor;

        if (isNewMill(grid, secondGrid, playerColor))
            actions.addSingleAction(removePawnAction(secondGrid));
    }

    int minimax(int[][] previousGrid, int[][] actualGrid, boolean isMaximizing, int depth) {
        int result = checkWinner(actualGrid);
        if (result == model.getIdPlayer()) return 1;
        else if (result == (model.getIdPlayer() + 1) % 2) return -1;
        else if (depth > 2000 || result == 2) return 0;

        int playerColor = isMaximizing ? model.getIdPlayer() : (model.getIdPlayer() + 1) % 2;

        if (isNewMill(previousGrid, actualGrid, playerColor)) {
            Point pawnToRemove = removePawn(actualGrid);
            assert pawnToRemove != null;
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

    Point removePawn(int[][] plateau) {
        Point meilleurPion = null;
        int joueur = 1; // On recherche le pion de l'adversaire, qui est représenté par 1
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                if (plateau[i][j] == joueur) {
                    int nbMoulins = millsCount(i, j, plateau);
                    if (nbMoulins > 0) { // Le pion forme au moins un moulin
                        // Si le pion peut être retiré sans former de moulin à l'adversaire, c'est le meilleur pion à retirer
                        if (!canMakeMill(i, j, plateau)) {
                            return new Point(i, j); // Retourne l'objet Point qui représente la position du pion à retirer
                        } else {
                            // Si le pion doit être retiré pour éviter un moulin à l'adversaire, le choisit comme meilleur pion à retirer
                            if (nbMoulins == 2) { // Si l'adversaire a deux pions qui forment des moulins, retirer n'importe lequel des deux peut être bénéfique
                                return new Point(i, j); // Retourne l'objet Point qui représente la position du pion à retirer
                            } else if (nbMoulins == 1) { // Si l'adversaire a un seul pion qui forme un moulin
                                if (meilleurPion == null || millsCount(meilleurPion.x, meilleurPion.y, plateau) == 0) {
                                    meilleurPion = new Point(i, j);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (meilleurPion == null) {
            meilleurPion = getPlayerPawnList(model.getIdPlayer() + 1 % 2, plateau).get(0);
        }
        return meilleurPion; // Retourne l'objet Point qui représente la position du pion à retirer, ou null si aucun pion ne peut être retiré
    }

}
