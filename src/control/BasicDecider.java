package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import model.Pawn;

import java.awt.*;
import java.util.List;

public class BasicDecider extends MerelleDecider {

    public BasicDecider(Model model, Controller control) {
        super(model, control);
    }

    /**
     * Dans la phase de placement des pions, analyse et place un pion du pot
     * -- Si l'IA peut completer un moulin, elle le complète.
     * -- Sinon elle choisit une case et pose le pion aléatoirement.
     */
    @Override
    void placePawn() {
        initGridTable();

        pawnToMove = selectNextInPot();

        boolean needToRemoveAPawn = false;

        // Moulins que l'ia peut completer
        java.util.List<Point> millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer(), grid);

        if (!millsToComplete.isEmpty()) {
            // Si l'IA peut complèter un moulin, elle le complète
            int selectedPoint = rand.nextInt(millsToComplete.size());
            destPoint = new Point(millsToComplete.get(selectedPoint).x, millsToComplete.get(selectedPoint).y);
            needToRemoveAPawn = true;
        } else {
            List<Point> freePoints = getFreePoints(grid);
            destPoint = freePoints.get(rand.nextInt(freePoints.size()));
        }

        actions.addSingleAction(new MoveAction(model, pawnToMove, "merelleboard", destPoint.y, destPoint.x));

        if (needToRemoveAPawn)
            actions.addSingleAction(removePawnAction(grid));
    }

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    @Override
    void movePawn() {
        initGridTable();

        List<Point> playerPawnList = getPlayerPawnList(model.getIdPlayer(), grid);

        playerPawnList.removeIf(pawn -> computeValidCells(pawn).isEmpty());

        if (!playerPawnList.isEmpty()) {
            Point toMove = playerPawnList.get(rand.nextInt(playerPawnList.size()));
            pawnToMove = (Pawn) model.getGrid("merelleboard").getFirstElement(toMove.y, toMove.x);

            List<Point> destinations = computeValidCells(toMove);

            destPoint = destinations.get(rand.nextInt(destinations.size()));

            actions.addSingleAction(new MoveAction(model, pawnToMove, "merelleboard", destPoint.y, destPoint.x));

            secondGrid = grid;
            grid[toMove.x][toMove.y] = 2;
            secondGrid[destPoint.x][destPoint.y] = model.getIdPlayer();

            if (isNewMill(grid, secondGrid, model.getIdPlayer()))
                actions.addSingleAction(removePawnAction(secondGrid));

        }
    }

    /**
     * Algorithme qui vérifie le meilleur pion à supprimer (empecher l'autre joueur de finir un moulin...)
     *
     * @param plateau situation actuelle du jeu
     */
    @Override
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

