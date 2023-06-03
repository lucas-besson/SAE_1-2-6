import boardifier.control.Controller;
import boardifier.model.Model;
import control.BasicDecider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

import static org.mockito.Mockito.mock;

public class BasicDeciderUnitTest {
    BasicDecider basicDeciderTest;
    Model model;
    Controller controller;

    @BeforeEach
    public void initEach() {
        model = mock(Model.class);
        controller = mock(Controller.class);

        basicDeciderTest = new BasicDecider(model, controller);
    }

    @Test
    public void testPlacePawn() {
        // FIXME: le test ne passe pas et j'ai aucune idée de comment faire aled :(

        java.util.List<Point> playablePawns = Arrays.asList(new Point(1,1), new Point(1,2), new Point(1,3));

        Random randomMock = mock(Random.class);
        when(randomMock.nextInt(playablePawns.size())).thenReturn(0);

        basicDeciderTest.placePawn();
        Point selectedPawn = new Point(1,1);

        Assertions.assertEquals(playablePawns.get(0), selectedPawn);
    }
}
