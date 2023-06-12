import boardifier.model.GameStageModel;
import model.MerelleBoard;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class MerelleBoardUnitTest {

    private MerelleBoard board;
    private final boolean[][] wantedReachableCells = new boolean[][]{
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, false, false, false, false, false}
    };

    @BeforeEach
    public void initEach() {
        GameStageModel gameStage = mock(GameStageModel.class);
        board = new MerelleBoard(0, 0, gameStage);
    }

    @Test
    public void testisActiveCell() {
        assertTrue(MerelleBoard.isActiveCell(0, 0));
        assertFalse(MerelleBoard.isActiveCell(0, 4));
        assertFalse(MerelleBoard.isActiveCell(7, 7));
    }


    @Test
    public void testgetPawnABF() {
        assertNull(board.getPawn(1, 1));
    }

    @Test
    public void testgetPawnABCF() {
        Pawn pawn = mock(Pawn.class);
        board.putElement(pawn, 0, 0);
        when(pawn.getColor()).thenReturn(1);
        when(pawn.getNumber()).thenReturn(1);
        assertNull(board.getPawn(3, 3));
    }

    @Test
    public void testgetPawnABCDF() {
        Pawn pawn = mock(Pawn.class);
        board.putElement(pawn, 0, 0);
        when(pawn.getColor()).thenReturn(1);
        when(pawn.getNumber()).thenReturn(1);
        assertNull(board.getPawn(3, 1));
    }


    @Test
    public void testgetPawnABCDE() {
        Pawn pawn = mock(Pawn.class);
        board.putElement(pawn, 0, 0);
        when(pawn.getColor()).thenReturn(3);
        when(pawn.getNumber()).thenReturn(3);
        assertEquals((board.getPawn(3, 3)), pawn);
    }

    @Test
    public void testMillsChecker() {
        Pawn p1 = mock(Pawn.class);
        Pawn p2 = mock(Pawn.class);
        Pawn p3 = mock(Pawn.class);

        int[][] mill1 = MerelleBoard.MILLS[0];
        int[][] mill2 = MerelleBoard.MILLS[1];

        // Only 2 pawn, not 3
        board.putElement(p1, mill1[0][0], mill1[0][1]);
        board.putElement(p2, mill1[1][0], mill1[1][1]);
        when(p1.getColor()).thenReturn(1);
        when(p2.getColor()).thenReturn(1);
        assertFalse(board.millsChecker(1));

        // 3 pawn but in the wrong place
        board.putElement(p3, mill2[2][0], mill2[2][1]);
        when(p3.getColor()).thenReturn(1);
        assertFalse(board.millsChecker(1));

        // 3 pawn int the right place but of the wrong color
        board.putElement(p3, mill1[2][0], mill1[2][1]);
        when(p3.getColor()).thenReturn(2);
        assertFalse(board.millsChecker(1));

        // 3 pawn int the right place of the right color
        board.putElement(p3, mill1[2][0], mill1[2][1]);
        when(p3.getColor()).thenReturn(1);
        assertTrue(board.millsChecker(1));

        // A new mill has been formed, the millUpdate is called
        verify(p1).setInAMill(anyBoolean());
        when(p1.isInAMill()).thenReturn(true);
        verify(p2).setInAMill(anyBoolean());
        when(p2.isInAMill()).thenReturn(true);
        verify(p3).setInAMill(anyBoolean());
        when(p3.isInAMill()).thenReturn(true);

        // The mill has already been formed, millsChecker cannot return true a second time
        assertFalse(board.millsChecker(1));

        // if the pawn have been mooed back and forward, then it is a new mill -> return true
        when(p1.isInAMill()).thenReturn(true);
        when(p2.isInAMill()).thenReturn(true);
        when(p3.isInAMill()).thenReturn(false);

        assertTrue(board.millsChecker(1));

        verify(p1, times(2)).setInAMill(anyBoolean());
        verify(p2, times(2)).setInAMill(anyBoolean());
        verify(p3, times(2)).setInAMill(anyBoolean());

    }

    @Test
    public void testavailableMoves() {

        // No pawn is in the grid
        assertEquals(24, board.availableMoves(0, 1));
        assertEquals(0, board.availableMoves(0, 2));

        // 1 pawn, in the corner, in the grid
        Pawn p1 = mock(Pawn.class);
        board.putElement(p1, 0, 0);
        when(p1.getColor()).thenReturn(0);
        when(p1.getCol()).thenReturn(0);
        when(p1.getRow()).thenReturn(0);
        assertEquals(23, board.availableMoves(0, 1));
        assertEquals(2, board.availableMoves(0, 2));

        // 2 pawn, in the corner, next to each other, in the grid
        Pawn p2 = mock(Pawn.class);
        board.putElement(p2, 3, 0);
        when(p2.getColor()).thenReturn(0);
        when(p2.getCol()).thenReturn(0);
        when(p2.getRow()).thenReturn(3);
        assertEquals(22, board.availableMoves(0, 1));
        assertEquals(3, board.availableMoves(0, 2));

        // 3 pawn, two in the corner next to each other, on close to the middle, in the grid
        Pawn p3 = mock(Pawn.class);
        board.putElement(p3, 3, 2);
        when(p3.getColor()).thenReturn(0);
        when(p3.getCol()).thenReturn(2);
        when(p3.getRow()).thenReturn(3);
        assertEquals(21, board.availableMoves(0, 1));
        assertEquals(6, board.availableMoves(0, 2));

        // All the pawn are encircle
        Pawn p4 = mock(Pawn.class);
        when(p4.getColor()).thenReturn(1);
        board.putElement(p4, 0, 3);
        board.putElement(p4, 6, 0);
        board.putElement(p4, 3, 1);
        board.putElement(p4, 2, 2);
        board.putElement(p4, 4, 2);
        board.putElement(p4, 3, 1);

        assertEquals(0, board.availableMoves(0, 2));
    }

    private void resetReachableCellsTable(boolean[][] table, boolean state) {
        for (int[] cell : MerelleBoard.ACTIVECELLS) {
            table[cell[0]][cell[1]] = state;
        }
    }

    @Test
    public void testSetValidCells() {
        Pawn pawn = mock(Pawn.class);
        resetReachableCellsTable(wantedReachableCells, true);
        board.setValidCells(pawn, 1);
        assertArrayEquals(wantedReachableCells, board.getReachableCells());
    }

    @Test
    public void testSetValidMillCells() {

        board.setValidMillCells(1);
        assertArrayEquals(wantedReachableCells, board.getReachableCells());

        Pawn pawn2 = mock(Pawn.class);
        when(pawn2.getColor()).thenReturn(0);
        board.putElement(pawn2, 0, 3);
        wantedReachableCells[0][3] = true;
        board.setValidMillCells(1);
        assertArrayEquals(wantedReachableCells, board.getReachableCells());

        Pawn pawn3 = mock(Pawn.class);
        when(pawn3.getColor()).thenReturn(1);
        board.putElement(pawn3, 1, 1);
        board.setValidMillCells(1);
        assertArrayEquals(wantedReachableCells, board.getReachableCells());

    }
}