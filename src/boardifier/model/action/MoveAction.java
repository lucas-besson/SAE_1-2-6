package boardifier.model.action;

import boardifier.model.*;
import boardifier.model.animation.AnimationTypes;
import boardifier.model.animation.LinearMoveAnimation;
import boardifier.model.animation.MoveAnimation;


public class MoveAction extends GameAction {

    protected String gridDest;
    protected int rowDest;
    protected int colDest;
    protected double xDest;
    protected double yDest;
    protected double factor; // a speed in pixel/ms or the whole duration, see LinearMoveAnimation

    public MoveAction(Model model, GameElement element, String gridDest, int rowDest, int colDest, String animationName, double xDest, double yDest, double factor) {
        super(model, element, animationName);

        this.gridDest = gridDest;
        this.rowDest = rowDest;
        this.colDest = colDest;
        this.xDest = xDest;
        this.yDest = yDest;
        this.factor = factor;

        createAnimation();
    }


    public String getGridDest() {
        return gridDest;
    }

    public int getRowDest() {
        return rowDest;
    }

    public int getColDest() {
        return colDest;
    }

    public void execute() {
        GridElement gridSrc = element.getGrid();
        GridElement gridDest = model.getGrid(this.gridDest);
        if (gridDest == null) return;
        if (gridSrc == gridDest) {
            gridDest.moveElement(element, rowDest, colDest);
        }
        else {
            gridSrc.removeElement(element);
            gridDest.putElement(element, rowDest, colDest);
        }
        onEndCallback.execute();
    }

    protected void createAnimation() {
        animation = null;
        if (animationName.startsWith("move")) {
            GridElement grid = model.getGrid(gridDest);
            if (grid == null) return;
            Coord2D endLoc = new Coord2D(xDest, yDest);
            // create animation to visualize this movement
            if (animationType == AnimationTypes.MOVETELEPORT_VALUE) {
                animation = new MoveAnimation(model, element.getLocation(), endLoc);
            } else if ((animationType == AnimationTypes.MOVELINEARPROP_VALUE) ||
                    (animationType == AnimationTypes.MOVELINEARCST_VALUE)) {
                animation = new LinearMoveAnimation(model, element.getLocation(), endLoc, animationType, factor);
            }
        }
    }
}
