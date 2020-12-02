package pathfinding;

import core.GameState;

import java.util.Stack;

public class DFS implements Pathfinding {
    boolean[] visited;
    Stack<Integer> stack;
    long frameDelay = 50;

    public void run(GameState gameState, GUIPathfinding gui, int from, int to) {

        // TODO Mark all the vertices as not visited (By default set as false)
        visited = new boolean[gameState.getMaze().size()];

        // TODO Create a stack for DFS
        stack = new Stack<>();

        // TODO Mark the current node as visited and add it to the stack
        visited[from] = true;
        stack.add(from);

        // GUI update
        gui.update(visited, stack, null);

        // Loop while there are still elements in the queue
        while (stack.size() != 0) {
            // TODO Dequeue a vertex from stack
            int current = stack.pop();

            // TODO Check if this is destination
            if (current == to && to != -1) {
                break;
            }

            // TODO Get all adjacent vertices of the dequeued vertex 'current'
            // TODO If neighbour has not been visited, then mark it visited and add it to the stack
            for (int node : gameState.getMaze().get(current).connections.values()) {
                if (!visited[node]) {
                    visited[node] = true;
                    stack.add(node);
                }
            }

            // GUI update
            gui.update(visited, stack, null);
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
