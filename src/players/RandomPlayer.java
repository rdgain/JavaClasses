package players;

import core.GameState;
import core.Player;

public class RandomPlayer extends Player {

    public RandomPlayer() {
        this(null);
    }
    public RandomPlayer(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState) {
        return randomGenerator.nextInt(6);
    }

    @Override
    protected Player _copy() {
        return new RandomPlayer();
    }
}
