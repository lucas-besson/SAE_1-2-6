package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.RemoveAction;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class MerelleDecider extends Decider {

    MerelleStageModel stage;
    MerelleBoard board;
    MerellePawnPot aIpot;

    ActionList actions = new ActionList(true);
    Pawn pawnToMove;
    Point destPoint;

    Random rand = new Random();
    int[][] grid;
    int[][] secondGrid;

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
        stage = (MerelleStageModel) model.getGameStage();
        board = stage.getBoard();// get the board
    }

    static List<Point> getFreePoints(int[][] grid) {
        ArrayList<Point> lst = new ArrayList<>();
        for (int col = 0; col < grid.length; col++) {
            for (int row = 0; row < grid[col].length; row++) {
                if (MerelleBoard.isActiveCell(row, col) && grid[col][row] == 2)
                    lst.add(new Point(col, row));
            }
        }
        return lst;
    }

    static void main(String[] args) {
        int[][] previousGrid = new int[][]{
                {0, 2, 2, 1, 2, 2, 0},
                {2, 0, 2, 2, 2, 0, 2},
                {2, 2, 0, 1, 0, 2, 2},
                {1, 2, 1, 2, 1, 2, 1},
                {2, 2, 0, 1, 0, 2, 2},
                {2, 0, 2, 2, 2, 0, 2},
                {0, 2, 2, 1, 2, 2, 0}
        };

//        System.out.println(MerelleDecider.getFreePoints(previousGrid));
//        System.out.println(MerelleDecider.hasMill(1, 3, previousGrid, 1));
//        System.out.println(MerelleDecider.hasMill(3, 1, previousGrid, 1));
//        System.out.println(MerelleDecider.hasMill(3, 5, previousGrid, 1));
//        System.out.println(MerelleDecider.hasMill(5, 3, previousGrid, 1));
    }

    @Override
    public ActionList decide() {

        initGridTable();

        // Prends le bon pot de pions
        if (model.getIdPlayer() == Pawn.PAWN_BLACK) aIpot = stage.getBlackPot();
        else aIpot = stage.getRedPot();

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
    abstract void placePawn();

    /**
     * Dans la phase de déplacements des pions, analyse et déplace un pion du jeu
     */
    abstract void movePawn();

    /**
     * Algorithme qui vérifie le meilleur pion à supprimer (par ex. empecher l'autre joueur de finir un moulin...)
     */
    abstract Point removePawn(int[][] plateau);


    /**
     * Convertit la board en tableau 2D
     */
    void initGridTable() {
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
    List<Point> computeValidCells(Point point) {
        ArrayList<Point> lst = new ArrayList<>();
        int[][][] jumpTable = {{{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}, {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}}, {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}}, {{1, 3}, {1, 2}, {1, 1}, {}, {1, 1}, {1, 2}, {1, 3}}, {{}, {}, {1, 1}, {1, 1}, {1, 1}, {}, {}}, {{}, {2, 2}, {}, {2, 1}, {}, {2, 2}, {}}, {{3, 3}, {}, {}, {3, 1}, {}, {}, {3, 3}}};

        int jumpX = jumpTable[point.x][point.y][0];
        int jumpY = jumpTable[point.x][point.y][1];

        int[][] offsetTable = {{0, -jumpY}, {-jumpX, 0}, {jumpX, 0}, {0, jumpY}};

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
     * Selectionne le pion suivant dans le pot de pions
     *
     * @return Pion suivant
     */
    Pawn selectNextInPot() {
        int i = 0;
        while (i < MerellePawnPot.PAWNS_IN_POT && aIpot.getElement(i, 0) == null) i++;
        return (Pawn) aIpot.getElement(i, 0);
    }

    /**
     * Construit une liste de points des Pawn du joueur passé en paramètres.
     *
     * @param playerColor Couleur du joueur à récuperer les pions
     * @param grid        Tableau 2D de la grille actuelle (2: null or not active)
     * @return Liste de pions du joueur (Point(x, y))
     */
    List<Point> getPlayerPawnList(int playerColor, int[][] grid) {
        List<Point> playerPawnList = new ArrayList<>();

        for (int col = 0; col < grid.length; col++) {
            for (int row = 0; row < grid[col].length; row++) {
                if (grid[col][row] == playerColor) playerPawnList.add(new Point(col, row));
            }
        }
        return playerPawnList;
    }


    /**
     * Retourne le vainqueur du tour, 2 si aucun vainqueur
     *
     * @param actualGrid grille à vérifier
     * @return idPlayer that wins
     */
    int checkWinner(int[][] actualGrid) {
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
    boolean isNewMill(int[][] previousGrid, int[][] actualGrid, int playerColor) {
        for (int[][] mill : MerelleBoard.MILLS) {
            int x1 = mill[0][0];
            int y1 = mill[0][1];
            int x2 = mill[1][0];
            int y2 = mill[1][1];
            int x3 = mill[2][0];
            int y3 = mill[2][1];

            // Vérifier si les 3 positions du mill sont occupées par le joueur actuel
            if (actualGrid[x1][y1] == actualGrid[x2][y2] && actualGrid[x2][y2] == actualGrid[x3][y3] && actualGrid[x1][y1] == playerColor) {

                // Vérifier si le joueur n'occupait pas les 3 positions du mill avant
                if (previousGrid[x1][y1] != actualGrid[x1][y1] || previousGrid[x2][y2] != actualGrid[x2][y2] || previousGrid[x3][y3] != actualGrid[x3][y3])
                    return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si en placant le pion en (x,y)
     *
     * @param x
     * @param y
     * @param plateau
     * @return
     */
    boolean canMakeMill(int x, int y, int[][] plateau) {
        int joueur = plateau[x][y];
        for (int i = 0; i < 3; i++) {
            if (plateau[x][(y + i) % 3] != joueur) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (plateau[(x + i) % 3][y] != joueur) {
                break;
            }
            if (i == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode pour compter le nombre de moulins pour un pion donné
     *
     * @param x
     * @param y
     * @param plateau
     * @return
     */
    int millsCount(int x, int y, int[][] plateau) {
        int moulins = 0;
        int joueur = plateau[x][y];
        if (joueur == 0) {
            return 0;
        }
        // Vérifie les moulins horizontaux et verticaux
        if (plateau[x][(y + 1) % 3] == joueur && plateau[x][(y + 2) % 3] == joueur) {
            moulins++;
        }
        if (plateau[(x + 1) % 3][y] == joueur && plateau[(x + 2) % 3][y] == joueur) {
            moulins++;
        }
        // Vérifie les moulins diagonaux
        if ((x == 0 && y == 0) || (x == 1 && y == 1) || (x == 2 && y == 2)) {
            if (plateau[(x + 1) % 3][(y + 1) % 3] == joueur && plateau[(x + 2) % 3][(y + 2) % 3] == joueur) {
                moulins++;
            }
        }
        if ((x == 0 && y == 2) || (x == 1 && y == 1) || (x == 2 && y == 0)) {
            if (plateau[(x + 1) % 3][(y + 2) % 3] == joueur && plateau[(x + 2) % 3][(y + 1) % 3] == joueur) {
                moulins++;
            }
        }
        return moulins;
    }

    /**
     * TODO: Doit retourner le nombre de pions adverse pour une liste donnée en entrée
     * @return
     */
//    private int nb_pawn_op(ArrayList<Point> positions, int[][] actualGrid) {
//        int pawnNumber = 0;
//        for(Point position : positions) {
//            if(board.getFirstElement(position.getLocation().x, position.getLocation().y).)
//        }
//        return pawnNumber;
//    }

    /**
     * Retourne le meilleure RemoveAction à effectuer pour la grille actuelle.
     *
     * @param actualGrid
     * @return RemoveAction à effectuer par le joueur actuel
     */
    RemoveAction removePawnAction(int[][] actualGrid) {
        Point pawnToRemove = removePawn(actualGrid);
        assert pawnToRemove != null;
        return new RemoveAction(model, board.getFirstElement(pawnToRemove.y, pawnToRemove.x));
    }

//    /**
//     * TODO: Doit retourner Vrai si un moulin est faisable à la position entré en paramètre sinon False
//     *
//     * @param position
//     * @param actualGrid
//     * @return
//     */
//    boolean getPossibleMills(Point position, int[][] actualGrid) {
//        return false;
//    }

    /**
     * Analyse les moulins qui sont prêts à etre validés (dont il reste qu'un pion pour valider le moulin)
     *
     * @param couleurJoueur Couleur du joueur a analyser les moulins
     * @param grid
     * @return Liste des cases à remplir pour faire un moulin
     */
    List<Point> getUncompletedMillsForPlayer(int couleurJoueur, int[][] grid) {
        List<Point> emptyCellsForMill = new ArrayList<>();

        if (model.getIdPlayer() == 1)
            return emptyCellsForMill;

        // Pour chaque case vide
        for (Point caseVide : getFreePoints(grid)) {
            // Si en placant un pion de la couleur indiquée on fait un moulin, on ajoute le pion à la liste
            if (hasMill(caseVide.x, caseVide.y, grid, couleurJoueur))
                emptyCellsForMill.add(caseVide);
        }

        return emptyCellsForMill;
    }

    /**
     * Vérifie si en plaçant le pion en (col, row) on crée un moulin
     *
     * @param col      position col du pion à verifier
     * @param row      position y du pion à verifier
     * @param grid     etat actuel du plateau
     * @param playerId id du joueur qui place le pion
     * @return vrai si avec cette combinaison (col, y) un moulin sera créé
     */
    boolean hasMill(int col, int row, int[][] grid, int playerId) {
        for (int[][] mill : MerelleBoard.MILLS) {
            if (contains(mill, new int[]{col, row})) {
                int count = 1;
                for (int[] position : mill) {
                    int pawnX = position[0];
                    int pawnY = position[1];
                    if (grid[pawnX][pawnY] == playerId)
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
     * @param millToCheck moulin à vérifier
     * @param position    position à vérifier
     * @return vrai si la position est contenue dans le moulin
     */
    boolean contains(int[][] millToCheck, int[] position) {
        for (int[] millPoint : millToCheck) {
            if (millPoint[0] == position[0] && millPoint[1] == position[1])
                return true;
        }
        return false;
    }
}


