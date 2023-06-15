package control;

import boardifier.control.Controller;
import boardifier.control.ControllerAction;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.GameMode;
import view.MerelleView;

/**
 * A basic action controller that only manages menu actions
 * Action events are mostly generated when there are user interactions with widgets like
 * buttons, checkboxes, menus, ...
 */
public class MerelleControllerAction extends ControllerAction implements EventHandler<ActionEvent> {

    // to avoid lots of casts, create an attribute that matches the instance type.
    private final MerelleView merelleView;

    public MerelleControllerAction(Model model, View view, Controller control) {
        super(model, view, control);
        // take the view parameter ot define a local view attribute with the real instance type, i.e. HoleView.
        merelleView = (MerelleView) view;

        // set handlers dedicated to menu items
        setMenuHandlers();

        // If needed, set the general handler for widgets that may be included within the scene.
        // In this case, the current gamestage view must be retrieved and casted to the right type
        // in order to have an access to the widgets, and finally use setOnAction(this).
        // For example, assuming the current gamestage view is an instance of MyGameStageView, which
        // creates a Button myButton :
        // ((MyGameStageView)view.getCurrentGameStageView()).getMyButton().setOnAction(this).

    }

    private void setMenuHandlers() {

        // set event handler on the MenuStart item
        merelleView.getMenuStart().setOnAction(e -> {
            try {
                GameMode result = ((MerelleView) view).gameModeView();
                if (result == null) return;
                // if the result is usable, it will clear the actual game and start another.
                ((MerelleView) view).selectedGameMode = result; // We keep track of the selected game mode
                model.getPlayers().clear();
                switch (result.type) {
                    case GameMode.PvAI -> {
                        model.addHumanPlayer(result.player1);
                        model.addComputerPlayer(result.player2);
                    }
                    case GameMode.AIvAI -> {
                        model.addComputerPlayer(result.player1);
                        model.addComputerPlayer(result.player2);
                    }
                    default -> {
                        model.addHumanPlayer(result.player1);
                        model.addHumanPlayer(result.player2);
                    }
                }
                control.startGame();
            } catch (GameException err) {
                System.err.println(err.getMessage());
                System.exit(1);
            }
        });
        // set event handler on the MenuIntro item
        merelleView.getMenuIntro().setOnAction(e -> {
            control.stopGame();
            merelleView.resetView();
        });
        // set event handler on the MenuQuit item
        merelleView.getMenuQuit().setOnAction(e -> System.exit(0));
    }
}

