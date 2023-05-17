import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import control.MerelleController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MerelleConsole extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        int mode = 1;
//        if (args.length == 1) {
//            try {
//                mode = Integer.parseInt(args[0]);
//                if ((mode < 0) || (mode > 2)) mode = 0;
//            } catch (NumberFormatException e) {
//                mode = 0;
//            }
//        }
//        if (mode == 0) {
//        } else if (mode == 1) {
//            model.addHumanPlayer("player");
//            model.addComputerPlayer("computer");
//        } else if (mode == 2) {
//            model.addComputerPlayer("computer1");
//            model.addComputerPlayer("computer2");
//        }

        Model model = new Model();
        model.addHumanPlayer("player1");
        model.addHumanPlayer("player2");
        StageFactory.registerModelAndView("merelle", "model.MerelleStageModel", "view.MerelleStageView");

        RootPane rootPane = new RootPane();
        View merelleView = new View(model, stage, rootPane);
        MerelleController control = new MerelleController(model, merelleView);
        control.setFirstStageName("merelle");
        try {
            control.startGame();
            control.stageLoop();
        } catch (GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}
