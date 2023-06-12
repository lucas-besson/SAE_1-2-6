import boardifier.control.Controller;
import boardifier.model.Model;
import control.IntelligentDecider;
import model.MerelleBoard;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import java.util.Random;

public class IntelligentDeciderUnitTest {

    private Controller controller;
    private Model model;
    IntelligentDecider merelleIntelligentDeciderTest;
    MerelleBoard mockBoard;

    @BeforeEach
    public void initEach() {
        controller = Mockito.mock(Controller.class);
        model = Mockito.mock(Model.class);

        merelleIntelligentDeciderTest = new IntelligentDecider(model, controller);
        mockBoard = Mockito.mock(MerelleBoard.class);
    }
}
