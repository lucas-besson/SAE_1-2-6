package view;

import boardifier.model.GameElement;
import boardifier.model.GridElement;
import boardifier.view.ConsoleColor;
import boardifier.view.GridLook;
import model.MerelleBoard;

public class MerelleGridLook extends GridLook {

    public MerelleGridLook(int width, int height, int cellWidth, int cellHeight, int borderWidth, String borderColor, GameElement element) {
        super(width,height,cellWidth,cellHeight,borderWidth,borderColor,element);
    }


    @Override
    public void onChange() {
//        FIXME
    }
}