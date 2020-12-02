package pathfinding;

import core.Button;
import core.GameState;
import core.Pickup;
import core.Player;
import mazeGraphDraw.Vector2D;
import utils.GraphNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class GameViewPathFinding extends JComponent {
    // Path finding vars
    boolean[] visited;
    Collection<Integer> queue;
    ArrayList<Integer> path;

    public int from;
    public int to;

    // Drawing vars
    int gridWidth, gridHeight;
    private GameState gameState;
    int cellSize, offsetX = 10, offsetY = 10;

    public GameViewPathFinding(GameState gameState, int c, JTextField fromF, JTextField toF) {
        this.gameState = gameState;
        this.gridWidth = gameState.getWidth();
        this.gridHeight = gameState.getHeight();
        this.cellSize = c;
        from = -1;
        to = -1;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int id = (e.getPoint().y - offsetY)/cellSize * gridWidth + (e.getPoint().x - offsetX)/cellSize;
                if (from == -1) {
                    from = id;
                    fromF.setText("" + id);
                } else {
                    to = id;
                    toF.setText("" + id);
                }
            }
        });
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.black);

        if (visited != null) {
            drawPathFinding(g);
        }

        if (gameState != null) {
            drawMaze(g);
//            drawPickups(g);
//            drawButton(g);
//            drawPlayers(g);
        }
    }

    private void drawPathFinding(Graphics2D g) {
        // Draw nodes visited in path finding algorithm
        g.setColor(new Color(93, 188, 133));
        for (int i = 0; i < visited.length; i++) {
            if (visited[i]) {
                Vector2D pos = posToScreenCoords(i);
                int nodeXScreen = pos.getX();
                int nodeYScreen = pos.getY();
                g.fillRect(offsetX + nodeXScreen, offsetY + nodeYScreen, cellSize, cellSize);
            }
        }

        // Draw nodes in the queue
        g.setColor(new Color(152, 113, 188));
        for (int i: queue) {
            Vector2D pos = posToScreenCoords(i);
            int nodeXScreen = pos.getX();
            int nodeYScreen = pos.getY();
            g.fillRect(offsetX + nodeXScreen, offsetY + nodeYScreen, cellSize, cellSize);
        }

        // Draw nodes in the path
        if (path != null) {
            g.setColor(new Color(188, 129, 69));
            for (int i : path) {
                Vector2D pos = posToScreenCoords(i);
                int nodeXScreen = pos.getX();
                int nodeYScreen = pos.getY();
                g.fillRect(offsetX + nodeXScreen, offsetY + nodeYScreen, cellSize, cellSize);
            }
        }

        g.setColor(Color.black);

        // Highlight from
        Vector2D pos = posToScreenCoords(from);
        int nodeXScreen = pos.getX();
        int nodeYScreen = pos.getY();
        g.drawString("F", offsetX + nodeXScreen + cellSize/2, offsetY + nodeYScreen + cellSize/2);

        // Highlight to
        pos = posToScreenCoords(to);
        nodeXScreen = pos.getX();
        nodeYScreen = pos.getY();
        g.drawString("T", offsetX + nodeXScreen + cellSize/2, offsetY + nodeYScreen + cellSize/2);
    }

    private void drawPickups(Graphics2D g) {
        ArrayList<Pickup> pickups = gameState.getPickups();
        g.setColor(new Color(150, 68, 88));
        for (Pickup p: pickups) {
            if (p.isActive()) {
                Vector2D pos = posToScreenCoords(p.getPosition());
                int nodeXScreen = pos.getX();
                int nodeYScreen = pos.getY();
                g.fillOval(offsetX + nodeXScreen + cellSize / 3, offsetY + nodeYScreen + cellSize / 3, cellSize/4, cellSize/4);
            }
        }
    }

    private void drawPlayers(Graphics2D g) {
        Player[] players = gameState.getPlayers();
        for (Player p: players) {
            if (p.getGameStatus() != -1) {
                // Only draw players still in the game
                Vector2D pos = posToScreenCoords(p.getPosition());
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
                g.drawString(offsetX + "" + p.getPlayerID(), offsetY + nodeXScreen + cellSize / 4, nodeYScreen + cellSize / 2);
                g.setColor(team);
                g.fillRect(offsetX + nodeXScreen + cellSize / 4, offsetY + nodeYScreen + cellSize / 4, cellSize / 2, cellSize / 2);
                g.setColor(edge);
                g.drawRect(offsetX + nodeXScreen + cellSize / 4, offsetY + nodeYScreen + cellSize / 4, cellSize / 2, cellSize / 2);
            }
        }
    }

    private void drawButton(Graphics2D g) {
        Button b = gameState.getButton();
        Vector2D pos = posToScreenCoords(b.getPosition());
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
        g.setColor(Color.black);
        HashMap<Integer, GraphNode> maze = gameState.getMaze();
        for (GraphNode n : maze.values()) {
            Vector2D pos = posToScreenCoords(n.id);
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

    private Vector2D posToScreenCoords(int pos) {
        int nodeX = pos % gridWidth;
        int nodeY = pos / gridWidth;
        int nodeXScreen = nodeX * cellSize;
        int nodeYScreen = nodeY * cellSize;
        return new Vector2D(nodeXScreen, nodeYScreen);
    }

    public void update(boolean[] visited, Collection<Integer> queue, ArrayList<Integer> path) {
        this.visited = visited;
        this.queue = queue;
        this.path = path;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((gridWidth+2)*cellSize, (gridHeight+3)*cellSize);
    }
}
