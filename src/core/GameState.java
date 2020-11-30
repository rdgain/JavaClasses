package core;

import utils.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameState {
    static final int maxGameTicks = 1000;
    static final int nTeams = 2;
    static final int nTotalPickups = 50;

    public boolean gameEnded;
    int gameTick;
    Button button;
    Player[] players;

    HashMap<Integer, GraphNode> maze;
    int width, height;

    ArrayList<Pickup> pickups;
    int nPickups;

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
        gs.maze = new HashMap<>();
        for (Map.Entry<Integer, GraphNode> e: maze.entrySet()) {
            gs.maze.put(e.getKey(), e.getValue().copy());
        }
        gs.width = width;
        gs.height = height;

        gs.pickups = new ArrayList<>();
        for (Pickup k: pickups) {
            gs.pickups.add(k.copy());
        }
        gs.nPickups = nPickups;
        return gs;
    }

    public HashMap<Integer, GraphNode> getMaze() {
        return maze;
    }

    public Button getButton() {
        return button;
    }

    public int getGameTick() {
        return gameTick;
    }

    public static int getMaxGameTicks() {
        return maxGameTicks;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGameStatus(int playerID) {
        return players[playerID].gameStatus;
    }

    public ArrayList<Pickup> getPickups() {
        return pickups;
    }

    public int getNPickups() {
        return nPickups;
    }

    public static int getNTotalPickups() {
        return nTotalPickups;
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
