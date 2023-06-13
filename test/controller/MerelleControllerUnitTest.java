package controller;

import boardifier.model.Model;
import boardifier.view.View;
import control.MerelleController;
import model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MerelleControllerUnitTest {

    // stageLoop methode based on boardifier method and Merelle methode that have already been tested.
    /*
    @Test
    public void teststageLoop() throws GameException {
        Model model = Mockito.mock(Model.class);
        View view = Mockito.mock(View.class);
        MerelleStageModel gameStageModel = Mockito.mock(MerelleStageModel.class);
        MerelleController merelleController = new MerelleController(model,view);
        Mockito.when(model.getGameStage()).thenReturn(gameStageModel);
        Mockito.when(gameStageModel.isEndStage()).thenReturn(false);
        merelleController.setFirstStageName("test");
        merelleController.startGame();
        merelleController.stageLoop();



    }
    */

    // TO FINISH
    @Test
    public void testnextPlayer() {

//        V1
//        Player player = Mockito.mock(Player.class);
//        Mockito.when(player.getType()).thenReturn(1);
//        String line = Mockito.mock(String.class);
//        Controller controller = Mockito.mock(Controller.class);
//        Mockito.when(line.equalsIgnoreCase("stop")).thenReturn(true);
//        Mockito.verify(controller).endGame();

//        V2
//        Player player = Mockito.mock(Player.class);
//        Model model = Mockito.mock(Model.class);
//        Mockito.when(model.getCurrentPlayer()).thenReturn(player);
//        View view = Mockito.mock(View.class);
//        MerelleController merelleController = new MerelleController(model,view);
//        merelleController.nextPlayer();
    }
}
