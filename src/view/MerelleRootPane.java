package view;

import boardifier.view.RootPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MerelleRootPane extends RootPane {

    public MerelleRootPane() {
        super();
    }

    @Override
    public void createDefaultGroup() {
        Rectangle frame = new Rectangle(600, 100, Color.LIGHTGREY);
        Text text = new Text("Playing to Nine Men's Morris");
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setX(10);
        text.setY(50);
        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(frame, text);
    }
}
