package graphToGridDraw;

import core.Button;
import core.GameState;
import core.Pickup;
import core.Player;
import mazeGraphDraw.Vector2D;
import utils.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static utils.Utils.posToScreenCoords;

public class GameView extends JComponent {
    int gridWidth, gridHeight;
    private GameState gameState;
    int cellSize, offsetX = 10, offsetY = 10;

    HashMap<Integer, ArrayList<Integer>> playerPositionHistory;
    int heatMapOpacity = 20;
    int playerFocusHeatMap = -1;  // If set to a player ID, only that player's positional heatmap will be drawn
    Color[] playerColors;


    ArrayList<Integer> posSequence;

    public GameView(int w, int h, int c, Color[] playerColors) {
        this.gridWidth = w;
        this.gridHeight = h;
        this.cellSize = c;
        this.playerPositionHistory = new HashMap<>();
        this.playerColors = new Color[6];
        for (int i = 0; i < 6; i++) {
            // 6 players
            this.playerPositionHistory.put(i, new ArrayList<>());
            this.playerColors[i] = new Color(playerColors[i].getRed(), playerColors[i].getGreen(), playerColors[i].getBlue(), heatMapOpacity);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.black);

        if (gameState != null) {
            drawMaze(g);

            drawPlayerPositionHeatMap(g);

            for (Player p: gameState.getPlayers()) {
                if (p.drawing) {
                    p.draw(g, gameState, cellSize, offsetX, offsetY);
                }
            }

            if (posSequence != null) {
                for (int p: posSequence) {
                    g.setColor(new Color(0,0,0, 205));
                    Vector2D pos2D = posToScreenCoords(p, gridWidth, cellSize);
                    g.fillRect(offsetX + pos2D.getX() + cellSize/4, offsetY + pos2D.getY() + cellSize/4, cellSize/2, cellSize/2);
                }
                posSequence = null;
            }

            drawButton(g);
            drawPickups(g);
            drawPlayers(g);
        }
    }



    private void drawPlayerPositionHeatMap(Graphics2D g) {
        for (int p: playerPositionHistory.keySet()) {
            if (playerFocusHeatMap == -1 || playerFocusHeatMap == p) {
                g.setColor(playerColors[p]);
                for (int pos : playerPositionHistory.get(p)) {
                    Vector2D pos2D = posToScreenCoords(pos, gridWidth, cellSize);
                    g.fillRect(offsetX + pos2D.getX(), offsetY + pos2D.getY(), cellSize, cellSize);
                }
            }
        }
    }

    private void drawPickups(Graphics2D g) {
        ArrayList<Pickup> pickups = gameState.getPickups();
        for (Pickup p: pickups) {
            if (p.isActive()) {
                Vector2D pos = posToScreenCoords(p.getPosition(), gridWidth, cellSize);
                int nodeXScreen = pos.getX();
                int nodeYScreen = pos.getY();
                g.setColor(new Color(150, 68, 88));
                g.fillOval(offsetX + nodeXScreen + cellSize / 3, offsetY + nodeYScreen + cellSize / 3, cellSize/4, cellSize/4);
            }
        }
    }

    private void drawPlayers(Graphics2D g) {
        Player[] players = gameState.getPlayers();
        for (Player p: players) {
            if (p.getGameStatus() != -1) {
                // Only draw players still in the game
                Vector2D pos = posToScreenCoords(p.getPosition(), gridWidth, cellSize);
                int nodeXScreen = pos.getX();
                int nodeYScreen = pos.getY();

                Color team, edge;
                if (p.getTeamID() == 0) {
                    team = new Color(44, 150, 43);
                    edge = new Color(50, 108, 30);
                } else {
                    team = new Color(150, 140, 52);
                    edge = new Color(108, 103, 38);
                }

                g.setColor(Color.black);
                g.drawString("" + p.getPlayerID(), offsetX + nodeXScreen + cellSize / 4, offsetY + nodeYScreen);
                g.setColor(team);
                g.fillRect(offsetX + nodeXScreen + cellSize / 4, offsetY + nodeYScreen + cellSize / 4, cellSize / 2, cellSize / 2);
                g.setColor(edge);
                g.drawRect(offsetX + nodeXScreen + cellSize / 4, offsetY + nodeYScreen + cellSize / 4, cellSize / 2, cellSize / 2);
            }
        }
    }

    private void drawButton(Graphics2D g) {
        Button b = gameState.getButton();
        Vector2D pos = posToScreenCoords(b.getPosition(), gridWidth, cellSize);
        int nodeXScreen = pos.getX();
        int nodeYScreen = pos.getY();

        Color fill, edge;
        if (b.isCurrentlyActive()) {
            fill = new Color(16, 125, 150);
            edge = new Color(15, 65, 108);
        } else {
            fill = new Color(149, 150, 150);
            edge = new Color(107, 106, 108);
        }

        g.setColor(fill);
        g.fillOval(offsetX + nodeXScreen, offsetY + nodeYScreen, cellSize, cellSize);
        g.setColor(edge);
        g.drawOval(offsetX + nodeXScreen, offsetY + nodeYScreen, cellSize, cellSize);
    }

    private void drawMaze(Graphics2D g) {
        HashMap<Integer, GraphNode> maze = gameState.getMaze();
        for (GraphNode n : maze.values()) {
            Vector2D pos = posToScreenCoords(n.id, gridWidth, cellSize);
            int nodeXScreen = pos.getX();
            int nodeYScreen = pos.getY();

            // Draw node ID in the center of the cell (nodeX + cellSize/2, nodeY + cellSize/2)
//            g.drawString("" + n.id, offsetX + nodeXScreen + cellSize / 2, offsetY + nodeYScreen + cellSize / 2);

            // Top edge
            if (!n.connections.containsKey(1)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen);
            }

            // Right edge
            if (!n.connections.containsKey(2)) {
                g.drawLine(offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen + cellSize);
            }

            // Bottom edge
            if (!n.connections.containsKey(3)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen + cellSize, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen + cellSize);
            }

            // Left edge
            if (!n.connections.containsKey(4)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen, offsetX + nodeXScreen, offsetY + nodeYScreen + cellSize);
            }
        }
    }

    public void update(GameState gameState, boolean recordPos) {
        this.gameState = gameState;

        // Update player positions
        if (recordPos) {
            for (Player p : gameState.getPlayers()) {
                playerPositionHistory.get(p.getPlayerID()).add(p.getPosition());
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((gridWidth+2)*cellSize, (gridHeight+3)*cellSize);
    }
}
