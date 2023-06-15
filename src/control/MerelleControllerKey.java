package control;

import boardifier.control.Controller;
import boardifier.control.ControllerKey;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.GameMode;
import view.MerelleView;

/**
 * A basic keystrokes handler.
 * Generally useless for board games, but it can still be used if needed
 */
public class MerelleControllerKey extends ControllerKey implements EventHandler<KeyEvent> {

    public MerelleControllerKey(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(KeyEvent event) {
        if (!model.isCaptureKeyEvent() || !event.getEventType().equals(KeyEvent.KEY_PRESSED))
            return;

//        FIXME les inputs du clavier sont ignorer lorsque les IA joue...

        GameMode selectedGameMode = ((MerelleView) view).selectedGameMode;

//        System.out.println(event.getCode());

        if (event.getCode().equals(KeyCode.LEFT) && selectedGameMode.type != GameMode.PvP) {
            MerelleDecider.decreaseAnimationSpeed();
        }
        if (event.getCode().equals(KeyCode.RIGHT) && selectedGameMode.type != GameMode.PvP) {
            MerelleDecider.increaseAnimationSpeed();
        }
        if (event.getCode().equals(KeyCode.SPACE) && selectedGameMode.type == GameMode.AIvAI) {
            if (model.isStageStarted()) model.pauseGame();
            else model.resumeGame();
        }

    }
}

