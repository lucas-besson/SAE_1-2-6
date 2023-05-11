package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;

import java.awt.*;
import java.util.List;

public class BasicDecider extends MerelleDecider {

    public BasicDecider(Model model, Controller control) {
        super(model, control);
    }

    /**
     * Dans la phase de placement des pions, analyse et place un pion du pot
     * -- Si l'IA peut completer un moulin, elle le complète.
     * -- Sinon si l'autre joueur peut completer un moulin, elle le bloque
     * -- Sinon elle choisit une case et pose le pion.
     */
    @Override
    void placePawn() {
        pawnToMove = selectNextInPot();
        initGridTable();
        List<Point> freePoints = getFreePoints(grid);

        destPoint = freePoints.get(rand.nextInt(freePoints.size()));
        actions.addSingleAction(new MoveAction(model, pawnToMove, "merelleboard", destPoint.x, destPoint.x));
    }

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    @Override
    void movePawn() {

    }

    /**
     * Algorithme qui vérifie le meilleur pion à supprimer (par ex. empecher l'autre joueur de finir un moulin...)
     *
     * @param plateau
     */
    @Override
    Point removePawn(int[][] plateau) {
        // TODO
        return null;
    }
}
