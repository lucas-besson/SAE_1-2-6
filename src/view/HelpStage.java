package view;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.lang.reflect.Type;

public class HelpStage extends Stage {
    static final String HelpText = " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin suscipit neque et elit suscipit convallis vel accumsan odio. Donec et ligula tempus, tincidunt mauris vitae, tincidunt leo. Vivamus bibendum quam in nibh sollicitudin, id viverra tortor cursus. Phasellus semper diam eget tristique rutrum. Donec nec nisi libero. Sed vitae dolor ante. Curabitur congue est ac cursus suscipit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis aliquet magna eu bibendum tempus. Praesent sed ornare diam, at blandit lacus. Suspendisse eget tempus orci, sed tincidunt turpis.\n" +
            "\n" +
            "Sed suscipit, libero ac pretium porta, lacus leo pellentesque massa, cursus tincidunt lectus mauris eget erat. Integer et rhoncus massa. Sed in arcu vel nunc convallis commodo. Phasellus non condimentum dolor, sit amet mollis mi. Donec vel volutpat diam. Morbi scelerisque placerat leo ac efficitur. Vestibulum interdum, sem et sagittis porta, lorem purus ullamcorper risus, id laoreet purus sapien in sem. Mauris massa risus, varius vel blandit at, tincidunt id mauris. Nunc faucibus tellus non magna venenatis pharetra. Donec ut faucibus odio. Donec sed elit justo. Duis finibus tellus sed ex maximus tempor. Donec feugiat risus enim, eu pretium sapien congue eu. Integer nec faucibus nibh, cursus aliquet lorem. Nullam ac metus a est euismod venenatis.\n" +
            "\n" +
            "Duis elementum purus dapibus turpis pellentesque euismod. Proin molestie nisi nec nunc laoreet blandit. In consectetur mauris elementum, bibendum enim sed, dapibus lacus. Curabitur rhoncus leo a varius vestibulum. Duis sed justo ornare nunc rhoncus volutpat eu nec lorem. Vestibulum vitae facilisis dolor. Ut lacinia felis ac metus malesuada congue scelerisque vel nisl. Mauris rutrum nisi eget lobortis cursus. Mauris rutrum turpis urna, nec imperdiet felis euismod at. Suspendisse quis aliquet justo. Sed mattis augue id cursus semper. ";

    static final String Credits = "Developed with heart by :\n" +
            "   Lucas BESSON\n" +
            "   Nathan BOSCHI\n" +
            "   Robin HALM\n" +
            "   Mathys NOURRY\n" +
            "   Tom SIOUAN\n" +
            "\nEn utilisant :\n" +
            "   - Language JAVA\n" +
            "   - Framework JavaFX\n" +
            "   - Framework Boardifier";

    public HelpStage(TypeOfHelp type){
        if (type == TypeOfHelp.HOW_TO_PLAY) initWidgets(HelpText);
        else if (type == TypeOfHelp.CREDITS) initWidgets(Credits);
    }

    private FlowPane mainPane;

    public void initWidgets(String textToPrint){
        Text text = new Text(textToPrint);

        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setWrappingWidth(700);
        text.setTextAlignment(TextAlignment.JUSTIFY);

        mainPane = new FlowPane();
        mainPane.getChildren().add(text);
        mainPane.setAlignment(Pos.CENTER);
    }

    public void display(){

        this.setScene(new Scene(mainPane, 800, 500));

        super.show();
    }

    public enum TypeOfHelp{
        HOW_TO_PLAY, CREDITS;
    }
}
