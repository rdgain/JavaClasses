package players;

import core.GameState;
import core.Player;

public class FlatMC extends Player {
    int rolloutLength = 50;
    int nIterations = 50;

    public FlatMC() {
        this(null);
    }

    public FlatMC(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState) {
        int nActions = 6;
        int nPlayers = 6;

        double maxValue = Double.NEGATIVE_INFINITY;
        int bestAction = -1;

        for (int a = 0; a < nActions; a++) {
            double value = 0;

            for (int i = 0; i < nIterations; i++) {
                GameState gsCopy = gameState.copy();

                for (int r = 0; r < rolloutLength; r++) {
                    int[] actions = new int[nPlayers];
                    for (int j = 0; j < nPlayers; j++) {
                        actions[j] = randomGenerator.nextInt(nActions);
                    }
                    forwardModel.next(actions, gsCopy);
                }

                value += evaluate(gsCopy);
            }

            if (value/nIterations > maxValue) {
                bestAction = a;
                maxValue = value/nIterations;
            }
        }

        return bestAction;
    }

    private double evaluate(GameState gsCopy) {
        int gameStatus = gsCopy.getGameStatus(playerID);
        if (gameStatus != -2) {
            return gameStatus;
        }
        return getScore();
    }

    @Override
    protected Player _copy() {
        return new FlatMC();
    }
}
