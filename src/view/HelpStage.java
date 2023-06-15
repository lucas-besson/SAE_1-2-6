package view;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;

public class HelpStage extends Stage {
    static final String RULES = """
            Nine Men's Morris is an ancient strategy board game that dates back to the Roman Empire and was popular throughout Europe during the Middle Ages. The game is played on a grid consisting of three concentric squares connected by lines. Here are the rules:
            
            \t1.\t\tThe game starts with an empty board. Each player has nine pieces, distinguished by color, such as red and black.
            \t2.\t\tPlayers take turns placing their pieces on the intersections of the lines. The goal is to form "mills", which are vertical or horizontal lines of three pieces of the same color. When a mill is formed, the player who created it can remove one of their opponent's pieces from the board (by clicking on it).
            \t3.\t\tAfter all the pieces have been placed on the board, players take turns moving their pieces along the lines to adjacent empty intersections (by clicking on the pawn and the wanted destination). The pieces can move in any direction along the lines, but they cannot jump over other pieces.
            \t4.\t\tWhen a player's move creates a mill (three pieces in a row), they can remove one of their opponent's pieces from the board.
            \t5.\t\tThe game continues until one player has only two pieces left or is unable to make a legal move. In the latter case, the other player wins.
            \t6.\t\tThe game ends when one player captures enough of their opponent's pieces to reduce them to two, or when a player is unable to make a legal move.
            
            Strategic thinking in Nine Men's Morris revolves around creating mills while simultaneously blocking your opponent's moves and preventing them from forming mills. The ability to anticipate and disrupt your opponent's plans is crucial for success in this game.
            It's up to you!""";

    static final String CREDITS = """
            Developed with ❤ by :
            \tLucas BESSON
            \tNathan BOSCHI
            \tRobin HALM
            \tMathys NOURRY
            \tTom SIOUAN
               
            Using :
            \t- JAVA Language
            \t- JavaFX Framework
            \t- Boardifier Framework
               
            Our github repository : [https://github.com/lucas-besson/SAE_1-2-6]""";


    public HelpStage(TypeOfHelp type, Window owner) {
        this.initOwner(owner);
        if (type == TypeOfHelp.RULES) initWidgets(RULES);
        else if (type == TypeOfHelp.CREDITS) initWidgets(CREDITS);
    }

    public void initWidgets(String textToPrint) {
        FlowPane mainPane;
        Text text = new Text(textToPrint);

        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setWrappingWidth(700);
        text.setTextAlignment(TextAlignment.JUSTIFY);
        text.setFontSmoothingType(FontSmoothingType.LCD);

        mainPane = new FlowPane();
        mainPane.getChildren().add(text);
        mainPane.setAlignment(Pos.CENTER);

        this.setResizable(false);

        this.setScene(new Scene(mainPane, 800, 500));
    }

    public void display() {

        super.show();
    }

    public enum TypeOfHelp {
        RULES, CREDITS;
    }
}
