package core;

import java.util.Objects;

public class Pickup {
    int position;
    int nPoints;
    boolean active;

    public Pickup(int position, int points) {
        this.position = position;
        this.nPoints = points;
        this.active = true;
    }

    /**
     * Picked up! Give player points, and indicate this pickup is inactive.
     * @param who - the player who picked this up.
     */
    void pick(Player who) {
        who.score += nPoints;
        this.active = false;
    }

    /**
     * Copy the button object.
     * @return - a new Button object with the same state.
     */
    public Pickup copy() {
        Pickup b = new Pickup(this.position, this.nPoints);
        b.active = active;
        return b;
    }

    public int getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Pickup{" +
                "position=" + position +
                ", nPoints=" + nPoints +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pickup)) return false;
        Pickup pickup = (Pickup) o;
        return position == pickup.position &&
                nPoints == pickup.nPoints;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, nPoints);
    }
}
