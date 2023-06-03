package view;

import boardifier.view.View;
import javafx.scene.control.Alert;

public class MillAlert {
    Alert alert;
    public MillAlert () {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mill Alert");
        alert.setHeaderText("You just formed a new Mill !!");
        alert.setContentText(
                "You just formed a new mill and have to remove an opponent pawn by clicking on it. " +
                "The game will not continue unless you remove an opponent pawn."
        );
    }
    public void show(){
        alert.showAndWait();
    }
}
