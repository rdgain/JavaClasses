package core;

public class Button {

    int position;
    int cooldown, cooldownCounter;
    boolean currentlyActive;

    public Button(int position) {
        this.position = position;
        currentlyActive = false;
        cooldown = 10;
        cooldownCounter = 10;
    }

    void tick() {
        if (!currentlyActive && cooldownCounter > 0) {
            cooldownCounter--;
            if (cooldownCounter == 0) {
                currentlyActive = true;
                cooldownCounter = cooldown;
            }
        }
    }

    void press(Player other) {
        other.swapTeam();
        currentlyActive = false;
        cooldown += 2;
        cooldownCounter = cooldown;
    }
}
