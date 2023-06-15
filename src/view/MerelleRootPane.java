package view;

import boardifier.view.RootPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class MerelleRootPane extends RootPane {

    public MerelleRootPane() {
        super();
    }

    @Override
    public void createDefaultGroup() {
        int height;
        int width;
        width = 1080;
        height = 450;
        Rectangle frame = new Rectangle(width, height, Color.LIGHTGREY);
        Text text = new Text("Playing to Nine Men's Morris\n\n" + HelpStage.RULES);
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setX(20);
        text.setY(50);
        text.setWrappingWidth(width - 40);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(frame, text);
    }
}
