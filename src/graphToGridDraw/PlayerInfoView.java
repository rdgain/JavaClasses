package graphToGridDraw;

import core.GameState;
import core.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoView extends JComponent {
    private GameState gameState;
    int cellSize, offsetX = 10, offsetY = 5, spacing = 100;
    Color[] playerColors;

    public PlayerInfoView(int c, Color[] playerColors) {
        this.cellSize = c;
        this.playerColors = playerColors;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.black);

        if (gameState != null) {
            drawPlayers(g);
        }
    }

    private void drawPlayers(Graphics2D g) {
        Player[] players = gameState.getPlayers();
        int i = 0;
        for (Player p: players) {
            int nodeXScreen, nodeYScreen;
            if (i < 3) {
                nodeXScreen = i*cellSize + i * spacing;
                nodeYScreen = 0;
            } else {
                nodeXScreen = (i-3)*cellSize + (i-3) * spacing;
                nodeYScreen = offsetY*2 + 45 + cellSize;
            }

            Color team, edge;
            if (p.getTeamID() == 0) {
                team = new Color(44, 150, 43);
                edge = new Color(50, 108, 30);
            } else {
                team = new Color(150, 140, 52);
                edge = new Color(108, 103, 38);
            }
            if (p.getGameStatus() == -1) {
                team = new Color(149, 150, 150);
                edge = new Color(107, 106, 108);
            }

            g.setColor(team);
            g.fillRect(nodeXScreen + cellSize * 3 / 4, offsetY + nodeYScreen + cellSize * 3 / 4, cellSize / 2, cellSize / 2);
            g.setColor(edge);
            g.drawRect(nodeXScreen + cellSize * 3 / 4, offsetY + nodeYScreen + cellSize * 3 / 4, cellSize / 2, cellSize / 2);

            g.setColor(playerColors[p.getPlayerID()]);
            g.drawString("" + p.getPlayerID(), nodeXScreen + cellSize * 3 / 4, nodeYScreen + cellSize / 2);
            String score = "Score: " + p.getScore();
            g.drawString(score, nodeXScreen, offsetY + nodeYScreen + cellSize*2);

            String name = p.getClass().getName();
            String[] split = name.split("\\.");
            String pName = split[split.length-1].replace("Player", "");
            g.drawString(pName, nodeXScreen, offsetY + nodeYScreen + cellSize*2 + 15);

            i++;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(3*cellSize+2*spacing, cellSize*2+ 90 + offsetY*4);
    }

    public void update(GameState gameState) {
        this.gameState = gameState;
    }
}
