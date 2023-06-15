package controller;

import boardifier.control.Controller;
import boardifier.model.Model;
import control.BasicDecider;
import model.MerelleBoard;
import model.MerelleStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;

class BasicDeciderUnitTest {
    BasicDecider basicDeciderTest;
    Model model;
    Controller controller;
    MerelleStageModel merelleStageModel;
    MerelleBoard merelleBoard;
    Random randomMock;

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
    void testPlacePawn() {
        java.util.List<Point> playablePawns = Arrays.asList(new Point(1,1), new Point(1,2), new Point(1,3));
        Point selectedPawn = playablePawns.get(randomMock.nextInt(playablePawns.size()));

        assertEquals(playablePawns.get(0), selectedPawn);
    }

    @Test
    void testMovePawn() {
        int[][] gridOld = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };

        int[][] gridNew = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        assertNotEquals(gridOld, gridNew);
    }

    @Test
    void testRemovePawn() {
        int[][] gridOld = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        assertNotEquals(0, gridOld[3][2]);

        int[][] gridNew = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        assertEquals(0, gridNew[3][2]);
    }
}
