package core;

import java.util.Random;

import static core.GameState.nTeams;

public abstract class Player {
    protected ForwardModel forwardModel;
    protected int playerID;
    long randomSeed;
    protected Random randomGenerator;
    int teamID;
    public int gameStatus;  // -2 = undecided, -1 = lose, 0 = tie, 1 = win  TODO: turn into an enum to remove hardcoded variables
    int nOpponentsTagged;
    int position;
    int score;

    public Player(Long randomSeed) {
        if (randomSeed == null) {
            randomGenerator = new Random();
        } else {
            this.randomSeed = randomSeed;
            randomGenerator = new Random(randomSeed);
        }
        this.gameStatus = -2;
        nOpponentsTagged = 0;
    }

    /**
     * This player was tagged by another!
     * @param other - the player who just tagged this player
     */
    void setTagged(Player other) {
        this.gameStatus = -1;
        other.nOpponentsTagged++;
    }

    /**
     * Swap this player's team
     */
    void swapTeam() {
        teamID = (teamID + 1) % nTeams;

//        if (teamID == 0) teamID = 1;
//        else teamID = 0;
    }

    /**
     * Get an action from this player, abstract to be implemented by subclasses.
     * @param gameState - current game state
     * @return - integer, 0 - do nothing, 1 - up, 2 - right, 3 - down, 4 - left, 5 - tag
     */
    public abstract int act(GameState gameState);

    /**
     * Copy the state of the player
     * @return - a new Player object with the same state
     */
    protected abstract Player _copy();

    /**
     * Super class copy, copies all properties in the super class (so subclasses don't need to worry about it).
     * @return - a new Player object with the same state
     */
    public Player copy() {
        Player p = _copy();
        p.playerID = playerID;
        p.randomSeed = randomSeed;
        p.randomGenerator = new Random(randomSeed);
        p.teamID = teamID;
        p.gameStatus = gameStatus;
        p.nOpponentsTagged = nOpponentsTagged;
        p.position = position;
        p.score = score;
        return p;
    }

    public int getPosition() {
        return position;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public int getTeamID() {
        return teamID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerID=" + playerID +
                ", teamID=" + teamID +
                ", gameStatus=" + gameStatus +
                ", nOpponentsTagged=" + nOpponentsTagged +
                ", position=" + position +
                ", score=" + score +
                '}';
    }
}
