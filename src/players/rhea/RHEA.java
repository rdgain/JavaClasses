package players.rhea;

import core.GameState;
import core.Player;

public class RHEA extends Player {
    int rolloutLength = 50;
    int nIterations = 50;
    double mutateProb = 0.3;

    public RHEA() {
        this(null);
    }

    public RHEA(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState) {
        int nActions = 6;

        int[] actionSequence = new int[rolloutLength];
        for (int i = 0; i < rolloutLength; i++) {
            actionSequence[i] = randomGenerator.nextInt(nActions);
        }
        double value = evaluate(actionSequence, gameState.copy());

        for (int i = 0; i < nIterations; i++) {

            int[] mutatedSequence = mutate(actionSequence);
            double mutatedValue = evaluate(mutatedSequence, gameState.copy());

            if (mutatedValue > value) {
                actionSequence = mutatedSequence.clone();
                value = mutatedValue;
            }

        }

        return actionSequence[0];
    }

    private int[] mutate(int[] actionSequence) {
        int nActions = 6;

        int[] mutation = actionSequence.clone();

        for (int i = 0; i < actionSequence.length; i++) {
            if (randomGenerator.nextDouble() < mutateProb) {
                actionSequence[i] = randomGenerator.nextInt(nActions);
            }
        }

        return mutation;
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
        return new RHEA();
    }
}
