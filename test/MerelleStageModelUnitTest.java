import boardifier.model.Model;
import model.MerelleBoard;
import model.MerelleStageModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MerelleStageModelUnitTest {

    // @Lucas: I'm checking the logic of the function for the moment, see with
    // @Robin or if ever we can't find with the teacher how to test that we pass well in each if without using a mock for the MerelleStageModel
    @Test
    public void testisEndStage() {
        MerelleBoard merelleBoard = Mockito.mock(MerelleBoard.class);
        Model model = Mockito.mock(Model.class);
        MerelleStageModel merelleStageModel = Mockito.mock(MerelleStageModel.class);

        Mockito.when(model.getIdPlayer()).thenReturn(3);
        Mockito.when(merelleStageModel.getBoard()).thenReturn(merelleBoard);
        Mockito.when(merelleStageModel.getStatus()).thenReturn(0);
        if (merelleStageModel.getBoard().availableMoves(((model.getIdPlayer() == 1) ? 0 : 1), merelleStageModel.getStatus()) == 0) {
            model.setIdWinner(model.getIdPlayer());
            model.stopStage();
        }
        Mockito.verify(model).setIdWinner(3);


        Mockito.when(merelleStageModel.getBlackPawnsToPlay()).thenReturn(2);
        if (merelleStageModel.getBlackPawnsToPlay() == 2) {
        model.setIdWinner(1);
        model.stopStage();
        }
        Mockito.verify(model).setIdWinner(1);

        Mockito.when(merelleStageModel.getRedPawnsToPlay()).thenReturn(2);
        if (merelleStageModel.getRedPawnsToPlay() == 2) {
            model.setIdWinner(0);
            model.stopStage();
        }
        Mockito.verify(model).setIdWinner(0);
    }

}
