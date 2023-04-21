package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.MoveAction;
import model.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class MerelleDecider extends Decider {

    private MerelleStageModel stage;
    private MerelleBoard board;
    private MerellePawnPot AIpot;

    ActionList actions = new ActionList(true);
    private Pawn pawnToMove;
    private Point destPoint;
    Random rand = new Random();
    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of MerelleStageModel
        stage = (MerelleStageModel) model.getGameStage();
        board = stage.getBoard(); // get the board

        // Prends le bon pot de pions
        if (model.getIdPlayer() == Pawn.PAWN_BLACK)
            AIpot = stage.getBlackPot();
        else
            AIpot = stage.getRedPot();

        GameElement pawnToMove = null; // the pawn that is moved

        if (stage.getStatus() == MerelleGameStatus.PLACING) {
            placePawn();
        }

        if (stage.getStatus() == MerelleGameStatus.MOVING) {
            movePawn();
        }
        return null;
    }

    /**
     * Dans la phase de placement des pions, analyse et place un pion du pot
     * -- Si l'IA peut completer un moulin, elle le complète.
     * -- Sinon si l'autre joueur peut completer un moulin, elle le bloque
     * -- Sinon elle choisit une case et pose le pion.
     */
    private void placePawn() {
        List<Point> millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer(), board);
        // Si l'IA peut complèter un moulin, elle le complète
        if (!millsToComplete.isEmpty()) {
            destPoint = new Point(millsToComplete.get(0).x, millsToComplete.get(0).y);
        } else {
            millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer() == 1 ? 0 : 1, board);
            // Si l'IA ne peut pas completer de moulin alors on vérifie si l'autre joueur peut : on le bloque
            if (!millsToComplete.isEmpty()) {
                destPoint = new Point(millsToComplete.get(0).x, millsToComplete.get(0).y);
            } else {
                // Sinon on remplit une case aléatoirement
                // FIXME casesVides ne retourne pas les cases vides mais toutes les cases de la grille
                List<Point> casesVides = board.computeValidCells(null, 1);
                System.out.println(Arrays.toString(casesVides.toArray()));

                destPoint = casesVides.get(rand.nextInt(casesVides.size()));
            }
        }

        pawnToMove = selectNextInPot();
        MoveAction move = new MoveAction(model, pawnToMove, "merelleboard", destPoint.y, destPoint.x);
        actions.addSingleAction(move);
    }


    /**
     * Selectionne le pion suivant dans le pot de pions
     *
     * @return Pion suivant
     */
    private Pawn selectNextInPot() {
        int i = 0;
        while (i < MerellePawnPot.PAWNS_IN_POT && AIpot.getElement(i, 0) == null)
            i++;
        return (Pawn) AIpot.getElement(i, 0);
    }


    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    private void movePawn() {
        // INPROGRESS pour chaque pions du joueur actuel (IA), créer tous les déplacement possibles et utiliser minimax() pour etudier les scores futurs
        int playerColor = model.getIdPlayer();
        int bestScore = Integer.MIN_VALUE;
        MoveAction bestMove;

        for (Pawn pawn : getPlayerPawnList(playerColor)) {
            for (Point positionsToMove : board.computeValidCells(pawn, MerelleGameStatus.MOVING)) {
                // TODO Faire le mouvement
                int score = minimax(board);
                // TODO Annuler le mouvement
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move; // TODO Assigner le meilleur mouvememnt
                }
            }
        }
    }

    private List<Pawn> getPlayerPawnList(int playerColor) {
        List<Pawn> playerPawnList = new ArrayList<>();
        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            Pawn pawn = (Pawn) board.getPawn(i, playerColor);
            if (pawn != null) {
                playerPawnList.add(pawn);
            }
        }
        return playerPawnList;
    }

    private int minimax(MerelleBoard board) {

    }

    private void removePawn() {

    }


    /**
     * Analyse les moulins qui sont prêts à etre validés (dont il reste qu'un pion pour valider le moulin)
     *
     * @param couleurJoueur Couleur du joueur a analyser les moulins
     * @param board
     * @return Liste des cases à remplir pour faire un moulin
     */
    private List<Point> getUncompletedMillsForPlayer(int couleurJoueur, MerelleBoard board) {
        List<Point> emptyCellsForMill = new ArrayList<Point>();
        for (int i = 0; i < MerelleBoard.ACTIVCELL.length; i++) {
            int x = MerelleBoard.ACTIVCELL[i][0];
            int y = MerelleBoard.ACTIVCELL[i][1];
            if (board.getFirstElement(x, y) == null) {
                if (hasMill(x, y, board, MerelleBoard.mills)) {
                    emptyCellsForMill.add(new Point(x, y));
                }
            }
        }
        return emptyCellsForMill;
    }

    /**
     * Vérifie si en plaçant le pion en (x, y) on crée un moulin
     *
     * @param x     position x du pion à verifier
     * @param y     position y du pion à verifier
     * @param board etat actuel du plateau
     * @param mills liste des moulins possibles
     * @return vrai si avec cette combinaison (x, y) un moulin sera créé
     */
    private boolean hasMill(int x, int y, MerelleBoard board, int[][][] mills) {
        int playerId = model.getIdPlayer();
        for (int[][] mill : mills) {
            if (contains(mill, new int[]{x, y})) {
                int count = 1;
                for (int[] position : mill) {
                    int pawnX = position[0];
                    int pawnY = position[1];
                    Pawn pawn = (Pawn) board.getFirstElement(pawnX, pawnY);
                    if (pawn == null || pawn.getColor() != playerId) {
                        break;
                    }
                    count++;
                }
                if (count == 3) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Vérifie que les coordonnées données sont contenues dans la combinaison de coordonnées (moulin) fourni
     *
     * @param mill     moulin à vérifier
     * @param position position à vérifier
     * @return vrai si la position est contenue dans le moulin
     */
    private boolean contains(int[][] mill, int[] position) {
        for (int[] p : mill) {
            if (p[0] == position[0] && p[1] == position[1]) {
                return true;
            }
        }
        return false;
    }
}