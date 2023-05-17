package boardifier.model;

import boardifier.view.BackgroundLook;
import javafx.scene.paint.Color;

public class BackgroundElement extends StaticElement {

    public BackgroundElement(int x, int y, GameStageModel gameStageModel) {
        super(x, y, gameStageModel, ElementTypes.getType("background"));
    }
}
