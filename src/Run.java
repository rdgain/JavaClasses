import core.ForwardModel;
import core.GameState;
import core.Player;
import players.RandomPlayer;

import java.util.Arrays;


public class Run {

    public static void main(String[] args) {
        // Player array with 6 instances of the random player, randomly chosen different seeds
        Player[] players = new Player[] {
                new RandomPlayer(0),
                new RandomPlayer(1),
                new RandomPlayer(2),
                new RandomPlayer(3),
                new RandomPlayer(4),
                new RandomPlayer(5)
        };

        // Create game state and forward model instances
        GameState gameState = new GameState();
        ForwardModel forwardModel = new ForwardModel();

        // Setup the game, print game state
        forwardModel.setup(players, gameState);
        System.out.println(gameState.toString());

        // Game loop
        while (!gameState.gameEnded) {
            // Get actions from players
            int[] actions = new int[players.length];
            for (int i = 0; i < players.length; i++) {
                actions[i] = players[i].act(gameState.copy());
            }

            // Advance game state by one tick
            forwardModel.next(actions, gameState);

            // Print game state and actions played
            System.out.println("Actions played: " + Arrays.toString(actions));
            System.out.println(gameState.toString());
        }

        // Final print of game status for all players
        System.out.println("Game ended! Player results: ");
        for (Player p: players) {
            System.out.print(p.gameStatus + " ");
        }
    }
}
