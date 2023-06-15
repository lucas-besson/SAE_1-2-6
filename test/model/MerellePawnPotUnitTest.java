package model;

import boardifier.model.GameStageModel;
import model.MerellePawnPot;
import model.Pawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class MerellePawnPotUnitTest {
    @Test
    void testIsEmpty(){
        GameStageModel gameStage = Mockito.mock(GameStageModel.class);
        MerellePawnPot merellePawnPot = new MerellePawnPot(0,0,gameStage);

        Assertions.assertTrue(merellePawnPot.isEmpty());

        Pawn pawn = Mockito.mock(Pawn.class);
        merellePawnPot.putElement(pawn, 0,0);

        Assertions.assertFalse(merellePawnPot.isEmpty());
    }
}