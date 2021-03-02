package graphToGridDraw;

import core.GameState;
import players.mcts.MCTS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    Color[] playerColors = new Color[] {
            new Color(175, 170, 43),
            new Color(79, 175, 114),
            new Color(102, 95, 175),
            new Color(175, 88, 175),
            new Color(49, 136, 175),
            new Color(175, 105, 57),
    };

    public GUI(GameState gs, MCTS mcts) {
        this.gameView = new GameView(gs.getWidth(), gs.getHeight(), cellSize, playerColors);
        this.gridWidth = gs.getWidth();
        this.gridHeight = gs.getHeight();

        info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        gameTick = new JLabel("Game tick: " + gs.getGameTick() + " / " + GameState.getMaxGameTicks());
        gameTick.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameEnded = new JLabel("");
        gameEnded.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerView = new PlayerInfoView(cellSize, playerColors);
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
        update(gs, true);

        // Frame properties
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JOptionPane.showInputDialog(new JPanel(),"Ready?", null);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    mcts.drawingIteration = !mcts.drawingIteration;
                }
            }
        });
        repaint();
    }

    public void update(GameState gs, boolean recordPos) {
        this.gs = gs;

        gameTick.setText("Game tick: " + gs.getGameTick() + " / " + GameState.getMaxGameTicks());
        if (gs.gameEnded) {
            gameEnded.setText("Game ended!");
        }

        playerView.update(gs);
        gameView.update(gs, recordPos);
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

    public void drawPosSequence(ArrayList<Integer> iterPos) {
        gameView.posSequence = iterPos;
    }
}
