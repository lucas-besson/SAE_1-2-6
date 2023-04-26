import boardifier.control.ActionPlayer;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import model.MerelleBoard;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;

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

    @Test
    public void testMillsChecker(){
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerelleBoard board = new MerelleBoard(1,2,gameStage);
        Pawn p1 = Mockito.mock(Pawn.class);
        Pawn p2 = Mockito.mock(Pawn.class);
        Pawn p3 = Mockito.mock(Pawn.class);

        int[][] mill1 = MerelleBoard.MILLS[0];
        int[][] mill2 = MerelleBoard.MILLS[1];

        // Only 2 pawn, not 3
        board.putElement(p1,mill1[0][0],mill1[0][1]);
        board.putElement(p2,mill1[1][0],mill1[1][1]);
        Mockito.when(p1.getColor()).thenReturn(1);
        Mockito.when(p2.getColor()).thenReturn(1);
        Assertions.assertFalse(board.millsChecker(1));

        // 3 pawn but in the wrong place
        board.putElement(p3,mill2[2][0],mill2[2][1]);
        Mockito.when(p3.getColor()).thenReturn(1);
        Assertions.assertFalse(board.millsChecker(1));

        // 3 pawn int the right place but of the wrong color
        board.putElement(p3,mill1[2][0],mill1[2][1]);
        Mockito.when(p3.getColor()).thenReturn(2);
        Assertions.assertFalse(board.millsChecker(1));

        // 3 pawn int the right place of the right color
        board.putElement(p3,mill1[2][0],mill1[2][1]);
        Mockito.when(p3.getColor()).thenReturn(1);
        Assertions.assertTrue(board.millsChecker(1));

        // A new mill has been formed, the millUpdate is called
        Mockito.verify(p1).setInAMill(Mockito.anyBoolean());
        Mockito.when(p1.isInAMill()).thenReturn(true);
        Mockito.verify(p2).setInAMill(Mockito.anyBoolean());
        Mockito.when(p2.isInAMill()).thenReturn(true);
        Mockito.verify(p3).setInAMill(Mockito.anyBoolean());
        Mockito.when(p3.isInAMill()).thenReturn(true);

        // The mill has already been formed, millsChecker cannot return true a second time
        Assertions.assertFalse(board.millsChecker(1));

        // if the pawn have been mooed back and forward, then it is a new mill -> return true
        Mockito.when(p1.isInAMill()).thenReturn(true);
        Mockito.when(p2.isInAMill()).thenReturn(true);
        Mockito.when(p3.isInAMill()).thenReturn(false);

        Assertions.assertTrue(board.millsChecker(1));

        Mockito.verify(p1, Mockito.times(2)).setInAMill(Mockito.anyBoolean());
        Mockito.verify(p2, Mockito.times(2)).setInAMill(Mockito.anyBoolean());
        Mockito.verify(p3, Mockito.times(2)).setInAMill(Mockito.anyBoolean());

    }
}

