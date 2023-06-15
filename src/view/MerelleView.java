package view;

import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.GameMode;

public class MerelleView extends View {

    private final MillAlert millAlert;
    public GameMode selectedGameMode;
    private MenuItem menuStart;
    private MenuItem menuIntro;
    private MenuItem menuQuit;
    private ChoiceDialog<GameMode> gameModeView;
    private ObservableList<GameMode> gameModesList;
    private HelpStage helpRules;
    private HelpStage helpCredits;

    public MerelleView(Model model, Stage stage, RootPane rootPane) {
        super(model, stage, rootPane);
        millAlert = new MillAlert(this.getStage());
        helpRules = new HelpStage(HelpStage.TypeOfHelp.RULES, this.getStage());
        helpCredits = new HelpStage(HelpStage.TypeOfHelp.CREDITS, this.getStage());
    }

    public GameMode gameModeView() {
        gameModesList = FXCollections.observableArrayList();
        gameModesList.addAll(
                new GameMode("Player vs Player", GameMode.PVP, "Player1", "Player2"),
                new GameMode("Player vs Basic AI", GameMode.PVAI, "Player1", "computer2"),
                new GameMode("Player vs Intelligent AI", GameMode.PVAI, "Player1", "computer1"),
                new GameMode("Intelligent AI vs Basic AI", GameMode.AIVAI, "computer1", "computer2")
        );
        gameModeView = new ChoiceDialog<>(gameModesList.get(0), gameModesList);
        gameModeView.setTitle("GameMode Selection");
        gameModeView.setHeaderText(null);
        gameModeView.setContentText("Chose a GameMode: ");
        gameModeView.initOwner(stage);
        // if the game mode was previously selected, use it to set the default value for quick restart
        if (selectedGameMode != null) gameModeView.setSelectedItem(selectedGameMode);
        return gameModeView.showAndWait().orElse(null);
    }

    public MillAlert millAlert() {
        return millAlert;
    }

    @Override
    protected void createMenuBar() {
        menuBar = new MenuBar();

        Menu menu1 = new Menu("Game");
        menuStart = new MenuItem("New game");
        menuIntro = new MenuItem("Intro");
        menuQuit = new MenuItem("Quit");
        menu1.getItems().add(menuStart);
        menu1.getItems().add(menuIntro);
        menu1.getItems().add(menuQuit);
        menuBar.getMenus().add(menu1);

        Menu menu2 = new Menu("Help");
        MenuItem helpStageOpenMenu = new MenuItem("How to play ?");
        helpStageOpenMenu.setOnAction(event -> {
            assert helpRules != null;
            if (helpRules.isShowing()) helpRules.requestFocus();
            else helpRules.display();
        });
        MenuItem creditsStageOpenMenu = new MenuItem("Credits");
        creditsStageOpenMenu.setOnAction(event -> {
            assert helpCredits != null;
            if (helpCredits.isShowing()) helpCredits.requestFocus();
            else helpCredits.display();
        });
        menu2.getItems().addAll(helpStageOpenMenu, creditsStageOpenMenu);
        menuBar.getMenus().add(menu2);
    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuIntro() {
        return menuIntro;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }
}
