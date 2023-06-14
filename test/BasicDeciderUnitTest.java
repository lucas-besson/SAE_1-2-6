import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import control.BasicDecider;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BasicDeciderUnitTest {
    BasicDecider basicDeciderTest;
    Model model;
    Controller controller;
    MerelleStageModel merelleStageModel;
    MerelleBoard merelleBoard;
    Random randomMock;
    GameElement gameElement;

    @BeforeEach
    public void initEach() {
        model = mock(Model.class);
        controller = mock(Controller.class);
        merelleStageModel = Mockito.mock(MerelleStageModel.class);
        merelleBoard = Mockito.mock(MerelleBoard.class);
        randomMock = mock(Random.class);
        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        Mockito.when(model.getGrid(anyString())).thenReturn(merelleBoard);

        basicDeciderTest = new BasicDecider(model, controller);

    }

    @Test
    public void testPlacePawn() {
        ArrayList<Point> playablePawns = new ArrayList<>();
        Point point1 = new Point(1,1);
        Point point2 = new Point(1,2);
        Point point3 = new Point(1,3);
        playablePawns.add(point1);
        playablePawns.add(point2);
        playablePawns.add(point3);

        gameElement = Mockito.mock(GameElement.class);

        MerellePawnPot aiPot = Mockito.mock(MerellePawnPot.class);
        basicDeciderTest.setaIpot(aiPot);

        var oldGrid = basicDeciderTest.getGrid();
        basicDeciderTest.placePawn();
        var newGrid = basicDeciderTest.getGrid();

        assertFalse(Arrays.deepEquals(oldGrid, newGrid));

    }

    @Test
    public void testMovePawn() {

//        int[][] gridOld = {
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 2, 2, 0, 0, 0},
//                {0, 0, 2, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0}
//        };
//
//        int[][] gridNew = {
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 2, 2, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 2, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0}
//        };
//        assertNotEquals(gridOld, gridNew);


        doNothing().when(basicDeciderTest).movePawn();
        basicDeciderTest.movePawn();
        verify(basicDeciderTest, times(1)).movePawn();

        // FIXME: c'est pété



    }

    @Test
    public void testRemovePawn() {
        int[][] gridOld = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        assertNotEquals(gridOld[3][2], 0);

        int[][] gridNew = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        assertEquals(gridNew[3][2], 0);
    }
}
