import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

public class FractalExplorer extends JFrame {
	private int displaySize;
	private JImageDisplay image;
	private FractalGenerator fractals;
	private Rectangle2D.Double range;

	public FractalExplorer(int size) {
		this.displaySize = size;

		range = new Rectangle2D.Double();

		fractals = new Mandelbrot();
		fractals.getInitialRange(range); // x, y, width, height

		image = new JImageDisplay(displaySize, displaySize);
	}

	public void drawFractal() {
		for (int x = 0; x < displaySize; x++) {
			for (int y = 0; y < displaySize; y++) {
				double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x);
				double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y);
				int numIters = fractals.numIterations(xCoord, yCoord);

				if (numIters == -1) {
					image.drawPixel(x, y, 0);
				} else {
					float hue = 0.7f + (float) numIters / 200f;
					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
					image.drawPixel(x, y, rgbColor);
				}
				image.repaint();
			}
		}
	}

	public void createAndShowGUI() {
		JFrame frame = new JFrame("Mandelbrot");
		JButton button = new JButton("Reset");
		setPreferredSize(new Dimension(displaySize, displaySize));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(image, BorderLayout.CENTER);
		frame.add(button, BorderLayout.SOUTH);

		ResetListener reset = new ResetListener();
		button.addActionListener(reset);
		
		ZoomListener zoom = new ZoomListener();
		image.addMouseListener(zoom);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public class ResetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("Reset")) {
				image.clearImage();
				image.repaint();
			}
		}
	}

	public class ZoomListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());
			fractals.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
			drawFractal();
		}
	}

	public static void main(String[] args) {
		FractalExplorer app = new FractalExplorer(1200);
		app.createAndShowGUI();
		app.drawFractal();
	}
}
