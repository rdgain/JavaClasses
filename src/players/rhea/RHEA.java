package players.rhea;

import core.GameState;
import core.Player;
import graphToGridDraw.GUI;
import mazeGraphDraw.Vector2D;
import utils.Utils;

import java.awt.*;
import java.util.HashMap;

import static utils.Utils.posToScreenCoords;

public class RHEA extends Player {
    int rolloutLength = 50;  // length of action sequences
    int nIterations = 50;  // Number of iterations
    double mutateProb = 0.3;  // Mutation probability
    double epsilon = 1e-6;  // Small constant, controls amount of noise added

    HashMap<Individual, Double> sequences;  // History of action sequences, mapping to the highest value associated to sequence
    Individual actionSequence;  // Action sequence chosen for execution
    double[] qBounds;  // Value bounds (lowest and highest fitness values found)

    boolean drawing = false;  // If true, drawing action sequences

    public RHEA() {
        this(null);
    }

    public RHEA(Long randomSeed) {
        super(randomSeed);
    }

    @Override
    public int act(GameState gameState, GUI gui) {
        // Initialise variables for game tick
        int nActions = 6;
        sequences = new HashMap<>();
        qBounds = new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};

        // Initialise action sequence
        actionSequence = new Individual(rolloutLength);
        for (int i = 0; i < rolloutLength; i++) {
            actionSequence.set(i, randomGenerator.nextInt(nActions));
        }
        // Evaluate sequence
        double value = evaluate(actionSequence, gameState.copy());

        // Add it to the history of sequences
        sequences.put(actionSequence.copy(), value);

        // Create nIterations other mutations of the sequence, evaluate and keep the best one each time
        for (int i = 0; i < nIterations; i++) {
            Individual mutatedSequence = mutate(actionSequence);
            double mutatedValue = evaluate(mutatedSequence, gameState.copy());

            if (!sequences.containsKey(mutatedSequence) || sequences.get(mutatedSequence) < mutatedValue) {
                sequences.put(mutatedSequence.copy(), mutatedValue);
            }

            if (mutatedValue > value) {
                actionSequence = mutatedSequence;
                value = mutatedValue;
            }

        }

        // Return first action of best sequence found
        return actionSequence.getFirst();
    }

    /**
     * Mutate an individual
     * @param actionSequence - individual to mutate
     * @return - mutation
     */
    private Individual mutate(Individual actionSequence) {
        int nActions = 6;

        // Make copy of current individual
        Individual mutation = actionSequence.copy();

        // Use random generator against mutation probability to change some of the genes to new random values
        for (int i = 0; i < actionSequence.length(); i++) {
            if (randomGenerator.nextDouble() < mutateProb) {
                mutation.set(i, randomGenerator.nextInt(nActions));
            }
        }

        // Return mutation
        return mutation;
    }

    /**
     * Evaluate an individual
     * @param ind - individual to evaluate
     * @param gameState - starting game state
     * @return - value of individual
     */
    private double evaluate(Individual ind, GameState gameState) {
        // Iterate through actions and use forward model to advance the game state with each one
        for (int action : ind.actionSequence) {
            forwardModel.next(getActions(action), gameState);
        }

        // Get the value of the final game state reached and update bounds
        double q = evaluate(gameState);
        if (q > qBounds[1]) qBounds[1] = q;
        if (q < qBounds[0]) qBounds[0] = q;

        // Return value with small noise applied to distinguish between equal values
        return Utils.noise(q, epsilon, randomGenerator.nextDouble());
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
        // NOT a deep copy. This is only used for drawing purposes, so not having a deep copy is fine.
        RHEA copy = new RHEA();
        copy.sequences = sequences;
        copy.qBounds = qBounds;
        copy.actionSequence = actionSequence;
        copy.drawing = drawing;
        return copy;
    }

    /**
     * Draws action sequences explored by RHEA in a game tick, highlighting the chosen one with red borders.
     * Green circles indicate good sequences, red indicate bad.
     */
    @Override
    public void draw(Graphics2D g, GameState gs, int cellSize, int offsetX, int offsetY) {
        if (sequences != null && drawing) {
            Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(1));
            for (Individual seq : sequences.keySet()) {
                GameState gsCopy = gs.copy();
                drawActionSequence(g, gsCopy, cellSize, offsetX, offsetY, seq, false);
            }
            drawActionSequence(g, gs.copy(), cellSize, offsetX, offsetY, actionSequence, true);
            g.setStroke(s);
        }
    }

    private void drawActionSequence(Graphics2D g, GameState gsCopy, int cellSize, int offsetX, int offsetY, Individual seq, boolean chosen) {
        for (int a: seq.actionSequence) {
            forwardModel.next(getActions(a), gsCopy);

            // Find color depending on value of node
            double v = Utils.normalise(sequences.get(seq), qBounds[0], qBounds[1]);
            int frac = (int) Math.max(0, Math.min(255, 255*v));

            // Find position of player in this node
            int pos = gsCopy.getPlayers()[playerID].getPosition();
            Vector2D pos2D = posToScreenCoords(pos, gsCopy.getWidth(), cellSize);

            // Draw node
            if (chosen) g.setColor(Color.red);
            else g.setColor(Color.black);
            g.drawOval(offsetX + pos2D.getX(), offsetY + pos2D.getY(), cellSize, cellSize);
            g.setColor(new Color(255-frac, frac, 25, 20));
            g.fillOval(offsetX + pos2D.getX(), offsetY + pos2D.getY(), cellSize, cellSize);
        }
    }
}
