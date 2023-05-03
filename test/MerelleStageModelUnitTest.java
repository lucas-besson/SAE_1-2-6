import boardifier.model.Model;
import boardifier.model.Player;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import view.PawnPotLook;

public class MerelleStageModelUnitTest {

    @Test
    public void testisEndStage() {
        MerelleBoard merelleBoard = Mockito.mock(MerelleBoard.class);
        Model model = Mockito.mock(Model.class);
        MerelleStageModel merelleStageModel = new MerelleStageModel("test",model);
        MerellePawnPot merellePawnPot = Mockito.mock(MerellePawnPot.class);
        //Mockito.when(model).thenReturn(merelleStageModel);
        Mockito.when(model.getGameStage()).thenReturn(merelleStageModel);
        merelleStageModel.setBoard(merelleBoard);
        merelleStageModel.setBlackPot(merellePawnPot);
        merelleStageModel.isEndStage();

        //Premier if :
        Mockito.when(model.getIdPlayer()).thenReturn(3);
        Mockito.when(merelleBoard.availableMoves(Mockito.anyInt(),Mockito.anyInt())).thenReturn(0);
        Mockito.verify(model,Mockito.times(2)).getIdPlayer();
        merelleStageModel.isEndStage();

        // Deuxieme IF :
        Mockito.when(merelleBoard.availableMoves(Mockito.anyInt(),Mockito.anyInt())).thenReturn(2);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,0,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.isEndStage();
        Mockito.verify(model).setIdWinner(1);

        // Troisème IF :
        Mockito.when(merelleBoard.availableMoves(Mockito.anyInt(),Mockito.anyInt())).thenReturn(2);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.removedFromGrid(new Pawn(1,1,merelleStageModel), merelleBoard,0,0);
        merelleStageModel.isEndStage();
        Mockito.verify(model).setIdWinner(0);
    }

}
