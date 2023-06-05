package model;

public class GameMode {
    public static final int PvP = 1;
    public static final int PvAI = 2;
    public static final int AIvAI = 3;
    public String name;
    public String player1;
    public String player2;
    public int type;

    public GameMode(String name, int type, String player1, String player2) {
        this.name = name;
        this.player1 = player1;
        this.player2 = player2;
        this.type = type;
    }

    public String toString() {
        return this.name;
    }
}