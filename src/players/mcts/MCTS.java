package players.mcts;

import core.GameState;
import core.Player;
import graphToGridDraw.GUI;
import mazeGraphDraw.Vector2D;
import utils.Utils;

import java.awt.*;
import java.util.ArrayList;

import static utils.Utils.*;

public class MCTS extends Player {

    int nIterations = 500;
    int rolloutLength = 5;
    boolean saveRolloutNodes = false;
    TreeNode root;

    public MCTS() {
        this(null);
    }

    public MCTS(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState, GUI gui) {
        if (drawingIteration && visuals) {
            gameState.getPlayers()[playerID] = this;
            gui.update(gameState, false);
        }

        root = new TreeNode(null, -1, null);

        ArrayList<Integer> iterPos = new ArrayList<>();;
        for (int i = 0; i < nIterations; i++) {
            GameState gsCopy = gameState.copy();
            if (drawingIteration && visuals) {
                iterPos.clear();
            }

            // Selection
            TreeNode selected = root;
            while (selected.isFullyExpanded()) {
                selected = selected.treePolicy();
                forwardModel.next(getActions(selected.childIdx), gsCopy);
                if (drawingIteration && visuals) {
                    iterPos.add(gsCopy.getPlayers()[playerID].getPosition());
                }
            }

            // Expansion
            TreeNode newNode = selected.addRandomChild(randomGenerator);
            forwardModel.next(getActions(newNode.childIdx), gsCopy);
            if (drawingIteration && visuals) {
                iterPos.add(gsCopy.getPlayers()[playerID].getPosition());
            }

            // Simulation
            for (int r = 0; r < rolloutLength; r++) {
                int act = randomGenerator.nextInt(6);
                if (saveRolloutNodes) {
                    newNode = newNode.addChild(act);
                }
                forwardModel.next(getActions(act), gsCopy);
                if (drawingIteration && visuals) {
                    iterPos.add(gsCopy.getPlayers()[playerID].getPosition());
                }
            }

            // Backpropagation
            double value = evaluate(gsCopy);
            newNode.backpropagate(value);

            if (drawingIteration && visuals) {
                gui.drawPosSequence(iterPos);
                gui.repaint();

                try {
                    Thread.sleep(frameDelayIter);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return root.mostVisitedAction();
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
        copy.positionHistory = positionHistory;
        return copy;
    }

    @Override
    public void draw(Graphics2D g, GameState gs, int cellSize, int offsetX, int offsetY) {
        if (root != null) {
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
