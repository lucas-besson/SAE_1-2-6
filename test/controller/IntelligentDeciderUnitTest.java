package controller;

import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.view.GridLook;
import boardifier.view.View;
import control.IntelligentDecider;
import control.MerelleController;
import control.MerelleDecider;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IntelligentDeciderUnitTest {

    private IntelligentDecider intelligentDecider;
    private Model model;
    private View view;
    private MerelleController merelleController;
    private MerelleStageModel merelleStageModel;
    private MerelleBoard merelleBoard;
    private MerellePawnPot pot;

    @BeforeEach
    void initEach() {
        model = mock(Model.class);
        merelleStageModel = new MerelleStageModel("test", model);
        merelleBoard = new MerelleBoard(0, 0, merelleStageModel);

        pot = new MerellePawnPot(0,0, merelleStageModel);

        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            pot.putElement(new Pawn(1, 0, merelleStageModel), i, 0);
        }

        merelleStageModel.setBlackPot(pot);

        merelleStageModel.setBoard(merelleBoard);
        when(model.getGrid(anyString())).thenReturn(merelleBoard);
        when(model.getGameStage()).thenReturn(merelleStageModel);
        view = mock(View.class);
        merelleController = mock(MerelleController.class);
        intelligentDecider = Mockito.spy(new IntelligentDecider(model, merelleController));
        GridLook gridLook = mock(GridLook.class);
        when(merelleController.getElementLook(any())).thenReturn(gridLook);
        Coord2D coord2D = mock(Coord2D.class);
        when(gridLook.getRootPaneLocationForCellCenter(anyInt(),anyInt())).thenReturn(coord2D);

        model.setIdPlayer(Pawn.PAWN_BLACK);
    }

    @Test
    void testMovePawn() throws Exception {

        /**
         * Tester le mouvement qu'exécute l'IA dans un cas bien précis.
         */

        int[][] grid = {
                {2, 2, 2, 2, 2, 2, 0},
                {2, 1, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2},
                {1, 2, 2, 2, 0, 0, 2},
                {2, 2, 2, 2, 2, 2, 2},
                {2, 1, 2, 2, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                merelleBoard.putElement(new Pawn(1, grid[row][col], merelleStageModel), row, col);
            }
        }

        intelligentDecider.movePawn();
        var lastGrid = intelligentDecider.getGrid();

        //MerelleDecider.printGrid(lastGrid);

        grid[3][6] = 0;
        grid[6][6] = 2;

        Assertions.assertEquals(lastGrid[6][3], 0);
        verify(intelligentDecider, times(1)).movePawn();
        verify(intelligentDecider, times(8)).minimax(any(int[][].class), any(int[][].class), anyBoolean(), anyInt());
    }

    @Test
    void testPlacePawn() throws Exception {

        int[][] grid = {
                {1, 2, 2, 1, 2, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 2, 2, 0},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                merelleBoard.putElement(new Pawn(1, grid[row][col], merelleStageModel), row, col);
            }
        }

        // Call placePawn() method indirectly.
        /* The player is defined just above as
           being black and as the black pot is
           full the method decide() will choose
           to execute the method placePawn. */

        intelligentDecider.decide();

        var lastGrid = intelligentDecider.getGrid();

        Assertions.assertFalse(Arrays.deepEquals(grid, lastGrid));

        verify(intelligentDecider, times(1)).placePawn();
        verify(intelligentDecider, times(0)).movePawn();
    }

    @Test
    public void testMinimax() throws Exception {

        int[][] previousGrid = {
                {1, 2, 2, 1, 2, 2, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 1, 1, 1},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 2, 0, 2, 2, 2}
        };

        int[][] actualGrid = {
                {1, 2, 2, 1, 2, 2, 1},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 1, 1, 1},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {1, 2, 2, 2, 2, 2, 0}
        };

        int score = intelligentDecider.minimax(previousGrid, actualGrid, true, 0);
        Assertions.assertTrue(score > 0);
    }

    @Test
    public void testRemovePawn() throws Exception {

        int[][] previousGgrid = {
                {1, 2, 2, 1, 2, 2, 1},
                {2, 0, 2, 0, 2, 1, 2},
                {2, 2, 2, 1, 1, 2, 2},
                {1, 0, 2, 2, 2, 1, 2},
                {2, 2, 2, 2, 2, 2, 2},
                {2, 0, 2, 2, 2, 2, 2},
                {1, 2, 2, 2, 2, 2, 2}
        };

        Point point = intelligentDecider.removePawn(previousGgrid);

        System.out.println(point.x);
        System.out.println(point.y);

        Assertions.assertTrue(point.x == 0);
        Assertions.assertTrue(point.y == 0);
    }
}