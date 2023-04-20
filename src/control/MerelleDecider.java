package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MerelleDecider extends Decider {

    private MerelleStageModel stage;
    private MerelleBoard board;
    private MerellePawnPot AIpot;

    ActionList actions = new ActionList(true);

    private Pawn pawnToMove;
    private int rowDest;

    Random rand = new Random();
    private int colDest;


    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of MerelleStageModel
        stage = (MerelleStageModel) model.getGameStage();
        board = stage.getBoard(); // get the board

        if (model.getIdPlayer() == Pawn.PAWN_BLACK)
            AIpot = stage.getBlackPot();
        else
            AIpot = stage.getRedPot();

        GameElement pawn = null; // the pawn that is moved
        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board

        if (stage.getStage() == MerelleGameStatus.PLACEMENT) {
            placePawn();
        }

        if (stage.getStage() == MerelleGameStatus.MOVEMENT) {
            movePawn();
        }

        return actions;
    }

    /**
     * Dans la phase de placement des pions, analyse et place un pion du pot
     */
    private void placePawn() {
//        TODO : Placer un pion du pot vers le jeux selon les règles suivantes
//               SI getUncompletedMillsForPlayer(IA) > 0 : Finir un moulin pour l'IA
//               SINON SI getUncompletedMillsForPlayer(AutreJoueur) > 0 : Empecher l'autre joueur de joueur
//               SINON :  Placer un pion au hasard sur une case libre
//        MoveAction move = new MoveAction(model, anyPawn, board.getGrid(), rowDest, colDest);
//        actions.addSingleAction(move);
    }

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    private void movePawn() {

    }

    private void removePawn(){

    }


    /**
     * Analyse les moulins qui sont prêts à etre validés (dont il reste qu'un pion pour valider le moulin)
     * @param couleurJoueur Couleur du joueur a analyser les moulins
     * @return Liste des cases à remplir pour faire un moulin
     */
    private List<int[]> getUncompletedMillsForPlayer(int couleurJoueur) {
        List<int[]> emptyCellsForMill = new ArrayList<>();
        for (int i = 0; i < MerelleBoard.ACTIVCELL.length; i++) {
            int x = MerelleBoard.ACTIVCELL[i][0];
            int y = MerelleBoard.ACTIVCELL[i][1];
            if (board.getFirstElement(x, y) == null) {
                if (hasMill(x, y, board, MerelleBoard.mills)) {
                    emptyCellsForMill.add(new int[]{x, y});
                }
            }
        }
        return emptyCellsForMill;
    }

    /**
     * Vérifie si en plaçant le pion en (x, y) on crée un moulin
     * @param x position x du pion à verifier
     * @param y position y du pion à verifier
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
     * @param mill moulin à vérifier
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