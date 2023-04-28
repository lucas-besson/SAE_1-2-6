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

    private boolean needToRemovePawn;

    Random rand = new Random();
    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    private int[][] grid;

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
        stage = (MerelleStageModel) model.getGameStage();
        board = stage.getBoard();// get the board
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of MerelleStageModel

        // Prends le bon pot de pions
        if (model.getIdPlayer() == Pawn.PAWN_BLACK)
            AIpot = stage.getBlackPot();
        else
            AIpot = stage.getRedPot();

        GameElement pawnToMove = null; // the pawn that is moved
        needToRemovePawn = false;

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
     * Dans la phase de placement des pions, analyse et place un pion du pot
     * -- Si l'IA peut completer un moulin, elle le complète.
     * -- Sinon si l'autre joueur peut completer un moulin, elle le bloque
     * -- Sinon elle choisit une case et pose le pion.
     */
    private void placePawn() {
        List<Point> millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer(), board);
        // Si l'IA peut complèter un moulin, elle le complète
        if (!millsToComplete.isEmpty()) {
            int selectedPoint = rand.nextInt(millsToComplete.size());
            destPoint = new Point(millsToComplete.get(selectedPoint).x, millsToComplete.get(selectedPoint).y);
        } else {
            millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer() == 1 ? 0 : 1, board);
            // Si l'IA ne peut pas completer de moulin alors on vérifie si l'autre joueur peut : on le bloque
            if (!millsToComplete.isEmpty()) {
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
//        System.exit(100);
        // INPROGRESS pour chaque pions du joueur actuel (IA), créer tous les déplacement possibles et utiliser minimax() pour etudier les scores futurs
        int playerColor = model.getIdPlayer();
        int bestScore = Integer.MIN_VALUE;
        MoveAction bestMove = null;

        grid = new int[MerelleBoard.GRIDNBCOLS][MerelleBoard.GRIDNBROWS];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (stage.getGrid("merelleboard").getElements(i, j).isEmpty())
                    grid[j][i] = 2;
                else {
                    grid[j][i] = ((Pawn) stage.getGrid("merelleboard").getElements(i, j).get(0)).getColor();
                }
            }
        }

        for (Point point : getPlayerPawnList(playerColor, grid)) {
            for (Point positionsToMove : computeValidCells(point)) {
                // Faire la copie de la grid
                int[][] gridCopy = Arrays.copyOf(grid, grid.length);

                // Make the move
                gridCopy[positionsToMove.y][positionsToMove.x] = gridCopy[point.y][point.x];
                gridCopy[point.y][point.x] = 2;

                //FIXME Exception in thread "main" java.lang.NullPointerException: Cannot invoke "boardifier.model.GameElement.getGrid()" because "this.element" is null
                MoveAction move = new MoveAction(model, board.getFirstElement(point.x, point.y), "merelleboard", positionsToMove.x, positionsToMove.y);

                int score = minimax(gridCopy);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }

        actions.addSingleAction(bestMove);
    }

    private List<Point> computeValidCells(Point point) {
        ArrayList<Point> lst = new ArrayList<>();
        int[][][] jumpTable = {
                {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}},
                {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}},
                {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}},
                {{1, 3}, {1, 2}, {1, 1}, {}, {1, 1}, {1, 2}, {1, 3}},
                {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}},
                {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}},
                {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}
        };

        int jumpX = jumpTable[point.y][point.x][0];
        int jumpY = jumpTable[point.y][point.x][1];

        int[][] offsetTable = {
                {0, -jumpY},
                {-jumpX, 0}, {jumpX, 0},
                {0, jumpY}
        };

        for (int[] offsetCoords : offsetTable) {
            int newX = point.x + offsetCoords[0];
            int newY = point.y + offsetCoords[1];

            if (!MerelleBoard.isActiveCell(newX, newY))
                continue; // Do nothing if the cell is unreachable

            if (grid[newX][newY] == 0) {
                lst.add(new Point(newX, newY));
            }
        }
        return lst;
    }


    private List<Point> getPlayerPawnList(int playerColor, int[][] grid) {
        List<Point> playerPawnList = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == playerColor)
                    playerPawnList.add(new Point(i, j));
            }
        }
        return playerPawnList;
    }

    private int minimax(int[][] grid) {
        return 1;
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
        List<Point> emptyCellsForMill = new ArrayList<>();

        for (Point caseVide : board.computeValidCells(null, 1)) {
            if (hasMill(caseVide.x, caseVide.y, board, MerelleBoard.MILLS, couleurJoueur))
                emptyCellsForMill.add(caseVide);
        }
        return emptyCellsForMill;
    }

    /**
     * Vérifie si en plaçant le pion en (x, y) on crée un moulin
     *
     * @param x        position x du pion à verifier
     * @param y        position y du pion à verifier
     * @param board    etat actuel du plateau
     * @param mills    liste des moulins possibles
     * @param playerId
     * @return vrai si avec cette combinaison (x, y) un moulin sera créé
     */
    private boolean hasMill(int x, int y, MerelleBoard board, int[][][] mills, int playerId) {
        for (int[][] mill : mills) {
            if (contains(mill, new int[]{x, y})) {
                int count = 1;
                for (int[] position : mill) {
                    int pawnY = position[0];
                    int pawnX = position[1];
                    Pawn pawn = (Pawn) board.getFirstElement(pawnX, pawnY);
                    if (pawn == null)
                        break;
                    if (pawn.getColor() == playerId) {
                        return false;
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