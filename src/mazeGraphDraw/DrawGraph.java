package mazeGraphDraw;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows drawing a custom maze grid and outputs the corresponding graph in the format:
 * nodeID: nodeID_up,nodeID_right,nodeID_down,nodeID_left
 *
 * If a grid cell is not connected to another (due to a wall block), the connection will not show up in the graph e.g.:
 * nodeID: nodeID_up,,nodeID_down,nodeID_left   // No connection to the right from this cell
 *
 * Use left click and drag to draw new edges between cells.
 * Use right click to undo the last line segment drawn.
 * Close the window (without stopping the program) to print out the graph in the format mentioned above and save image.
 */
public class DrawGraph extends JFrame {
    int w, h;  // Width and height of grid, number of cells = w*h
    int cellSize = 40;  // Size of cell when drawn on the screen
    int offsetX = 0, offsetY = 0;  // Moves the whole drawing to the right (bigger x) or down (bigger y)

    Vector2D start;  // Keeps track of starting point of click-drag action
    Vector2D end;  // Keeps track of ending point of click-drag action
    boolean undo;  // Keeps track of right-click action for undo of last line segment
    List<Pair<Vector2D, Vector2D>> edges = new ArrayList<>();  // All edges drawn

    // Dashed stroke definition for grid outline
    final static float[] dash1 = {2f};
    final static BasicStroke dashed =
            new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);

    boolean init;  // True if the grid has been initialised, to avoid painting the static outline every frame
    WindowInput wi;  // Keeps track of window open/closed status

    /**
     * Constructor, takes grid size arguments and sets up mouse and window listeners to allow user interaction.
     * @param w - Grid width
     * @param h - Grid height
     */
    public DrawGraph(int w, int h) {
        this.w = w;
        this.h = h;

        // Mouse listener, for click and drag action recognition
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if (e.getButton() == 1) {  // Left-click, record point as start of line to draw
                    start = new Vector2D(e.getX(), e.getY());
                    start.subtract(offsetX, offsetY);  // Remove offset to help calculations
                } else if (e.getButton() == 3) {  // Right-click
                    undo = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == 1) {  // Only on left-click
                    end = new Vector2D(e.getX(), e.getY());
                    end.subtract(offsetX, offsetY);  // Remove offset to help calculations

                    // Drawing only straight lines, so find if Xs or Ys are closer to get direction
                    int xDiff = Math.abs(start.getX() - end.getX());
                    int yDiff = Math.abs(start.getY() - end.getY());

                    // Also find the point which is closer to a cell corner
                    Vector2D endGrid = end.copy();
                    endGrid.divide(cellSize);
                    Vector2D startGrid = start.copy();
                    startGrid.divide(cellSize);
                    endGrid.subtract(end);
                    startGrid.subtract(start);

                    // Keep values in grid sizes, rather than screen pixels
                    end.divide(cellSize);
                    start.divide(cellSize);

                    if (xDiff < yDiff) {
                        // Vertical line, same x
                        if (endGrid.magnitude() > startGrid.magnitude()) {
                            // End point was closer
                            start.setX(end.getX());
                        } else {
                            end.setX(start.getX());
                        }
                    } else {
                        // Horizontal line, same y
                        if (endGrid.magnitude() > startGrid.magnitude()) {
                            // End point was closer
                            start.setY(end.getY());
                        } else {
                            end.setY(start.getY());
                        }
                    }
                }
            }
        });

        // Set up window listener to check if window was closed
        this.wi = new WindowInput();
        addWindowListener(wi);

        // Frame properties
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        repaint();
    }

    /**
     * Draws static grid outline depending on grid size. Executed at the beginning, and every time an undo action is
     * done to repaint everything.
     * @param g - Graphics2D object to draw on the screen with.
     */
    private void init(Graphics2D g) {
        // Paint a white rectangle (filled, not just outline) on the whole display area.
        g.setColor(Color.white);
        g.fillRect(0,0, (w+2)*cellSize, (h+3)*cellSize);

        // Set the brush stroke for further drawings to be dashed and gray.
        g.setColor(Color.GRAY);
        g.setStroke(dashed);

        // Draw horizontal lines of grid
        for (int i = 0; i <= h; i++) {
            g.drawLine(offsetX, offsetY+i*cellSize, offsetX + w*cellSize, offsetY+i*cellSize);
        }
        // Draw Vertical lines of grid
        for (int i = 0; i <= w; i++) {
            g.drawLine(offsetX + i*cellSize, offsetY, offsetX + i*cellSize, offsetY + h*cellSize);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // Initial painting of grid outline, done only once
        if (!init) {
            init = true;
            init(g);
        }

        // If a click-drag action was completed, both start and end points were set
        if (start != null && end != null) {
            if (!start.equals(end)) {  // Only draw if the points actually make a line
                Vector2D diff = start.copy();
                diff.subtract(end);
                if (diff.magnitude() > 1) {  // Check if the line spans multiple cells, we need to split it up
                    if (start.getX() == end.getX()) {
                        // Same X, so iterate from minimum Y to maximum Y and add all edges individually
                        int minY = Math.min(start.getY(), end.getY());
                        int maxY = Math.max(start.getY(), end.getY());
                        for (int y = minY; y < maxY; y++) {
                            edges.add(new Pair<>(new Vector2D(start.getX(), y), new Vector2D(start.getX(), y+1)));
                        }
                    } else {
                        // Same Y, so iterate from minimum X to maximum X and add all edges individually
                        int minX = Math.min(start.getX(), end.getX());
                        int maxX = Math.max(start.getX(), end.getX());
                        for (int x = minX; x < maxX; x++) {
                            edges.add(new Pair<>(new Vector2D(x, start.getY()), new Vector2D(x+1, start.getY())));
                        }
                    }
                } else {
                    // Otherwise, 1-cell length edge, just add it to the list
                    edges.add(new Pair<>(start, end));
                }
            }
            // Reset these to indicate we've added the edge just drawn and a new one can be drawn next
            start = null;
            end = null;
        }

        if (undo) {
            // Check if we need to undo, in which case remove the last added line segment and reset variable
            if (!edges.isEmpty()) edges.remove(edges.size()-1);
            init(g);  // Repaint background to wipe out old drawing
            undo = false;
        }

        // Draw all edges
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(3));
        for (Pair<Vector2D, Vector2D> p: edges) {
            g.drawLine(offsetX+p.a.getX()*cellSize, offsetY+p.a.getY()*cellSize,
                    offsetX+p.b.getX()*cellSize, offsetY+p.b.getY()*cellSize);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((w+2)*cellSize, (h+3)*cellSize);
    }

    public void saveImage(String filename) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        init(graphics2D);
        paint(graphics2D);
        ImageIO.write(image,"jpeg", new File(filename));
    }

    /**
     * Prints the graph corresponding to the drawing in the right format.
     */
    public void printGridGraph() {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int id = i*w+j;  // Calculate ID of this node, counting from top-left, one line at a time

                // Edges surrounding this cell
                Pair<Vector2D, Vector2D> up = new Pair<>(new Vector2D(i,j), new Vector2D(i,j+1));
                Pair<Vector2D, Vector2D> right = new Pair<>(new Vector2D(i,j+1), new Vector2D(i+1,j+1));
                Pair<Vector2D, Vector2D> down = new Pair<>(new Vector2D(i+1,j), new Vector2D(i+1,j+1));
                Pair<Vector2D, Vector2D> left = new Pair<>(new Vector2D(i,j), new Vector2D(i+1,j));

                // Create the string representing this cell connections
                String s = id + ": ";
                if (!edges.contains(up) && inRange(j, i-1)) s += ((i-1)*w+(j));
                s += ",";
                if (!edges.contains(right) && inRange(j+1, i)) s += ((i)*w+(j+1));
                s += ",";
                if (!edges.contains(down) && inRange(j, i+1)) s += ((i+1)*w+(j));
                s += ",";
                if (!edges.contains(left) && inRange(j-1, i)) s += ((i)*w+(j-1));
                // Print it!
                System.out.println(s);
            }
        }
    }

    /**
     * Checks if coordinates are within the grid
     * @param x - x coordinate
     * @param y - y coordinate
     * @return - true if in the grid, false otherwise.
     */
    private boolean inRange(int x, int y) {
        return x >= 0 && x < w && y >= 0 && y < h;
    }

    /**
     * Main method to run the application.
     * @param args - none
     */
    public static void main(String[] args) {
        // Create a graph drawing application, with grid of size 16x18
        DrawGraph dg = new DrawGraph(16, 18);

        // Update the window as long as it remains open, with a delay of 100ms between frames
        while (!dg.wi.windowClosed) {
            dg.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print the graph when the window is closed and save the image to the file specified
        dg.printGridGraph();
        try {
            dg.saveImage("grid-" + System.currentTimeMillis() + ".jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
