package model;

public class GameMode {
    public static final int PVP = 1;
    public static final int PVAI = 2;
    public static final int AIVAI = 3;
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