package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MillAlert {
    Alert alert;
    public MillAlert (Stage stage) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mill Alert");
        alert.setHeaderText("You just formed a new Mill !!");
        alert.initOwner(stage);
        alert.getDialogPane().setContent(
                new Label("You just formed a new mill and have to remove an opponent pawn by clicking on it. " +
                "\nThe game will not continue unless you remove an opponent pawn.")
        );
        alert.setAlertType(Alert.AlertType.INFORMATION);
    }
    public void show(){
        alert.showAndWait();
    }
}
