import boardifier.control.Controller;
import boardifier.model.Model;
import control.MerelleDecider;
import model.MerelleBoard;
import model.MerelleStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MerelleDeciderUnitTest {
    @Test
    void testGetFreePoints() {
        Controller controller = Mockito.mock(Controller.class);
        Model model = Mockito.mock(Model.class);
        MerelleStageModel merelleStageModel = Mockito.mock(MerelleStageModel.class);
        MerelleBoard merelleBoard = Mockito.mock(MerelleBoard.class);
        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        Mockito.when(merelleStageModel.getBoard()).thenReturn(merelleBoard);
        int[][] grid = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 2, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0}
        };
        MerelleDecider merelleDecider = new MerelleDecider(model,controller) {
            @Override
            protected void placePawn() {

            }
            @Override
            protected void movePawn() {

            }

            @Override
            protected Point removePawn(int[][] actualGrid) {
                return null;
            }

            @Override
            public List<Point> getFreePoints(int[][] grid) {
                return super.getFreePoints(grid);
            }
        };

        Assertions.assertEquals(merelleDecider.getFreePoints(grid).size(),3);

    }

}
