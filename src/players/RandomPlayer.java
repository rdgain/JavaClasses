package players;

import core.Player;

public class RandomPlayer extends Player {

    public RandomPlayer(long randomSeed) {
        super(randomSeed);
    }

    @Override
    protected int act() {
        return random.nextInt(6);
    }
}
