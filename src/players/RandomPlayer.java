package players;

import core.GameState;
import core.Player;
import graphToGridDraw.GUI;

public class RandomPlayer extends Player {

    public RandomPlayer() {
        this(null);
    }
    public RandomPlayer(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState, GUI gui) {
        return randomGenerator.nextInt(6);
    }

    @Override
    protected Player _copy() {
        return new RandomPlayer();
    }
}
