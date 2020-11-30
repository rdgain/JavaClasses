package pathfinding;

import core.GameState;
import utils.GraphNode;

import java.util.*;

public class AStar implements Pathfinding {
    long frameDelay = 50;
    boolean[] visited;
    Queue<HeuristicNode> queue;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {
        // Transform graph nodes to hold extra values
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
            // TODO Calculate approximate distance to target for all neighbours, and sum up the 2 values
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

    /**
     * Calculates Euclidian distance between 2 node IDs
     * @param from - one node
     * @param to - another node
     * @param gridWidth - width of grid
     * @return - distance between nodes
     */
    private int distanceBetween(int from, int to, int gridWidth) {
        int xFrom = from % gridWidth;
        int yFrom = from / gridWidth;
        int xTo = to % gridWidth;
        int yTo = to / gridWidth;

        return (int) Math.sqrt((xFrom - xTo) * (xFrom - xTo) + (yFrom - yTo) * (yFrom - yTo));
    }

}
