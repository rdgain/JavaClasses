package pathfinding;

import core.GameState;
import utils.GraphNode;

import java.util.*;

public class Dijkstra implements Pathfinding {
    long frameDelay = 50;
    boolean[] visited;
    Queue<HeuristicNode> queue;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {
        // Transform graph nodes to hold extra values (distance from origin / to destination)
        HashMap<Integer, HeuristicNode> maze = new HashMap<>();
        for (Map.Entry<Integer, GraphNode> e: gameState.getMaze().entrySet()) {
            maze.put(e.getKey(), new HeuristicNode(e.getValue()));
        }

        // TODO Mark all the vertices as not visited (By default set as false)

        // TODO Create a priority Queue.

        // TODO Mark the current node as visited and enqueue it

        // GUI update
        gui.update(visited, objToID(queue));

        // Loop while there are still elements in the queue
        while (queue.size() != 0) {
            // TODO Dequeue a vertex from queue

            // TODO Check if this is destination

            // TODO Get all adjacent vertices of the dequeued vertex 'current'
            // TODO Calculate distance from origin for all neighbours, 1 more than current's
            // TODO If neighbour has not been visited or the distance found is smaller, then mark it visited, update distance and enqueue it

            // GUI update
            gui.update(visited, objToID(queue));
            try {
                Thread.sleep(frameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Reset GUI variables
        gui.gameView.from = -1;
        gui.gameView.to = -1;
        gui.from.setText("");
        gui.to.setText("");
    }

    /**
     * Transforms a list of HeuristicNode objects to a list of the object IDs
     * @param queue list of objects
     * @return list of IDs
     */
    private Queue<Integer> objToID(Queue<HeuristicNode> queue) {
        LinkedList<Integer> list = new LinkedList<>();
        for (HeuristicNode n: queue) {
            list.add(n.id);
        }
        return list;
    }

}
