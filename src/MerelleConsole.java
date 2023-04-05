import boardifier.model.GameException;
import boardifier.view.View;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.MerelleController;

public class MerelleConsole {

    public static void main(String[] args) {

        int mode = 0;
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                    mode = 0;
            }
        }
        Model model = new Model();
        if (mode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        }
        else if (mode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }

        StageFactory.registerModelAndView("merelle", "model.MerelleStageModel", "view.MerelleStageView");
        View merelleView = new View(model);
        MerelleController control = new MerelleController(model,merelleView);
        control.setFirstStageName("merelle");
        try {
            control.startGame();
            control.stageLoop();
        }
        catch(GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}
