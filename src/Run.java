public class Run {

    public static void main(String[] args) {
        int playerX = 0;
        int playerY = 0;
        String playerName = "Bubbles";

        int gameTick = 0;
        boolean gameEnded = false;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        while(!gameEnded) {
            playerX++;
            playerY++;
            gameTick++;
            if (playerX == 5 && playerY == 5) {
                gameEnded = true;
            }
            System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");
        }
    }
}
