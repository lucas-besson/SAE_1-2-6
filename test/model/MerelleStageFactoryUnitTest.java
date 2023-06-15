package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MerelleStageFactoryUnitTest {

    private MerelleStageFactory merelleStageFactory;
    private MerelleStageModel merelleStageModel;

    @BeforeEach
    void beforEach() {
//        Model model = new Model();
//        merelleStageModel = new MerelleStageModel("Test", model);
        merelleStageModel = mock(MerelleStageModel.class);
        merelleStageFactory = new MerelleStageFactory(merelleStageModel);
    }

    @Test
    void testSetup() {
        final ArgumentCaptor<Pawn[]> captorPawns = ArgumentCaptor.forClass(Pawn[].class);
        final ArgumentCaptor<MerellePawnPot> captorPot = ArgumentCaptor.forClass(MerellePawnPot.class);
        merelleStageFactory.setup();

        verify(merelleStageModel).setBoard(any());

        // Verify the pawns and pot are correctly initiate and the pawns used are the same.
        verify(merelleStageModel).setBlackPawns(captorPawns.capture());
        verify(merelleStageModel).setBlackPot(captorPot.capture());
        for (Pawn pawn : captorPawns.getValue()) {
            assertTrue(captorPot.getValue().contains(pawn));
        }

        // Verify the pawns and pot are correctly initiate and the pawns used are the same.
        verify(merelleStageModel).setRedPawns(captorPawns.capture());
        verify(merelleStageModel).setRedPot(captorPot.capture());
        for (Pawn pawn : captorPawns.getValue()) {
            assertTrue(captorPot.getValue().contains(pawn));
        }
    }
}
