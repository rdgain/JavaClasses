package pathfinding;

import core.GameState;

import java.util.LinkedList;
import java.util.Queue;

public class BFS implements Pathfinding {
    boolean[] visited;
    Queue<Integer> queue;
    long frameDelay = 50;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {

        // TODO Mark all the vertices as not visited (By default set as false)

        // TODO Create a queue for BFS

        // TODO Mark the current node as visited and enqueue it

        // GUI update
        gui.update(visited, queue);

        // Loop while there are still elements in the queue
        while (queue.size() != 0) {
            // TODO Dequeue a vertex from queue

            // TODO Check if this is destination

            // TODO Get all adjacent vertices of the dequeued vertex 'current'
            // TODO If neighbour has not been visited, then mark it visited and enqueue it

            // GUI update
            gui.update(visited, queue);
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
