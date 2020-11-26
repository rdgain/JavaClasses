import core.ForwardModel;
import core.GameState;
import core.Player;
import players.RandomPlayer;
import utils.StatSummary;

public class Run {

    public static void main(String[] args) {
        // Player array with 6 instances of the random player, randomly chosen different seeds
        Player[] players = new Player[] {
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer(),
                new RandomPlayer()
        };
        StatSummary[] stats = new StatSummary[players.length];
        for (int i = 0; i < players.length; i++) {
            stats[i] = new StatSummary("Player " + i + " [" + players[i].getClass().getName() + "]");
        }

        // Create game state and forward model instances
        GameState gameState = new GameState();
        ForwardModel forwardModel = new ForwardModel();
        int nReps = 10;

        for (int r = 0; r < nReps; r++) {

            // Setup the game, print game state
            forwardModel.setup(players, gameState);
//            System.out.println(gameState.toString());

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
//            System.out.println("Actions played: " + Arrays.toString(actions));
//            System.out.println(gameState.toString());
            }

            // Final print of game status for all players
            System.out.print("Game ended! Player results: ");
            for (int i = 0; i < players.length; i++) {
                int status = players[i].gameStatus;
                System.out.print(status + " ");
                stats[i].add(status);
                if (status == 1) stats[i].addWin();
                if (status == 0) stats[i].addDraw();
                if (status == -1) stats[i].addLoss();
            }
            System.out.println();
        }

        // All games finished! Print stats for each player
        for (StatSummary ss: stats) {
//            System.out.println(ss.toString());
            System.out.println(ss.shortString());
        }
    }
}
