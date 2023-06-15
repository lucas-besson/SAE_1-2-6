package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.animation.Animation;
import boardifier.model.animation.AnimationStep;
import boardifier.model.animation.LinearMoveAnimation;
import boardifier.view.GridGeometry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PawnUnitTest {

    private Pawn pawn;
    private GridGeometry gridGeometry;
    private GameStageModel gameStageModel;
    private MerelleStageModel merelleStageModel;

    @BeforeEach
    void beforeEach() {
        merelleStageModel = new MerelleStageModel("test", new Model());
        pawn = new Pawn(1, 2, merelleStageModel);
        gridGeometry = mock(GridGeometry.class);
    }

    @Test
    void testUpdate() {
        Animation animation = mock(LinearMoveAnimation.class);
        AnimationStep animationStep = mock(AnimationStep.class);
        when(animation.next()).thenReturn(null, animationStep);
        when(animationStep.getInt(0)).thenReturn(0);
        when(animationStep.getInt(1)).thenReturn(1);

//        null animation
        pawn.update(1, 2, gridGeometry);

//        animation set & step null
        pawn.setAnimation(animation);
        pawn.update(1, 2, gridGeometry);
        assertNull(pawn.getAnimation());

//        animation set & step set
        pawn.setAnimation(animation);
        pawn.getAnimation().start();
        pawn.update(1, 2, gridGeometry);
        assertEquals(0, pawn.getX());
        assertEquals(1, pawn.getY());
        assertNotNull(pawn.getAnimation());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, pawn.getNumber());
        assertEquals(2, pawn.getColor());
        assertFalse(pawn.isInAMill());

        MerelleBoard merelleBoard = new MerelleBoard(0, 0, merelleStageModel);
        merelleBoard.putElement(pawn, 3, 4);
        assertEquals(3, pawn.getRow());
        assertEquals(4, pawn.getCol());
    }
}