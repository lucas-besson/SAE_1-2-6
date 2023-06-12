import boardifier.model.Model;
import boardifier.model.Player;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageModel;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import view.PawnPotLook;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MerelleStageModelUnitTest {

    private MerelleBoard merelleBoard;
    private Model model;
    private MerelleStageModel merelleStageModel;
    private MerellePawnPot merellePawnPot;

    @BeforeEach
    public void beforeEach() {
        merelleBoard = mock(MerelleBoard.class);
        model = mock(Model.class);
        merelleStageModel = new MerelleStageModel("test", model);
        merellePawnPot = mock(MerellePawnPot.class);
        when(model.getGameStage()).thenReturn(merelleStageModel);
        merelleStageModel.setBoard(merelleBoard);
        merelleStageModel.setBlackPot(merellePawnPot);
    }

    @Test
    public void testisEndStage() {


        //Premier if :
        when(model.getIdPlayer()).thenReturn(3);
        when(merelleBoard.availableMoves(anyInt(), anyInt())).thenReturn(0);
        merelleStageModel.isEndStage();
        verify(model, times(2)).getIdPlayer();
        verify(model).setIdWinner(3);
        // Troisème IF :
        when(merelleBoard.availableMoves(anyInt(), anyInt())).thenReturn(2);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 1, merelleStageModel), merelleBoard, 0, 0);
        verify(model).setIdWinner(0);

        // Deuxieme IF :
        when(merelleBoard.availableMoves(anyInt(), anyInt())).thenReturn(2);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        merelleStageModel.removedFromGrid(new Pawn(1, 0, merelleStageModel), merelleBoard, 0, 0);
        verify(model).setIdWinner(1);
    }

    @Test
    public void testCallBacks() {
        Pawn pawn = mock(Pawn.class);

        // onSelectionChange :
        // skip the if because the selected list isn't empty
        merelleStageModel.setSelected(pawn, true);
        verify(merelleBoard).setValidCells(eq(pawn), anyInt());
        // go through the if because the selected list is empty
        merelleStageModel.setSelected(pawn, false);
        verify(merelleBoard).resetReachableCells(false);

        // onPutInGrid :
        merelleStageModel.putInGrid(pawn, merelleBoard, 0, 1);
        verify(pawn).setInAMill(false);
        verify(pawn).setCol(1);
        verify(pawn).setRow(0);

        // onMoveInGrid :
        merelleStageModel.movedInGrid(pawn, merelleBoard, 2, 3);
        verify(pawn, times(2)).setInAMill(false);
        verify(pawn).setCol(3);
        verify(pawn).setRow(2);

        // onRemoveFromGrid :
        // if the board is the same as in the stageModel
        merelleStageModel.putInGrid(pawn, mock(MerelleBoard.class), 2, 3);

        // if player play black pawn
        when(pawn.getColor()).thenReturn(0);
        merelleStageModel.removedFromGrid(pawn, merelleBoard, 2, 3);
        assertEquals(MerellePawnPot.PAWNS_IN_POT - 1, merelleStageModel.getBlackPawnsToPlay());

        // if player play red pawn
        when(pawn.getColor()).thenReturn(1);
        merelleStageModel.removedFromGrid(pawn, merelleBoard, 2, 3);
        assertEquals(MerellePawnPot.PAWNS_IN_POT - 1, merelleStageModel.getRedPawnsToPlay());
    }
}
