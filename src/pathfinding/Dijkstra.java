package pathfinding;

import core.GameState;
import utils.GraphNode;

import java.util.*;

public class Dijkstra implements Pathfinding {
    long frameDelay = 50;
    boolean[] visited;
    Queue<HeuristicNode> queue;
    ArrayList<Integer> path;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {
        // Transform graph nodes to hold extra values (distance from origin / to destination)
        HashMap<Integer, HeuristicNode> maze = new HashMap<>();
        for (Map.Entry<Integer, GraphNode> e: gameState.getMaze().entrySet()) {
            maze.put(e.getKey(), new HeuristicNode(e.getValue()));
        }

        //  Mark all the vertices as not visited (By default set as false)
        visited = new boolean[gameState.getMaze().size()];

        //  Create a priority Queue.
        queue = new PriorityQueue<>();

        //  Mark the current node as visited and enqueue it
        visited[from] = true;
        maze.get(from).parent = -1;
        queue.add(maze.get(from));

        // GUI update
        gui.update(visited, objToID(queue), path);

        // Loop while there are still elements in the queue
        while (queue.size() != 0) {
            //  Dequeue a vertex from queue
            HeuristicNode current = queue.poll();

            //  Check if this is destination
            if (current.id == to && to != -1) {
                break;
            }

            //  Get all adjacent vertices of the dequeued vertex 'current'
            //  Calculate distance from origin for all neighbours, 1 more than current's
            //  If neighbour has not been visited or the distance found is smaller, then mark it visited, update distance and enqueue it
            for (int next: current.connections.values()) {
                HeuristicNode nextNode = maze.get(next);

                int distanceFrom = current.distanceFrom + 1;
                if (!visited[next] || distanceFrom < nextNode.distanceFrom) {
                    visited[next] = true;
                    nextNode.distanceFrom = distanceFrom;
                    nextNode.parent = current.id;
                    queue.add(nextNode);
                }
            }

            // GUI update
            gui.update(visited, objToID(queue), path);
            try {
                Thread.sleep(frameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        path = reconstructPath(maze, to);
        // GUI update
        gui.update(visited, objToID(queue), path);
        try {
            Thread.sleep(frameDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reset GUI variables
        gui.gameView.from = -1;
        gui.gameView.to = -1;
        gui.from.setText("");
        gui.to.setText("");
    }

    private ArrayList<Integer> reconstructPath(HashMap<Integer, HeuristicNode> maze, int to) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(to);
        HeuristicNode current = maze.get(to);

        while (current.parent != -1) {
            current = maze.get(current.parent);
            path.add(current.id);
        }

        return path;
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
