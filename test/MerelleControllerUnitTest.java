import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import control.MerelleController;
import control.MerelleDecider;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.sound.sampled.Line;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;

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

    @Test
    public void testAnalyseAndPlay() {
//        Player player = Mockito.mock(Player.class);
        MerelleStageModel merelleStageModel = Mockito.mock(MerelleStageModel.class);
        MerellePawnPot merellePawnPot = Mockito.mock(MerellePawnPot.class);
        MerelleBoard merelleBoard = Mockito.mock(MerelleBoard.class);
        Pawn pawn = Mockito.mock(Pawn.class);
        Model model = Mockito.mock(Model.class);
        View view = Mockito.mock(View.class);
        MerelleController merelleController = new MerelleController(model,view);
        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        Mockito.when(merellePawnPot.getElement(Mockito.anyInt(),Mockito.anyInt())).thenReturn(pawn);
        Mockito.when(merelleStageModel.getBlackPot()).thenReturn(merellePawnPot);
        Mockito.when(merelleStageModel.getRedPot()).thenReturn(merellePawnPot);
        Mockito.when(merelleStageModel.getBoard()).thenReturn(merelleBoard);

//        Pawn index to small
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("0A1"));

//        The cell is not actif
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("1A2"));

//        The BlackPot is empty for pawn N x
        Mockito.when(model.getIdPlayer()).thenReturn(0);
        Mockito.when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);
        Mockito.when(merellePawnPot.isEmptyAt(Mockito.anyInt(),Mockito.anyInt())).thenReturn(true);
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("1A1"));

//        The RedPot is not empty for pawn but can not reach the cell
        Mockito.when(model.getIdPlayer()).thenReturn(1);
        Mockito.when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);
        Mockito.when(merellePawnPot.isEmptyAt(Mockito.anyInt(),Mockito.anyInt())).thenReturn(false);
        Mockito.when(merelleBoard.canReachCell(Mockito.anyInt(),Mockito.anyInt())).thenReturn(false);
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("1A1"));

//        Status 2 : the pawn can not be found
        Mockito.when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.MOVING);
        Mockito.when(merelleBoard.getPawn(Mockito.anyInt(),Mockito.anyInt())).thenReturn(null);
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("1A1"));

//        Status 2 : the pawn has been found
        Mockito.when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.MOVING);
        Mockito.when(merelleBoard.getPawn(Mockito.anyInt(),Mockito.anyInt())).thenReturn(pawn);
        Assertions.assertFalse(merelleController.AccesAnalyseAndPlay("1A1"));

//        The move is possible
        Mockito.when(model.getIdPlayer()).thenReturn(0);
        Mockito.when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);

        Mockito.when(merelleBoard.canReachCell(Mockito.anyInt(),Mockito.anyInt())).thenReturn(true);
        Assertions.assertTrue(merelleController.AccesAnalyseAndPlay("1A1"));
    }
}
