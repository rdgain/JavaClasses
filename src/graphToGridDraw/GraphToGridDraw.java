package graphToGridDraw;

import utils.GraphNode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GraphToGridDraw extends JFrame {
    private int gridWidth;
    private int gridHeight;
    HashMap<Integer, GraphNode> maze;
    int cellSize = 50, offsetX = 10, offsetY = 20;

    public GraphToGridDraw(HashMap<Integer, GraphNode> graph, int gridWidth, int gridHeight) {
        this.maze = graph;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        // Frame properties
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.black);

        for (GraphNode n: maze.values()) {
            int nodeX = n.id % gridWidth;
            int nodeY = n.id / gridWidth;
            int nodeXScreen = nodeX * cellSize;
            int nodeYScreen = nodeY * cellSize;

            // Draw node ID in the center of the cell (nodeX + cellSize/2, nodeY + cellSize/2)
            g.drawString(""+n.id, nodeXScreen + cellSize/2, nodeYScreen + cellSize/2);

            // Top edge TODO: only draw edge this if path is blocked from this node in the up direction
            if (!n.connections.containsKey(1)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen);
            }

            // Right edge TODO: only draw edge this if path is blocked from this node in the right direction
            if (!n.connections.containsKey(2)) {
                g.drawLine(offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen + cellSize);
            }

            // Bottom edge TODO: only draw this edge if path is blocked from this node in the down direction
            if (!n.connections.containsKey(3)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen + cellSize, offsetX + nodeXScreen + cellSize, offsetY + nodeYScreen + cellSize);
            }

            // Left edge TODO: only draw this edge if path is blocked from this node in the left direction
            if (!n.connections.containsKey(4)) {
                g.drawLine(offsetX + nodeXScreen, offsetY + nodeYScreen, offsetX + nodeXScreen, offsetY + nodeYScreen + cellSize);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension((gridWidth+2)*cellSize, (gridHeight+3)*cellSize);
    }

    public void saveImage(String filename) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        paint(graphics2D);
        ImageIO.write(image,"jpeg", new File(filename));
    }

    public static void main(String[] args) {
        GraphNode.Container container = GraphNode.readMazeFromFile();
        new GraphToGridDraw(container.graph, container.width, container.height);
    }
}
