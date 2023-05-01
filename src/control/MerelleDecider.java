package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import model.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MerelleDecider extends Decider {

    private MerelleStageModel stage;
    private MerelleBoard board;
    private MerellePawnPot AIpot;

    ActionList actions = new ActionList(true);
    private Pawn pawnToMove;
    private Point destPoint;

    Random rand = new Random();
    private int[][] grid;
    private int[][] bestMoveGrid;

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

        if (stage.getStatus() == MerelleGameStatus.PLACING) {
            placePawn();
            return actions;
        }

        if (stage.getStatus() == MerelleGameStatus.MOVING) {
            movePawn();
            if (isNewMill(grid, bestMoveGrid, model.getIdPlayer()))
                actions.addSingleAction(removePawn(bestMoveGrid));
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
        boolean needToRemoveAPawn = false;
        List<Point> millsToComplete = getUncompletedMillsForPlayer(model.getIdPlayer(), board);
        // Si l'IA peut complèter un moulin, elle le complète
        if (!millsToComplete.isEmpty()) {
            int selectedPoint = rand.nextInt(millsToComplete.size());
            destPoint = new Point(millsToComplete.get(selectedPoint).x, millsToComplete.get(selectedPoint).y);
            needToRemoveAPawn = true;
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

        if (needToRemoveAPawn) {
            initGridTable();
            grid[destPoint.x][destPoint.y] = model.getIdPlayer();
            actions.addSingleAction(removePawn(grid));
        }
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
        MoveAction bestMove = null;

        initGridTable();

        for (Point point : getPlayerPawnList(playerColor, grid)) {
            for (Point positionsToMove : computeValidCells(point)) {
                System.out.println(point + " " + computeValidCells(point));
                // Faire la copie de la grid
                int[][] gridCopy = Arrays.copyOf(grid, grid.length);


                // Make the move
                pawnToMove = (Pawn) model.getGrid("merelleboard").getElement(point.x, point.y);
                MoveAction move = new MoveAction(model, pawnToMove, "merelleboard", positionsToMove.y, positionsToMove.x);

                gridCopy[positionsToMove.x][positionsToMove.y] = gridCopy[point.x][point.y];
                gridCopy[point.x][point.y] = 2;

                int score = minimax(grid, gridCopy, true);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                    bestMoveGrid = Arrays.copyOf(gridCopy, gridCopy.length);
                }
            }
        }
        actions.addSingleAction(bestMove);
    }

    /**
     * Convertit la board en tableau 2D
     */
    private void initGridTable() {
        grid = new int[MerelleBoard.GRIDNBCOLS][MerelleBoard.GRIDNBROWS];
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
     * Retourne une liste de déplacements possibles d'un pion
     *
     * @param point Pion à déplacer
     * @return Liste de Point ou ce pion peut etre déplacé
     */
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

        int jumpX = jumpTable[point.x][point.y][0];
        int jumpY = jumpTable[point.x][point.y][1];

        int[][] offsetTable = {
                {0, -jumpY},
                {-jumpX, 0}, {jumpX, 0},
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
     * Construit une liste de points des Pawn du joueur passé en paramètres.
     *
     * @param playerColor Couleur du joueur à récuperer les pions
     * @param grid        Tableau 2D de la grille actuelle (2: null or not active)
     * @return Liste de pions du joueur (Point(x, y))
     */
    private List<Point> getPlayerPawnList(int playerColor, int[][] grid) {
        List<Point> playerPawnList = new ArrayList<>();

        for (int col = 0; col < grid.length; col++) {
            for (int row = 0; row < grid[col].length; row++) {
                if (grid[col][row] == playerColor)
                    playerPawnList.add(new Point(col, row));
            }
        }
        return playerPawnList;
    }

    private int minimax(int[][] previousGrid, int[][] actualGrid, boolean isMaximizing) {
        int result = checkWinner(actualGrid);
        if (result == model.getIdPlayer()) {
            return 1;
        } else if (result != 2) {
            return -1;
        }

        int playerColor = isMaximizing ? model.getIdPlayer() : (model.getIdPlayer() + 1) % 2;

        if (isNewMill(previousGrid, actualGrid, playerColor)) {
            Point pawnToRemove = removePawn(playerColor, actualGrid);
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

                    int score = minimax(actualGrid, gridCopy, false);

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

                    int score = minimax(actualGrid, gridCopy, true);

                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    /**
     * Retourne le vainqueur du tour, 2 si aucun vainqueur
     *
     * @param actualGrid grille à vérifier
     * @return idPlayer that wins
     */
    private int checkWinner(int[][] actualGrid) {
        if (getPlayerPawnList(model.getIdPlayer(), actualGrid).size() < 3)
            return model.getIdPlayer();
        if (getPlayerPawnList((model.getIdPlayer() + 1) % 2, actualGrid).size() < 3)
            return (model.getIdPlayer() + 1) % 2;
        return 2;
    }


    /**
     * Check if a new mill is present between the two grids in parameters, for the player set in parameters
     *
     * @param previousGrid Grille précédente
     * @param actualGrid   Nouvelle grille
     * @param playerColor  Couleur du joueur
     * @return vrai ou faux si oui ou non il y a nouveau moulin
     */
    private boolean isNewMill(int[][] previousGrid, int[][] actualGrid, int playerColor) {
        for (int[][] mill : MerelleBoard.MILLS) {
            int x1 = mill[0][0];
            int y1 = mill[0][1];
            int x2 = mill[1][0];
            int y2 = mill[1][1];
            int x3 = mill[2][0];
            int y3 = mill[2][1];

            // Vérifier si les 3 positions du mill sont occupées par le joueur actuel
            if (actualGrid[x1][y1] == actualGrid[x2][y2] &&
                    actualGrid[x2][y2] == actualGrid[x3][y3] &&
                    actualGrid[x1][y1] == playerColor) {

                // Vérifier si le joueur n'occupait pas les 3 positions du mill avant
                if (previousGrid[x1][y1] != actualGrid[x1][y1] ||
                        previousGrid[x2][y2] != actualGrid[x2][y2] ||
                        previousGrid[x3][y3] != actualGrid[x3][y3]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Algorithme (non IA) qui vérifie le meilleur pion à supprimer (par ex. empecher l'autre joueur de finir un moulin...)
     *
     * @param playerColor
     */
    private Point removePawn(int playerColor, int[][] actualGrid) {
        ArrayList<Point> best_possible_positions = new ArrayList<>();
        var opponent = (model.getIdPlayer() + 1) % 2;

        /* Pour chaque pion adverse, je regarde pour chacun de ces coups
           s'il y a possibilité de faire un moulin si OUI on ajoute la position du coup dans
           la liste best_possible_positions */ // Des lignes : 308 à 318

        for(int i = 0; i < getPlayerPawnList(opponent, actualGrid).size(); i++) {
            Point opPawn = getPlayerPawnList(opponent, actualGrid).get(i);

            for(int j = 0; j < computeValidCells(opPawn).size(); j++) {
                Point position = computeValidCells(opPawn).get(j);

                if(getPossibleMills(position, actualGrid)) {
                    best_possible_positions.add(position);
                }
            }
        }

        if(best_possible_positions.isEmpty()) { return getPlayerPawnList(opponent, actualGrid).get(0); }

        //Vérification du meilleur pion à enlever parmis tous les coups ou un moulin est faisable
        var bestPosition = best_possible_positions.get(0);
        for(Point position : best_possible_positions) {
            if(nb_pawn_op( , opponent, actualGrid)) {
                bestPosition = position;
            }
        }
        return null;
    }

    /**
     * TODO: Doit retourner Vrai si un moulin est faisable à la position entré en paramètre sinon False
     * @param position
     * @param actualGrid
     * @return
     */
    private boolean getPossibleMills(Point position, int [][] actualGrid) {
        return false;
    }

    /**
     * TODO: Doit retourner le nombre de pions adverse pour une liste donnée en entrée
     * @return
     */
    private int nb_pawn_op(ArrayList<Point> positions, int[][] actualGrid) {
        int pawnNumber = 0;
        for(Point position : positions) {
            if(board.getFirstElement(position.getLocation().x, position.getLocation().y).)
        }
        return pawnNumber;
    }

    /**
     * Retourne le meilleure RemoveAction à effectuer pour la grille actuelle.
     *
     * @param actualGrid
     * @return RemoveAction à effectuer par le joueur actuel
     */
    private RemoveAction removePawn(int[][] actualGrid) {
        Point pawnToRemove = removePawn(model.getIdPlayer(), actualGrid);
        assert pawnToRemove != null;
        return new RemoveAction(model, board.getFirstElement(pawnToRemove.y, pawnToRemove.x));
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
     * @param playerId id du joueur qui place le pion
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
                    else if (pawn.getColor() == playerId) {
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