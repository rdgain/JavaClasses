package core;

import java.util.Random;

public abstract class Player {

    int playerID;
    long randomSeed;
    protected Random random;
    int teamID;
    int gameStatus;  // -2 = undecided, -1 = loss, 0 = tie, 1 = win
    int nOpponentsTagged;
    int position;

    public Player(long randomSeed) {
        this.randomSeed = randomSeed;
        setGameStatus(-2);
        nOpponentsTagged = 0;
        random = new Random(randomSeed);
    }

    void setTagged(Player other) {
        setGameStatus(-1);
        other.nOpponentsTagged++;
    }

    void setGameStatus(int newStatus) {
        this.gameStatus = newStatus;
    }

    void swapTeam() {
        teamID = (teamID + 1) % 2;

//        if (teamID == 0) teamID = 1;
//        else teamID = 0;
    }

    /**
     * @return an action, 0 - do nothing, 1 - up, 2 - right, 3 - down, 4 - left, 5 - tag
     */
    protected abstract int act();

}
