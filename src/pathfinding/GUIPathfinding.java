package pathfinding;

import core.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class GUIPathfinding extends JFrame {
    int gridWidth;
    int gridHeight;
    int cellSize = 20;

    // Elements in frame
    public GameViewPathFinding gameView;
    public JTextField from, to;
    public GameState gs;
    JPanel pathing;
    public boolean start = false;

    public GUIPathfinding(GameState gs) {
        this.gs = gs;
        this.gridWidth = gs.getWidth();
        this.gridHeight = gs.getHeight();

        from = new JTextField(10);
        to = new JTextField(10);
        JButton start = new JButton("Start");
        start.addActionListener(e -> GUIPathfinding.this.start = true);
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> {
            from.setText("");
            to.setText("");
            gameView.from = -1;
            gameView.to = -1;
        });
        pathing = new JPanel();
        pathing.add(from);
        pathing.add(to);
        pathing.add(start);
        pathing.add(clear);

        this.gameView = new GameViewPathFinding(gs, cellSize, from, to);
        pathing.setPreferredSize(new Dimension(gameView.getPreferredSize().width, 100));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(pathing);
        wrapper.add(gameView);
        getContentPane().add(wrapper);

        // Frame properties
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        repaint();
    }

    public void update(boolean[] visited, Collection<Integer> queue) {
        gameView.update(visited, queue);
        repaint();
    }

    public void saveImage(String filename) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        paint(graphics2D);
        ImageIO.write(image,"jpeg", new File(filename));
    }

    public Dimension getPreferredSize() {
        return new Dimension(gameView.getPreferredSize().width, pathing.getPreferredSize().height + gameView.getPreferredSize().height);
    }
}
