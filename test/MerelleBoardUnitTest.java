import boardifier.model.GameElement;
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
        MerelleBoard board = Mockito.mock(MerelleBoard.class);
        Mockito.when(board.isEmptyAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        Assertions.assertNull(board.getPawn(Mockito.anyInt(), Mockito.anyInt()));
    }

    @Test
    public void testgetPawnABCF() {
        MerelleBoard board = Mockito.mock(MerelleBoard.class);
        Pawn pawn = Mockito.mock(Pawn.class);
        Mockito.when(board.isEmptyAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(board.getElement(Mockito.anyInt(), Mockito.anyInt())).thenReturn((GameElement)pawn);
        Mockito.when(pawn.getColor()).thenReturn(2);
        Assertions.assertNull(board.getPawn(1, 1));
    }

    @Test
    public void testgetPawnABCDF() {
        MerelleBoard board = Mockito.mock(MerelleBoard.class);
        Pawn pawn = Mockito.mock(Pawn.class);
        Mockito.when(board.isEmptyAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(board.getElement(Mockito.anyInt(), Mockito.anyInt())).thenReturn((GameElement)pawn);
        Mockito.when(pawn.getColor()).thenReturn(2);
        Mockito.when(pawn.getNumber()).thenReturn(2);
        Assertions.assertNull(board.getPawn(2, 1));
    }

    @Test
    public void testgetPawnABCDE() {
        MerelleBoard board = Mockito.mock(MerelleBoard.class);
        Pawn pawn = Mockito.mock(Pawn.class);
        Mockito.when(board.isEmptyAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        Mockito.when(board.getElement(Mockito.anyInt(), Mockito.anyInt())).thenReturn((GameElement)pawn);
        Mockito.when(pawn.getColor()).thenReturn(2);
        Mockito.when(pawn.getNumber()).thenReturn(2);
        Assertions.assertEquals((board.getPawn(2, 2)),pawn);
    }

}
