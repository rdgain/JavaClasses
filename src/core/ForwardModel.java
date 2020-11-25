package core;

import utils.GraphNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import static core.GameState.nTeams;

public class ForwardModel {

    /**
     * Set up the initial state of the game
     * @param players - players for the game
     * @param gameState - game state to set up
     */
    public void setup(Player[] players, GameState gameState) {
        gameState.gameTick = 0;
        gameState.gameEnded = false;
        gameState.button = new Button(3);
        gameState.players = players;
        for (int i = 0; i < players.length; i++) {
            players[i].playerID = i;
            players[i].teamID = (i < players.length/2? 0 : 1);  // TODO: modify to work with any number of teams nTeams
            players[i].position = new Random().nextInt(288);
            players[i].forwardModel = this;
        }

//        gameState.maze = new HashMap<>();
        GraphNode.Container container = GraphNode.readMazeFromFile();
        gameState.maze = container.graph;
        gameState.width = container.width;
        gameState.height = container.height;
    }

    /**
     * Advance the game state by one tick by applying game rules and player actions
     * @param actions - player actions, index corresponds to player ID
     * @param gameState - game state to modify
     */
    public void next(int[] actions, GameState gameState) {
        gameState.gameTick += 1;

        // Handle actions
        for (int i = 0; i < actions.length; i++) {
            int action = actions[i];

            if (action == 0) continue; // Do nothing

            int position = gameState.players[i].position;
            HashMap<Integer, Integer> connections = gameState.maze.get(position).connections;

            if (action == 5) {
                // Tag a player
                Collection<Integer> neighbouringPositions = connections.values();
                int neighbourPlayers = 0;
                int neighbour = -1;
                for (int p = 0; p < gameState.players.length; p++) {
                    if (p != i) {
                        if (neighbouringPositions.contains(gameState.players[p].position)) {
                            neighbourPlayers ++;
                            neighbour = p;
                        }
                    }
                }
                if (neighbourPlayers == 1) {
                    gameState.players[neighbour].setTagged(gameState.players[i]);
                }
            } else {
                // Move
                if (connections.containsKey(action)) {
                    gameState.players[i].position = connections.get(action);
                } else {
                    System.out.println("Illegal move action ");
                }
            }
        }

        // Check if player pressed the button
        int nPlayersAlive = 0;
        int playerAliveIdx = -1;
        for (Player p: gameState.players) {
            if (p.gameStatus == -2) {
                if (p.position == gameState.button.position) {
                    gameState.button.press(p);
                }
                nPlayersAlive++;
                playerAliveIdx = p.playerID;
            }
        }

        // Tick the button
        gameState.button.tick();

        // Check if the game has ended
        checkGameEnd(gameState, nPlayersAlive, playerAliveIdx);
    }

    /**
     * Checks if the game ended according to game rules (1 player left standing, or maximum number of game ticks).
     * @param gameState - current game state to check
     * @param nPlayersAlive - number of players still alive in the game
     * @param playerAliveIdx - index of last alive player
     */
    private void checkGameEnd(GameState gameState, int nPlayersAlive, int playerAliveIdx) {
        // The last player standing wins.
        if (nPlayersAlive == 1) {
            // The game has ended!
            gameState.gameEnded = true;
            gameState.players[playerAliveIdx].gameStatus = 1;
        }
        // The game also ends after X game ticks.
        if (gameState.gameTick >= GameState.maxGameTicks) {
            // The game has ended!
            gameState.gameEnded = true;

            boolean multiplesOnATeam = false;  // True if at least one team has more than 1 player still alive
            int[] nPlayersAlivePerTeam = new int[nTeams];
            for (Player p: gameState.players) {
                if (p.gameStatus == -2) {
                    nPlayersAlivePerTeam[p.teamID] ++;
                    if (nPlayersAlivePerTeam[p.teamID] > 1) multiplesOnATeam = true;
                }
            }
            boolean soloWinner = false;
            for (int i = 0; i < nTeams; i++) {
                if (nPlayersAlivePerTeam[i] == 1) {
                    soloWinner = true;
                    break;
                }
            }
            for (int i = 0; i < nTeams; i++) {
                //  If multiple players from the same team are alive at the end, but
                //  only 1 player from the other team, the 1 solo player wins and everyone else loses.
                if (nPlayersAlivePerTeam[i] == 1) {
                    // The player on team i wins, everyone else loses! - unless all teams have 1 or less players alive
                    if (multiplesOnATeam) {
                        // This player wins, everyone else loses
                        for (Player p: gameState.players) {
                            if (p.gameStatus == -2 && p.teamID == i) {
                                p.gameStatus = 1;
                            } else if (p.gameStatus == -2) {
                                p.gameStatus = -1;
                            }
                        }
                        break;
                    } else {
                        // If all teams have 1 or less players still alive, they tie.
                        for (Player p: gameState.players) {
                            if (p.gameStatus == -2) {
                                p.gameStatus = 0;
                            }
                        }
                    }
                } else if (!soloWinner) {
                    // If multiple players from both teams are still alive, everyone loses.
                    for (Player p: gameState.players) {
                        if (p.gameStatus == -2) {
                            p.gameStatus = -1;
                        }
                    }
                }
            }
        }
    }
}
