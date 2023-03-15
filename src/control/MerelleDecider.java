package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.util.Calendar;
import java.util.Random;

public class MerelleDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
    }


    @Override
    public ActionList decide() {
        ActionList actions = null;
        /*
        TO FULFILL:
            - compute the action list.
         */
        return actions;
    }
}
