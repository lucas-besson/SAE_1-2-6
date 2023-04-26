import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import model.MerelleBoard;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MerelleBoardUnitTest {

    @Test
    public void testisActiveCell(){
        Assertions.assertTrue(MerelleBoard.isActiveCell(0, 0));
        Assertions.assertFalse(MerelleBoard.isActiveCell(0, 4));
        Assertions.assertFalse(MerelleBoard.isActiveCell(7, 7));
    }


    @Test
    public void testgetPawnABF() {
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerelleBoard board = new MerelleBoard(1,2,gameStage);
        Assertions.assertNull(board.getPawn(Mockito.anyInt(), Mockito.anyInt()));
    }

    @Test
    public void testgetPawnABCF() {
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerelleBoard board = new MerelleBoard(1,2,gameStage);
        Pawn pawn = Mockito.mock(Pawn.class);
        board.putElement(pawn,0,0);
        Mockito.when(pawn.getColor()).thenReturn(1);
        Mockito.when(pawn.getNumber()).thenReturn(1);
        Assertions.assertNull(board.getPawn(3, 3));
    }

    @Test
    public void testgetPawnABCDF() {
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerelleBoard board = new MerelleBoard(1,2,gameStage);
        Pawn pawn = Mockito.mock(Pawn.class);
        board.putElement(pawn,0,0);
        Mockito.when(pawn.getColor()).thenReturn(1);
        Mockito.when(pawn.getNumber()).thenReturn(1);
        Assertions.assertNull(board.getPawn(3, 1));
    }


    @Test
    public void testgetPawnABCDE() {
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerelleBoard board = new MerelleBoard(1,2,gameStage);
        Pawn pawn = Mockito.mock(Pawn.class);
        board.putElement(pawn,0,0);
        Mockito.when(pawn.getColor()).thenReturn(3);
        Mockito.when(pawn.getNumber()).thenReturn(3);
        Assertions.assertEquals((board.getPawn(3, 3)),pawn);
    }

}
