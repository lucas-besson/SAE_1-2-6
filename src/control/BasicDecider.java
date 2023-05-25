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
    protected void placePawn() {
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

        if (needToRemoveAPawn) actions.addSingleAction(removePawnAction(grid));
    }

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    @Override
    protected void movePawn() {
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
     * Method that return the best pawn to delete (will prevent the opponent to make a mill first)
     *
     * @param grid 2D int table : grid
     * @return Point
     */
    @Override
    protected Point removePawn(int[][] grid) {
        List<Point> playerPawnList = getPlayerPawnList((model.getIdPlayer() + 1) % 2, grid);
        return playerPawnList.get(rand.nextInt(playerPawnList.size()));
    }
}