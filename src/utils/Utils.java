package utils;

import mazeGraphDraw.Vector2D;

public class Utils {
    /**
     * Adds a small noise to the input value.
     * @param input value to be altered
     * @param epsilon relative amount the input will be altered
     * @param random random variable in range [0,1]
     * @return epsilon-random-altered input value
     */
    public static double noise(double input, double epsilon, double random) {
        return (input + epsilon) * (1.0 + epsilon * (random - 0.5));
    }

    //Normalizes a value between its MIN and MAX.
    public static double normalise(double a_value, double a_min, double a_max)
    {
        if(a_min < a_max)
            return (a_value - a_min)/(a_max - a_min);
        else    // if bounds are invalid, then return same value
            return a_value;
    }

    public static Vector2D posToScreenCoords(int pos, int gridWidth, int cellSize) {
        int nodeX = pos % gridWidth;
        int nodeY = pos / gridWidth;
        int nodeXScreen = nodeX * cellSize;
        int nodeYScreen = nodeY * cellSize;
        return new Vector2D(nodeXScreen, nodeYScreen);
    }
}
