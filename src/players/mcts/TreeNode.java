package players.mcts;

import utils.Utils;

import java.util.Random;

public class TreeNode {

    TreeNode parent;
    int childIdx;
    TreeNode[] children;
    double C = Math.sqrt(2);
    double epsilon = 1e-6;  // Small constant, controls amount of noise added

    double[] qBounds;
    double sumQ;  // the sum of values seen in iterations through this node
    int nVisits;  // number of times the node was visited

    public TreeNode(TreeNode parent, int childIdx, double[] qBounds) {
        this.parent = parent;
        this.childIdx = childIdx;
        this.children = new TreeNode[6];  // 6 actions
        this.qBounds = qBounds;
        if (qBounds == null) {
            this.qBounds = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        }

        this.sumQ = 0;
        this.nVisits = 0;
    }

    public boolean isFullyExpanded() {
        for (TreeNode child : children) {
            if (child == null) {
                return false;
            }
        }
        return true;
    }

    public TreeNode treePolicy() {
        TreeNode selected = null;
        double maxValue = Double.NEGATIVE_INFINITY;

        for (TreeNode child: children) {
            if (child != null) {
                double averageQ = child.sumQ / (child.nVisits+epsilon);
                averageQ = Utils.normalise(averageQ, qBounds[0], qBounds[1]);
                double log = Math.log(nVisits+epsilon);
                double sqrt = Math.sqrt(log / (child.nVisits+epsilon));
                double ucbValue = averageQ + C * sqrt;

                if (ucbValue > maxValue) {
                    maxValue = ucbValue;
                    selected = child;
                }
            }
        }

        return selected;
    }

    public TreeNode addRandomChild(Random randomGenerator) {
        int randomChoice = -1;
        double randomValue = -1;

        for (int i = 0; i < children.length; i++) {
            double v = randomGenerator.nextDouble();
            if (children[i] == null && v > randomValue) {
                randomChoice = i;
                randomValue = v;
            }
        }

        return addChild(randomChoice);
    }

    public TreeNode addChild(int idx) {
        if (children[idx] == null) {
            TreeNode child = new TreeNode(this, idx, qBounds);
            children[idx] = child;
            return child;
        }
        return null;
    }

    public void backpropagate(double value) {
        if (value > qBounds[1]) qBounds[1] = value;
        if (value < qBounds[0]) qBounds[0] = value;

        TreeNode node = this;
        while (node != null) {
            node.sumQ += value;
            node.nVisits += 1;
            node = node.parent;
        }
    }

    public int mostVisitedAction() {
        int mostVisited = -1;
        double maxVisits = 0;

        for (int i = 0; i < children.length; i++) {
            double v = Utils.noise(children[i].nVisits, epsilon, new Random().nextDouble());
            if (v > maxVisits) {
                maxVisits = v;
                mostVisited = i;
            }
        }

        return mostVisited;
    }
}
