import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import control.MerelleController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.MerelleRootPane;
import view.MerelleView;

public class MerelleConsole extends Application {
    private static int mode;
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Model model = new Model();

        StageFactory.registerModelAndView("merelle", "model.MerelleStageModel", "view.MerelleStageView");
        MerelleRootPane rootPane = new MerelleRootPane();
        MerelleView merelleView = new MerelleView(model,stage,rootPane);
        MerelleController control = new MerelleController(model, merelleView);
        control.setFirstStageName("merelle");
        stage.setTitle("Nine Men's Morris");
        stage.show();
    }
}
