package core;

import utils.GraphNode;
import java.util.HashMap;

public class GameState {
    static final int maxGameTicks = 5;
    static final int nTeams = 2;

    public boolean gameEnded;
    int gameTick;
    Button button;
    Player[] players;

    HashMap<Integer, GraphNode> maze;
    int width, height;

    public GameState() {}

    /**
     * Copy the game state.
     * @return - a new GameState object with the same values for all variables as currently set in this object.
     */
    public GameState copy() {
        GameState gs = new GameState();
        gs.gameEnded = gameEnded;
        gs.gameTick = gameTick;
        gs.button = button.copy();
        gs.players = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            gs.players[i] = players[i].copy();
        }
        return gs;
    }

    @Override
    public String toString() {
        String ps = "";
        for (Player p: players) {
            ps += "\t" + p.toString() + "\n";
        }
        return "tick: " + gameTick +
                ", ended? " + gameEnded +

                "\n\t" + button.toString() +
                "\n" + ps;
    }
}
