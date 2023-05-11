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

    @Test
    public void testAnalyseAndPlay() {
//        Player player = Mockito.mock(Player.class);
        MerelleStageModel merelleStageModel = mock(MerelleStageModel.class);
        MerellePawnPot merellePawnPot = mock(MerellePawnPot.class);
        MerelleBoard merelleBoard = mock(MerelleBoard.class);
        Pawn pawn = mock(Pawn.class);
        Model model = mock(Model.class);
        View view = mock(View.class);
        MerelleController merelleController = new MerelleController(model, view);
        when(model.getGameStage()).thenReturn(merelleStageModel);
        when(merellePawnPot.getElement(anyInt(), anyInt())).thenReturn(pawn);
        when(merelleStageModel.getBlackPot()).thenReturn(merellePawnPot);
        when(merelleStageModel.getRedPot()).thenReturn(merellePawnPot);
        when(merelleStageModel.getBoard()).thenReturn(merelleBoard);

//        Pawn index to small
        assertFalse(merelleController.AccessAnalyseAndPlay("0A1"));

//        The cell is not actif
        assertFalse(merelleController.AccessAnalyseAndPlay("1A2"));

//        The BlackPot is empty for pawn N x
        when(model.getIdPlayer()).thenReturn(0);
        when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);
        when(merellePawnPot.isEmptyAt(anyInt(), anyInt())).thenReturn(true);
        assertFalse(merelleController.AccessAnalyseAndPlay("1A1"));

//        The RedPot is not empty for pawn but can not reach the cell
        when(model.getIdPlayer()).thenReturn(1);
        when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);
        when(merellePawnPot.isEmptyAt(anyInt(), anyInt())).thenReturn(false);
        when(merelleBoard.canReachCell(anyInt(), anyInt())).thenReturn(false);
        assertFalse(merelleController.AccessAnalyseAndPlay("1A1"));

//        Status 2 : the pawn can not be found
        when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.MOVING);
        when(merelleBoard.getPawn(anyInt(), anyInt())).thenReturn(null);
        assertFalse(merelleController.AccessAnalyseAndPlay("1A1"));

//        Status 2 : the pawn has been found
        when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.MOVING);
        when(merelleBoard.getPawn(anyInt(), anyInt())).thenReturn(pawn);
        assertFalse(merelleController.AccessAnalyseAndPlay("1A1"));

//        The move is possible
        when(model.getIdPlayer()).thenReturn(0);
        when(merelleStageModel.getStatus()).thenReturn(MerelleGameStatus.PLACING);

        when(merelleBoard.canReachCell(anyInt(), anyInt())).thenReturn(true);
        assertTrue(merelleController.AccessAnalyseAndPlay("1A1"));
    }

    @Test
    public void testMillAnalyseAndPlay() {
//        Player player = Mockito.mock(Player.class);
        MerelleStageModel merelleStageModel = mock(MerelleStageModel.class);
        MerellePawnPot merellePawnPot = mock(MerellePawnPot.class);
        MerelleBoard merelleBoard = mock(MerelleBoard.class);
        Pawn pawn = mock(Pawn.class);
        Model model = mock(Model.class);
        View view = mock(View.class);
        MerelleController merelleController = new MerelleController(model, view);
        when(model.getGameStage()).thenReturn(merelleStageModel);
        when(merellePawnPot.getElement(anyInt(), anyInt())).thenReturn(pawn);
        when(merelleStageModel.getBlackPot()).thenReturn(merellePawnPot);
        when(merelleStageModel.getRedPot()).thenReturn(merellePawnPot);
        when(merelleStageModel.getBoard()).thenReturn(merelleBoard);

//        The pawn has a non-valid number
        assertFalse(merelleController.AccessMillAnalyseAndPlay("0"));

//        The pawn number correspond to an unexciting pawn
        when(merelleBoard.getPawn(anyInt(), anyInt())).thenReturn(null);
        assertFalse(merelleController.AccessMillAnalyseAndPlay(("1")));

//        The pawn has been recovered, but can not be removed for this mill action
        when(merelleBoard.getPawn(anyInt(), anyInt())).thenReturn(pawn);
        when(merelleBoard.canReachCell(anyInt(), anyInt())).thenReturn(false);
        assertFalse(merelleController.AccessMillAnalyseAndPlay(("1")));

//        The pawn has been recovered, and the player can remove it for this mill action.
        when(merelleBoard.getPawn(anyInt(), anyInt())).thenReturn(pawn);
        when(merelleBoard.canReachCell(anyInt(), anyInt())).thenReturn(true);
        assertTrue(merelleController.AccessMillAnalyseAndPlay(("1")));
    }

}
