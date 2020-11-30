package pathfinding;

import core.GameState;

public interface Pathfinding {
    void run(GameState gameState, GUIPathfinding gui, int from, int to);
}
