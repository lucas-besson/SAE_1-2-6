package control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import model.MerelleBoard;
import model.MerellePawnPot;
import model.MerelleStageFactory;
import model.MerelleStageModel;
import model.Pawn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MerelleDecider extends Decider {

    private MerelleStageModel stage;
    private MerelleBoard board;
    private MerellePawnPot AIpot;
    private Pawn pawnToMove;
	private int rowDest;
	private int colDest;

    Random rand = new Random();


//	List<List<Integer>> closedMills = new ArrayList<List<Integer>>(); // tous les moulins (Liste d'un ensemble de 3 <x, y>)
//	List<List<Integer>> checkedMills = new ArrayList<List<Integer>>(); // tous les moulins déja utilisés (Liste d'un ensemble de 3 <x, y>)
//	int counterForm2 = 0;
//    TODO : Comment on check les moulins et ceux qui ont déja été pris en compte

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public MerelleDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of MerelleStageModel
        stage = (MerelleStageModel)model.getGameStage();
        board = stage.getBoard(); // get the board

        if (model.getIdPlayer() == Pawn.PAWN_BLACK)
            AIpot = stage.getBlackPot();
        else
            AIpot = stage.getRedPot();

        GameElement pawn = null; // the pawn that is moved
        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board


//        VERSION DE HOLEDECIDER
//        for(int i=0;i<MerellePawnPot.PAWNS_IN_POT;i++) {
//            Pawn p = (Pawn)pot.getElement(i,0);
//            // if there is a pawn in i.
//            if (p != null) {
//                // get the valid cells
//                List<Point> valid = board.computeValidCells(p,stage.getStage());
//                if (valid.size() != 0) {
//                    // choose at random one of the valid cells
//                    int id = loto.nextInt(valid.size());
//                    pawn = p;
//                    rowDest = valid.get(id).y;
//                    colDest = valid.get(id).x;
//                    break; // stop the loop
//                }
//            }
//        }

        // create action list. After the last action, it is next player's turn.
        ActionList actions = new ActionList(true);
        // create the move action, without animation => the pawn will be put at the center of dest cell
        GameAction move = new MoveAction(model, pawn, "merelleboard", rowDest, colDest);
        actions.addSingleAction(move);
        return actions;
    }
}
