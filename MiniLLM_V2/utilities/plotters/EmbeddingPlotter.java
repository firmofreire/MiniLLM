package MiniLLM_V2.utilities.plotters;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Snapshot plotter for embeddings (static 2D positions).
 * Can optionally draw embedding vectors from the origin, now ending in arrows.
 */
public class EmbeddingPlotter {
    private double[][] reduced;
    private String[] labels;
    private final Color[] tokenColors;
    private PlotPanel plotPanel;
    private final Object lock = new Object();
    private final boolean drawVectors;

    public EmbeddingPlotter() {
        this(null, false);
    }

    public EmbeddingPlotter(Color[] colors) {
        this(colors, true); // default to arrows now
    }

    public EmbeddingPlotter(Color[] colors, boolean drawVectors) {
        this.tokenColors = (colors != null) ? colors : generateColors(200);
        this.drawVectors = drawVectors;

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Embedding Plotter");
            plotPanel = new PlotPanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(plotPanel);
            frame.setSize(600, 600);
//            frame.setLocation(906, 0);
            frame.setLocation(686, 0);
            frame.setVisible(true);
        });
    }

    public void waitUntilReady() {
        while (plotPanel == null) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
    }

    public void display(float[][] embeddings, String[] labels) {
        synchronized (lock) {
            this.reduced = PCA2D.reduce(embeddings);
            this.labels = labels;
        }
        plotPanel.repaint();
    }

    private class PlotPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.BLACK);
            if (reduced == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 40;

            double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
            double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
            for (double[] point : reduced) {
                minX = Math.min(minX, point[0]);
                maxX = Math.max(maxX, point[0]);
                minY = Math.min(minY, point[1]);
                maxY = Math.max(maxY, point[1]);
            }

            double xScale = (width - 2.0 * padding) / (maxX - minX + 1e-5);
            double yScale = (height - 2.0 * padding) / (maxY - minY + 1e-5);

            int originX = width / 2;
            int originY = height / 2;

            for (int i = 0; i < reduced.length; i++) {
                int x = (int) (padding + (reduced[i][0] - minX) * xScale);
                int y = (int) (height - padding - (reduced[i][1] - minY) * yScale);

                if (drawVectors) {
                    g2.setColor(tokenColors[i % tokenColors.length]);
                    drawArrow(g2, originX, originY, x, y);
                }

                if (labels != null) {
                    g2.setColor(Color.WHITE);
                    g2.drawString(labels[i], x + 8, y);
                }
            }
        }
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2);
        double phi = Math.toRadians(25);
        int barb = 10;
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        for (int j = 0; j < 2; j++) {
            double rho = theta + phi - j * 2 * phi;
            double x = x2 - barb * Math.cos(rho);
            double y = y2 - barb * Math.sin(rho);
            g2.drawLine(x2, y2, (int) x, (int) y);
        }
    }

    private static Color[] generateColors(int vocabSize) {
        Color[] colors = new Color[vocabSize];
        for (int i = 0; i < vocabSize; i++) {
            float hue = i / (float) vocabSize;
            colors[i] = Color.getHSBColor(hue, 0.7f, 0.9f);
        }
        return colors;
    }

    // --- Standalone Test ---
    public static void main(String[] args) {
        int vocab = 15, dim = 6;
        float[][] embeddings = randomEmbeddings(vocab, dim);
        String[] labels = new String[vocab];
        for (int i = 0; i < vocab; i++) labels[i] = "T" + i;

        EmbeddingPlotter plotter = new EmbeddingPlotter(null, true);		// (Color[] colors, boolean drawVectors)
        plotter.waitUntilReady();

        Timer timer = new Timer(1000, e -> {
            perturbEmbeddings(embeddings);
            plotter.display(embeddings, labels);
        });
        timer.start();
    }

    private static float[][] randomEmbeddings(int vocab, int dim) {
        Random rnd = new Random(123);
        float[][] e = new float[vocab][dim];
        for (int i = 0; i < vocab; i++)
            for (int j = 0; j < dim; j++)
                e[i][j] = rnd.nextFloat() - 0.5f;
        return e;
    }

    private static void perturbEmbeddings(float[][] e) {
        Random rnd = new Random();
        for (int i = 0; i < e.length; i++)
            for (int j = 0; j < e[i].length; j++)
                e[i][j] += (rnd.nextFloat() - 0.5f) * 0.05f;
    }
}
