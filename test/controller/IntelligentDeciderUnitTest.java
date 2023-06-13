package controller;

import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.view.GridLook;
import boardifier.view.RootPane;
import boardifier.view.View;
import control.IntelligentDecider;
import control.MerelleController;
import javafx.stage.Stage;
import model.MerelleBoard;
import model.MerelleStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntelligentDeciderUnitTest {

    private IntelligentDecider intelligentDecider;
    private Model model;
    private View view;
    private MerelleController merelleController;
    private MerelleStageModel merelleStageModel;
    private MerelleBoard merelleBoard;

    @BeforeEach
    public void initEach() {
//        controller = mock(Controller.class);
//        model = new Model();
//        pawnToMove = mock(Pawn.class);
//        gridElement = mock(GridElement.class);
//        merelleStageModel = new MerelleStageModel("test", model);
//        merelleBoard = new MerelleBoard(7, 7, merelleStageModel);
//        gameElement = mock(GameElement.class);
//        merelleDecider = mock(MerelleDecider.class);
//        model.setGameStage(merelleStageModel);
//        merelleStageModel.setBoard(merelleBoard);
////        when(model.getGrid(anyString())).thenReturn(merelleBoard);
//
//        intelligentDecider = new IntelligentDecider(model, controller);
//
//        pawn = mock(Pawn.class);

        model = mock(Model.class);
        merelleStageModel = new MerelleStageModel("test", model);
        merelleBoard = new MerelleBoard(0, 0, merelleStageModel);
        merelleStageModel.setBoard(merelleBoard);
        when(model.getGrid(anyString())).thenReturn(merelleBoard);
        when(model.getGameStage()).thenReturn(merelleStageModel);
        view = mock(View.class);
        merelleController = mock(MerelleController.class);
        intelligentDecider = new IntelligentDecider(model, merelleController);
        GridLook gridLook = mock(GridLook.class);
        when(merelleController.getElementLook(any())).thenReturn(gridLook);
        Coord2D coord2D = mock(Coord2D.class);
        when(gridLook.getRootPaneLocationForCellCenter(anyInt(),anyInt())).thenReturn(coord2D);
    }

    @Test
    public void testMovePawn() throws Exception {

        /**
         * Tester le mouvement qu'exécute l'IA dans un cas bien précis.
         */

        int[][] grid = {
                {1, 2, 2, 1, 2, 2, 0},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 0, 0, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                merelleBoard.putElement(new Pawn(1, grid[row][col], merelleStageModel), row, col);
            }
        }
        model.setIdPlayer(Pawn.PAWN_BLACK);

        intelligentDecider.movePawn();
        var lastGrid = intelligentDecider.getGrid();

        grid[3][6] = 0;
        grid[6][6] = 2;

        Assertions.assertArrayEquals(grid, lastGrid);
    }
}