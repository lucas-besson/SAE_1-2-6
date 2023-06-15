import boardifier.control.Controller;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import boardifier.view.GridLook;
import boardifier.view.View;
import control.BasicDecider;
import control.MerelleDecider;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class BasicDeciderUnitTest {
    BasicDecider basicDeciderTest;
    Model model;
    Controller controller;
    View view;
    MerelleStageModel merelleStageModel;
    MerelleBoard merelleBoard;
    Random randomMock;
    GameElement gameElement;
    MerellePawnPot aiPot;
    @BeforeEach
    void initEach() {
        model = mock(Model.class);
        controller = mock(Controller.class);
        view = mock(View.class);
        merelleStageModel = new MerelleStageModel("test", model);
        merelleBoard = new MerelleBoard(0, 0, merelleStageModel);
        randomMock = mock(Random.class);

        aiPot = new MerellePawnPot(0,0, merelleStageModel);
        for (int i = 0; i < MerellePawnPot.PAWNS_IN_POT; i++) {
            aiPot.putElement(new Pawn(1, 0, merelleStageModel), i, 0);
        }
        merelleStageModel.setBlackPot(aiPot);

        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        Mockito.when(model.getGrid(anyString())).thenReturn(merelleBoard);

        merelleStageModel.setBlackPot(aiPot);

        merelleStageModel.setBoard(merelleBoard);
        basicDeciderTest = Mockito.spy(new BasicDecider(model, controller));
        GridLook gridLook = mock(GridLook.class);
        when(controller.getElementLook(any())).thenReturn(gridLook);
        Coord2D coord2D = mock(Coord2D.class);
        when(gridLook.getRootPaneLocationForCellCenter(anyInt(),anyInt())).thenReturn(coord2D);
    }
    @Test
    void testPlacePawn() {

        int[][] oldGrid = {
                {1, 2, 2, 1, 2, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 2, 2, 0},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };
        for (int row = 0; row < oldGrid.length; row++) {
            for (int col = 0; col < oldGrid.length; col++) {
                merelleBoard.putElement(new Pawn(1, oldGrid[row][col], merelleStageModel), row, col);
            }
        }

        model.setIdPlayer(Pawn.PAWN_BLACK);

        MerelleDecider.printGrid(oldGrid);

        basicDeciderTest.decide();

        var newGrid = basicDeciderTest.getGrid();
        MerelleDecider.printGrid(newGrid);

        Assertions.assertFalse(Arrays.deepEquals(oldGrid, newGrid));

        verify(basicDeciderTest, times(1)).placePawn();
        verify(basicDeciderTest, times(0)).movePawn();

    }

    @Test
    void testMovePawn() {
        int[][] oldGrid = {
                {1, 2, 2, 1, 2, 2, 0},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 0, 0, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };
        for (int row = 0; row < oldGrid.length; row++) {
            for (int col = 0; col < oldGrid.length; col++) {
                merelleBoard.putElement(new Pawn(1, oldGrid[row][col], merelleStageModel), row, col);
            }
        }
        MerelleDecider.printGrid(oldGrid);

        model.setIdPlayer(Pawn.PAWN_BLACK);

        basicDeciderTest.movePawn();

        var newGrid = basicDeciderTest.getGrid();
        MerelleDecider.printGrid(newGrid);

        oldGrid[3][6] = 0;
        oldGrid[6][6] = 2;

        assertFalse(Arrays.deepEquals(oldGrid, newGrid));
        verify(basicDeciderTest, times(1)).movePawn();
        verify(basicDeciderTest, times(0)).placePawn();

    }

    @Test
    void testRemovePawn() {
        int[][] oldGrid = {
                {1, 2, 2, 1, 2, 2, 0},
                {2, 1, 2, 1, 2, 1, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {1, 1, 1, 2, 0, 0, 2},
                {2, 2, 1, 1, 1, 2, 2},
                {2, 1, 2, 1, 2, 1, 2},
                {0, 2, 2, 2, 2, 2, 2}
        };
        for (int row = 0; row < oldGrid.length; row++) {
            for (int col = 0; col < oldGrid.length; col++) {
                merelleBoard.putElement(new Pawn(1, oldGrid[row][col], merelleStageModel), row, col);
            }
        }

        basicDeciderTest.removePawn(oldGrid);

        var newGrid = basicDeciderTest.getGrid();

        assertFalse(Arrays.deepEquals(oldGrid, newGrid));
        verify(basicDeciderTest, times(1)).removePawn(oldGrid);
    }
}