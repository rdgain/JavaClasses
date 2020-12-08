package players.rhea;

import java.util.Arrays;

/**
 * Wrapper class for action sequences array, to be able to define custom hashCode and equals functions.
 */
public class Individual {
    public int[] actionSequence;

    public Individual(int length) {
        actionSequence = new int[length];
    }

    public int getFirst() {
        return actionSequence[0];
    }

    public void set(int idx, int value) {
        actionSequence[idx] = value;
    }

    int length() {
        return actionSequence.length;
    }

    Individual copy() {
        Individual i = new Individual(actionSequence.length);
        i.actionSequence = actionSequence.clone();
        return i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;
        Individual that = (Individual) o;
        return Arrays.equals(actionSequence, that.actionSequence);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(actionSequence);
    }
}
