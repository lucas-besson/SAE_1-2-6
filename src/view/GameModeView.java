package view;

import boardifier.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GameMode;

import javax.swing.text.html.Option;
import java.util.Optional;

public class GameModeView {
    ChoiceDialog<GameMode> gameModeChoiceDialog;
    ObservableList<GameMode> gameModesList;

    public GameModeView(Stage stage) {
        gameModesList = FXCollections.observableArrayList();
        gameModesList.addAll(
                new GameMode("Player vs Player", GameMode.PvP, "Player1", "Player2"),
                new GameMode("Player vs Basic AI", GameMode.PvAI, "Player1", "computer2"),
                new GameMode("Player vs Intelligent AI", GameMode.PvAI, "Player1", "computer1"),
                new GameMode("Intelligent AI vs Basic AI", GameMode.AIvAI, "computer1", "computer2")
        );
        gameModeChoiceDialog = new ChoiceDialog<>(gameModesList.get(0), gameModesList);
        gameModeChoiceDialog.setTitle("GameMode Selection");
        gameModeChoiceDialog.setHeaderText(null);
        gameModeChoiceDialog.setContentText("Chose a GameMode: ");
        gameModeChoiceDialog.initOwner(stage);
    }

    public GameMode showAndStart() {
        return gameModeChoiceDialog.showAndWait().orElse(null);
    }

}
