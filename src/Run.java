public class Run {
    static int playerX = 0;
    static int playerY = 0;
    static String playerName = "Bubbles";
    static int gameTick = 0;

    static boolean iterate() {
        boolean gameEnded = false;
        playerX++;
        playerY++;
        gameTick++;
        if (playerX == 5 && playerY == 5) {
            gameEnded = true;
        }
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");
        return gameEnded;
    }

    public static void main(String[] args) {
        boolean gameEnded = false;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        while(!gameEnded) {
            gameEnded = iterate();
        }
    }
}
