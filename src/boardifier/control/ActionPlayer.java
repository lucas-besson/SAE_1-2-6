package boardifier.control;

import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.animation.Animation;
import javafx.application.Platform;

import java.util.List;


public class ActionPlayer extends Thread {

    protected Controller control;
    protected Model model;
    protected Decider decider;
    protected ActionList actions;
    protected ActionList preActions;

    public ActionPlayer(Model model, Controller control, Decider decider, ActionList preActions) {
        this.model = model;
        this.control = control;
        this.actions = null;
        this.decider = decider;
        this.preActions = preActions;
    }

    public ActionPlayer(Model model, Controller control, ActionList actions) {
        this.model = model;
        this.control = control;
        this.actions = actions;
        this.decider = null;
        this.preActions = null;
    }

    public void run() {
        // first disable event capture
        model.setCaptureEvents(false);

        if (preActions != null) {
            playActions(preActions);
        }
        // if there is a decider, decide what to do
        if (decider != null) {
            actions = decider.decide();
        }

        playActions(actions);

        model.setCaptureEvents(true);

        // now check if the next player must play, but only if not at the end of the stage/game
        // NB: the ned of the stage/game may have been detected by playing the actions
        if ((!model.isEndStage()) && (!model.isEndGame()) && (actions.mustDoNextPlayer())) {
            Platform.runLater(() -> {
                control.nextPlayer();
            });
        }
    }

    private void playActions(ActionList actions) {
        // loop over all action packs
        int idPack = 0;
        for (List<GameAction> actionPack : actions.getActions()) {
            System.out.println("playing pack " + idPack);
            // step 1 : start animations from actions.
            Animation[] animations = new Animation[actionPack.size()];
            int idx = 0;
            for (GameAction action : actionPack) {
                if (action.getAnimation() != null) {
                    animations[idx++] = action.getAnimation();
                    action.setupAnimation();
                }
            }
            // step 2 : start animations of the same pack
            for (int i = 0; i < idx; i++) {
                animations[i].start();

            }
            // step 3 : wait for the end of all animations
            for (int i = 0; i < idx; i++) {
                animations[i].getAnimationState().waitStop();
            }
            // step 4 : do the real actions, based on action.type
            for (GameAction action : actionPack) {
                action.execute();
            }
            // last enable event capture
            idPack++;
        }
    }
}
