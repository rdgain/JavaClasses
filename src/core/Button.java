package core;

public class Button {
    int position;
    int cooldown;
    int cooldownCounter;
    boolean currentlyActive;

    public Button(int position) {
        this.position = position;
        currentlyActive = false;
        cooldown = 10;
        cooldownCounter = 10;
    }

    /**
     * Button update at every game tick, cooldown counter reduced until it reaches 0 and button becomes active.
     */
    void tick() {
        if (!currentlyActive) {
            cooldownCounter --;
            if (cooldownCounter == 0) {
                currentlyActive = true;
                cooldownCounter = cooldown;
            }
        }
    }

    /**
     * Button was pressed! Execute button pressing logic
     * @param who - the player who pressed the button.
     */
    void press(Player who) {
        who.swapTeam();
        currentlyActive = false;
        cooldown += 2;
        cooldownCounter = cooldown;
    }

    /**
     * Copy the button object.
     * @return - a new Button object with the same state.
     */
    public Button copy() {
        Button b = new Button(this.position);
        b.cooldownCounter = cooldownCounter;
        b.cooldown = cooldown;
        b.currentlyActive = currentlyActive;
        return b;
    }

    @Override
    public String toString() {
        return "Button{" +
                "position=" + position +
                ", cooldown=" + cooldown +
                ", cooldownCounter=" + cooldownCounter +
                ", currentlyActive=" + currentlyActive +
                '}';
    }
}
