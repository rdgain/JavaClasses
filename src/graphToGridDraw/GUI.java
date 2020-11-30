package graphToGridDraw;

import core.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    GameState gs;
    int gridWidth;
    int gridHeight;
    int cellSize = 20, infoHeight = 20;

    // Elements in frame
    JPanel info;
    GameView gameView;
    JLabel gameTick;
    JLabel gameEnded;
    PlayerInfoView playerView;

    public GUI(GameState gs) {
        this.gameView = new GameView(gs.getWidth(), gs.getHeight(), cellSize);
        this.gridWidth = gs.getWidth();
        this.gridHeight = gs.getHeight();

        info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        gameTick = new JLabel("Game tick: " + gs.getGameTick() + " / " + GameState.getMaxGameTicks());
        gameTick.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameEnded = new JLabel("");
        gameEnded.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerView = new PlayerInfoView(cellSize);
        info.add(gameTick);
        info.add(gameEnded);
        info.add(playerView);
        Dimension d = playerView.getPreferredSize();
        info.setPreferredSize(new Dimension(d.width, d.height + infoHeight));

        // Add game view and info panel to frame
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.add(info);
        wrapper.add(gameView);
        getContentPane().add(wrapper);

        // Update game state info
        update(gs);

        // Frame properties
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        repaint();
    }

    public void update(GameState gs) {
        this.gs = gs;

        gameTick.setText("Game tick: " + gs.getGameTick() + " / " + GameState.getMaxGameTicks());
        if (gs.gameEnded) {
            gameEnded.setText("Game ended!");
        }

        playerView.update(gs);
        gameView.update(gs);
        repaint();
    }

    public void saveImage(String filename) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        paint(graphics2D);
        ImageIO.write(image,"jpeg", new File(filename));
    }

    public Dimension getPreferredSize() {
        return new Dimension(gameView.getPreferredSize().width, gameView.getPreferredSize().height + info.getPreferredSize().height);
    }
}
