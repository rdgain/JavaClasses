public class Run {

    public static void main(String[] args) {
        int playerX = 0;
        int playerY = 0;
        String playerName = "Bubbles";

        int gameTick = 0;
        boolean gameEnded = false;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        playerX++;
        playerY++;
        gameTick++;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        playerX++;
        playerY++;
        gameTick++;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        playerX++;
        playerY++;
        gameTick++;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        playerX++;
        playerY++;
        gameTick++;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

        playerX++;
        playerY++;
        gameTick++;
        gameEnded = true;
        System.out.println("T: " + gameTick + "; Ended? " + gameEnded + "; player: " + playerName + "; position:(" + playerX + "," + playerY + ")");

    }
}
