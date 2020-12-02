package players;

import core.GameState;
import core.Player;

public class RandomSearch extends Player {
    int rolloutLength = 50;
    int nIterations = 50;

    public RandomSearch() {
        this(null);
    }

    public RandomSearch(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState) {
        int nActions = 6;

        double maxValue = Double.NEGATIVE_INFINITY;
        int bestAction = -1;

        for (int i = 0; i < nIterations; i++) {

            int[] actionSequence = new int[rolloutLength];
            for (int r = 0; r < rolloutLength; r++) {
                actionSequence[r] = randomGenerator.nextInt(nActions);
            }
            double value = evaluate(actionSequence, gameState.copy());

            if (value > maxValue) {
                maxValue = value;
                bestAction = actionSequence[0];
            }
        }

        return bestAction;
    }

    private double evaluate(int[] actionSequence, GameState gameState) {
        int nActions = 6;
        int nPlayers = 6;

        for (int action : actionSequence) {
            int[] actions = new int[nPlayers];
            for (int p = 0; p < nPlayers; p++) {
                if (p != getPlayerID()) {
                    actions[p] = randomGenerator.nextInt(nActions);
                } else {
                    actions[p] = action;
                }
            }

            forwardModel.next(actions, gameState);
        }

        return evaluate(gameState);
    }

    private double evaluate(GameState gameState) {
        int gameStatus = gameState.getGameStatus(playerID);
        if (gameStatus != -2) {
            return gameStatus;
        }
        return getScore();
    }

    @Override
    protected Player _copy() {
        return new RandomSearch();
    }
}
