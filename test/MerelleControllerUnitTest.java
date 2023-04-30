import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import control.MerelleController;
import control.MerelleDecider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sound.sampled.Line;

import static org.mockito.Mockito.mock;

public class MerelleControllerUnitTest {
    // TO FINISH
    @Test
    public void teststageLoop(){
        GameStageModel gameStageModel = Mockito.mock(GameStageModel.class);
        View view = Mockito.mock(View.class);
        Model model = Mockito.mock(Model.class);
        Mockito.when(model.getGameStage()).thenReturn(gameStageModel);
        Mockito.when(gameStageModel.getModel()).thenReturn(model);
        Mockito.when(model.getGameStage().getModel().isEndStage()).thenReturn(true);
        MerelleController merelleController = new MerelleController(model,view);
        //Mockito.verify(merelleController).nextPlayer();
        //Mockito.verify(merelleController).update();
        //Mockito.verify(merelleController).stopStage();
        //Mockito.verify(merelleController).endGame();
    }
    // TO FINISH
    @Test
    public void testnextPlayer(){
        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getType()).thenReturn(1);
        String line = Mockito.mock(String.class);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(line.equalsIgnoreCase("stop")).thenReturn(true);
        Mockito.verify(controller).endGame();
    }
}
