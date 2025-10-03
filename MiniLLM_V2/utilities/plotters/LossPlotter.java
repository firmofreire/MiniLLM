package MiniLLM_V2.utilities.plotters;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CountDownLatch;

/***********************************************************************************************************************/
public class LossPlotter
{
    private final 	List<Double> 	xData = new ArrayList<>();
    private final 	List<Double> 	yData = new ArrayList<>();

    private 		PlotPanel 		plotPanel	= null;

    private final 	CountDownLatch 	readyLatch 	= new CountDownLatch(1);

/***********************************************************************************************************************/

    public LossPlotter()
    {
        SwingUtilities.invokeLater(() ->
        {
        	JFrame frame = new JFrame("Loss vs Epoch");
        	plotPanel = new PlotPanel();
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	frame.add(plotPanel);
        	frame.setSize(700, 600);
//			frame.setLocation(220, 0);
			frame.setLocation(0, 0);

        	frame.setVisible(true);

			readyLatch.countDown(); 			// Signal Readiness
		});
    }

/********************************************************************/

	public void waitUntilReady()
	{
        try
        {
            readyLatch.await(); 				//  Blocks until countDown is called
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

/***********************************************************************************************************************/

    public void display(double x, double y)
    {
        xData.add(x);
        yData.add(y);
        plotPanel.repaint();
    }

/***********************************************************************************************************************/

    private class PlotPanel extends JPanel
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            setBackground(Color.BLACK);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int padding = 80;
            int labelPadding = 10;

            int plotWidth = width - 2 * padding;
            int plotHeight = height - 2 * padding;
			//
            // Draw Title
            //
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("Loss vs Epoch", width / 2 - 60, 30);

            if (xData.isEmpty()) return;

            double xMin = xData.get(0);
            double xMax = xData.get(xData.size() - 1);
            double yMin = 0.0;
            double yMax = Math.max(1.0, yData.stream().max(Double::compareTo).orElse(1.0));

            double xScale = plotWidth / (xMax - xMin + 1e-5);
            double yScale = plotHeight / (yMax - yMin + 1e-5);

            // Axes
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(padding, height - padding, width - padding, height - padding); 	// X-Axis
            g2.drawLine(padding, padding, padding, height - padding); 					// Y-axis Left
            g2.drawLine(width - padding, padding, width - padding, height - padding); 	// Y-Axis Right
            //
            // Grid and Y Labels
            //
            g2.setFont(new Font("Open Sans", Font.PLAIN, 12));

            g2.setColor(Color.DARK_GRAY);
//            for (double y = yMin; y <= yMax; y += 0.1)
            for (double y = yMin; y <= yMax; y += 0.2)
            {
                int yPos = height - padding - (int) ((y - yMin) * yScale);
                g2.drawLine(padding, yPos, width - padding, yPos); 						// Horizontal Gridline

                g2.setColor(Color.WHITE);
                String yLabel = String.format("%.1f", y);
                g2.drawString(yLabel, padding - 40, yPos + 4); 							// Left
                g2.drawString(yLabel, width - padding + 10, yPos + 4); 					// Right
                g2.setColor(Color.DARK_GRAY);
            }
			//
            // Grid and X Labels
            int xTickCount = Math.min(20, xData.size());
            for (int i = 0; i < xTickCount; i++) {
                double x = xMin + i * (xMax - xMin) / (xTickCount - 1);
                int xPos = padding + (int) ((x - xMin) * xScale);
                g2.drawLine(xPos, padding, xPos, height - padding); 					// Vertical Gridline

                g2.setColor(Color.WHITE);
                String xLabel = String.format("%.0f", x);
                g2.drawString(xLabel, xPos - 10, height - padding + 20);
                g2.setColor(Color.DARK_GRAY);
            }
			//
            // Plot Line
            //
            g2.setColor(Color.CYAN);
            for (int i = 1; i < xData.size(); i++)
            {
                int x1 = padding + (int) ((xData.get(i - 1) - xMin) * xScale);
                int y1 = height - padding - (int) ((yData.get(i - 1) - yMin) * yScale);
                int x2 = padding + (int) ((xData.get(i) - xMin) * xScale);
                int y2 = height - padding - (int) ((yData.get(i) - yMin) * yScale);
                g2.drawLine(x1, y1, x2, y2);
            }
			//
            // Axis Labels
            //
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.drawString("Epoch", width / 2 - 20, height - 10);

            g2.rotate(-Math.PI / 2);
            g2.drawString("Loss", -height / 2 - 30, 20);
            g2.rotate(Math.PI / 2);
            //
			// Red Border Around Plotting Area
			//
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(2)); 											// Thicker Line For Visibility
			g2.drawRect(padding + 1, padding + 1, plotWidth - 2, plotHeight - 2); 		// Slightly Inset
        }
    }

/***********************************************************************************************************************/

    // Test main
    public static void main(String[] args)
    {
        LossPlotter plotter = new LossPlotter();
        new Timer(200, e ->
        {
            double epoch = plotter.xData.size();
            double loss = 1.0 / (1 + 0.1 * epoch) + 0.05 * Math.random();
            plotter.display(epoch, loss);
        }).start();
    }

/***********************************************************************************************************************/

}