import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.GridElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import control.MerelleDecider;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;

public class MerelleDeciderUnitTest {
    private Model model;
    private Controller controller;
    private MerelleStageModel merelleStageModel;
    private MerelleBoard merelleBoard;
    private MerelleDecider merelleDeciderTest;

    @BeforeEach
    void initEach(){
        controller = Mockito.mock(Controller.class);
        model = Mockito.mock(Model.class);
        merelleStageModel = Mockito.mock(MerelleStageModel.class);
        merelleBoard = Mockito.mock(MerelleBoard.class);
        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        Mockito.when(model.getGrid(anyString())).thenReturn(merelleBoard);
        merelleDeciderTest = new MerelleDecider(model, controller) {
            public int test=1;

            public int getTest() {
                return test;
            }

            @Override
            public void placePawn() {
                test=2;
            }
            @Override
            protected void movePawn() {
                test=3;
            }
            public List<Point> getFreePoints(int[][] actualGrid) {
                return super.getFreePoints(actualGrid);
            }
            @Override
            public Point removePawn(int[][] actualGrid) {
                return null;
            }

            @Override
            public ActionList decide() {
                return super.decide();
            }


        };
    }
    @Test
    void testGetFreePoints() {
        int[][] grid = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };

        Assertions.assertEquals(merelleDeciderTest.getFreePoints(grid).size(),3);
    }

    @Test
    void testDecide() {
        GridElement gridElement = Mockito.mock(GridElement.class);
        Pawn gameElement = Mockito.mock(Pawn.class);
        MerellePawnPot aIPot = Mockito.mock(MerellePawnPot.class);
        List<GameElement> gameElements = Mockito.mock(List.class);

        Mockito.when(merelleStageModel.getStatus()).thenReturn(1);
        Mockito.when(aIPot.getFirstElement(Mockito.anyInt(),Mockito.anyInt())).thenReturn(gameElement);
        Mockito.when(gridElement.getElements(Mockito.anyInt(),Mockito.anyInt())).thenReturn( gameElements);
        merelleDeciderTest.decide();
        Assertions.assertEquals(2, merelleDeciderTest.getTest());


        Mockito.when(merelleStageModel.getStatus()).thenReturn(2);
        Mockito.when(aIPot.getFirstElement(Mockito.anyInt(),Mockito.anyInt())).thenReturn(gameElement);
        Mockito.when(gridElement.getElements(Mockito.anyInt(),Mockito.anyInt())).thenReturn( gameElements);
        merelleDeciderTest.decide();
        Assertions.assertEquals(3, merelleDeciderTest.getTest());
    }

}
