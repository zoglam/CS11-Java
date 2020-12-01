import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
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
		JFrame frame = new JFrame("Fractals");

		JPanel selectPanel = new JPanel();
		JLabel textField = new JLabel("Fractal:");
		JComboBox<FractalGenerator> selectButton = new JComboBox();
		selectButton.addItem(new Mandelbrot());
		selectButton.addItem(new Tricorn());
		selectButton.addItem(new BurningShip());
		selectPanel.add(textField);
		selectPanel.add(selectButton);

		JPanel actionPanel = new JPanel();
		JButton saveButton = new JButton("Save Image");
		JButton resetButton = new JButton("Reset");
		actionPanel.add(saveButton);
		actionPanel.add(resetButton);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(selectPanel, BorderLayout.NORTH);
		frame.add(image, BorderLayout.CENTER);
		frame.add(actionPanel, BorderLayout.SOUTH);

		ButtonListener button = new ButtonListener();
		ZoomListener zoom = new ZoomListener();
		selectButton.addActionListener(button);
		resetButton.addActionListener(button);
		saveButton.addActionListener(button);
		image.addMouseListener(zoom);

		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();

			switch (cmd) {
			case "Reset": {
				fractals.getInitialRange(range);
				drawFractal();
				break;
			}
			case "comboBoxChanged": {
				JComboBox<FractalGenerator> box = (JComboBox) e.getSource();
				fractals = (FractalGenerator) box.getSelectedItem();
				fractals.getInitialRange(range);
				drawFractal();
				break;
			}
			case "Save Image": {
				JFileChooser j = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				j.setFileFilter(filter);
				j.setAcceptAllFileFilterUsed(false);
				int status = j.showSaveDialog(getParent());
				if (status == JFileChooser.APPROVE_OPTION) {
					try {
						BufferedImage img = image.getImage();
						File f = j.getSelectedFile();
						ImageIO.write(img, "png", f);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(getParent(), e2.getMessage(), "Error save",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + cmd);
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
		FractalExplorer app = new FractalExplorer(600);
		app.createAndShowGUI();
		app.drawFractal();
	}
}
