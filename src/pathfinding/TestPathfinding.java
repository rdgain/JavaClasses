package pathfinding;

import core.ForwardModel;
import core.GameState;
import core.Player;
import players.RandomPlayer;

public class TestPathfinding {

    public static void main(String[] args) {
        // Set up environment and GUI
        Player[] players = new Player[] {
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer()
        };
        GameState gameState = new GameState();
        ForwardModel forwardModel = new ForwardModel();
        forwardModel.setup(players, gameState);
        GUIPathfinding gui = new GUIPathfinding(gameState);

        Pathfinding pf = new AStar();  // TODO swap with any algorithm

        //noinspection InfiniteLoopStatement
        while (true) {
            if (gui.start) {
                gui.start = false;
                int fromID = -1;
                try {
                    fromID = Integer.parseInt(gui.from.getText());
                } catch (Exception ignored) {}
                int toID = -1;
                try {
                    toID = Integer.parseInt(gui.to.getText());
                } catch (Exception ignored) {}

                gui.gameView.from = fromID;
                gui.gameView.to = toID;

                if (fromID != -1) {
                    pf.run(gui.gs, gui, fromID, toID);
                } else {
                    System.out.println("ERROR: Did not start. Should provide a starting point by inputting a number " +
                            "in the first text field, or clicking a cell in the grid.");
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
