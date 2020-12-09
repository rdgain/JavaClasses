package players.mcts;

import core.GameState;
import core.Player;
import mazeGraphDraw.Vector2D;
import utils.Utils;

import java.awt.*;

import static utils.Utils.posToScreenCoords;

public class MCTS extends Player {

    int nIterations = 50;
    int rolloutLength = 100;

    TreeNode root;

    boolean drawing = true;

    public MCTS() {
        this(null);
    }

    public MCTS(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState) {
        root = new TreeNode(null, -1, null);

        for (int i = 0; i < nIterations; i++) {
            GameState gsCopy = gameState.copy();

            // Selection
            TreeNode selected = root;
            while (selected.isFullyExpanded()) {
                selected = selected.treePolicy();
                forwardModel.next(getActions(selected.childIdx), gsCopy);
            }

            // Expansion
            TreeNode newNode = selected.addRandomChild(randomGenerator);
            forwardModel.next(getActions(newNode.childIdx), gsCopy);

            // Simulation
            for (int r = 0; r < rolloutLength; r++) {
                forwardModel.next(getActions(randomGenerator.nextInt(6)), gsCopy);
            }

            // Backpropagation
            double value = evaluate(gsCopy);
            newNode.backpropagate(value);
        }

        return root.mostVisitedAction();
    }

    /**
     * Evaluate a game state. Returns game score, unless the player's win status has been decided, in which case it
     * returns that multiplied to wrap around the score values. Score is the number of pickups in a level, and
     * there are always maximum gridWidth*gridHeight pickups.
     * @param gameState - game state to evaluate
     * @return - value of state
     */
    private double evaluate(GameState gameState) {
        int gameStatus = gameState.getGameStatus(playerID);
        if (gameStatus != -2) {
            return gameStatus * gameState.getWidth() * gameState.getHeight();
        }
        return getScore();
    }

    /**
     * Returns a list of actions, one for each player, given my action decided and placed at the correct index.
     * @param myAction - action for me to execute.
     * @return - actions array
     */
    private int[] getActions(int myAction) {
        int[] actions = new int[6];  // 6 players
        for (int i = 0; i < actions.length; i++) {
            if (i == playerID) {
                actions[i] = myAction;
            } else {
                actions[i] = randomGenerator.nextInt(6);  // 6 possible actions, random opponents
            }
        }
        return actions;
    }

    @Override
    protected Player _copy() {
        MCTS copy = new MCTS();
        copy.root = root;
        return copy;
    }

    @Override
    public void draw(Graphics2D g, GameState gs, int cellSize, int offsetX, int offsetY) {
        if (root != null && drawing) {
            Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(1));
            drawNode(root, g, gs, cellSize, offsetX, offsetY);
            g.setStroke(s);
        }
    }

    private void drawNode(TreeNode node, Graphics2D g, GameState gs, int cellSize, int offsetX, int offsetY) {
        // Draw the node
        // Find color depending on value of node
        double v = Utils.normalise((node.sumQ/node.nVisits), node.qBounds[0], node.qBounds[1]);
        int frac = (int) Math.max(0, Math.min(255, 255*v));

        // Find position of player in this node
        int pos = gs.getPlayers()[playerID].getPosition();
        Vector2D pos2D = posToScreenCoords(pos, gs.getWidth(), cellSize);

        // Draw node
        g.setColor(Color.black);
        g.drawOval(offsetX + pos2D.getX(), offsetY + pos2D.getY(), cellSize, cellSize);
        g.setColor(new Color(255-frac, frac, 25, 20));
        g.fillOval(offsetX + pos2D.getX(), offsetY + pos2D.getY(), cellSize, cellSize);

        // Draw the children
        for (TreeNode child: node.children) {
            if (child != null) {
                GameState gsCopy = gs.copy();
                forwardModel.next(getActions(child.childIdx), gsCopy);
                drawNode(child, g, gsCopy, cellSize, offsetX, offsetY);
            }
        }
    }
}
