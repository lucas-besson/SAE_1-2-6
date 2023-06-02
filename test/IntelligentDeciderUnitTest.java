import boardifier.control.Controller;
import boardifier.model.Model;
import control.IntelligentDecider;
import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class IntelligentDeciderUnitTest {

    private Controller controller;
    private Model model;
    IntelligentDecider merelleIntelligentDeciderTest;

    @BeforeEach
    public void initEach() {
        controller = Mockito.mock(Controller.class);
        model = Mockito.mock(Model.class);

        merelleIntelligentDeciderTest = new IntelligentDecider(model, controller);
    }
}
