package pathfinding;

import core.GameState;

import java.util.LinkedList;
import java.util.Queue;

public class BFS implements Pathfinding {
    boolean[] visited;
    Queue<Integer> queue;
    long frameDelay = 50;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {

        // Mark all the vertices as not visited (By default set as false)
        visited = new boolean[gameState.getMaze().size()];

        // Create a queue for BFS
        queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        visited[from] = true;
        queue.add(from);

        // GUI update
        gui.update(visited, queue, null);

        // Loop while there are still elements in the queue
        while (queue.size() != 0) {
            // Dequeue a vertex from queue
            int current = queue.poll();

            // Check if this is destination
            if (current == to && to != -1) {
                break;
            }

            // Get all adjacent vertices of the dequeued vertex 'current'
            // If neighbour has not been visited, then mark it visited and enqueue it
            for (int node : gameState.getMaze().get(current).connections.values()) {
                if (!visited[node]) {
                    visited[node] = true;
                    queue.add(node);
                }
            }

            // GUI update
            gui.update(visited, queue, null);
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

}
