import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.MerelleController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.Images;
import view.MerelleRootPane;
import view.MerelleView;

import java.nio.file.Paths;

public class MerelleGUI extends Application {
    private static int mode;

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode < 0) || (mode > 2)) mode = 0;
            } catch (NumberFormatException e) {
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
        MerelleView merelleView = new MerelleView(model, stage, rootPane);
        MerelleController control = new MerelleController(model, merelleView);
        control.setFirstStageName("merelle");
        stage.setTitle("Nine Men's Morris");
        stage.getIcons().add(Images.loadImage(Paths.get(".").resolve("assets").resolve("images").resolve("favicon.png").toString()));
        stage.show();
    }
}
