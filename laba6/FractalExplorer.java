import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FractalExplorer {

	private int side;
	private int remainingLines;
	private JImageDisplay jpgImg;
	private Rectangle2D.Double vsblRange;
	private FractalGenerator geni = new BurningShip();

	public FractalExplorer(int x) {
		side = x;
		vsblRange = new Rectangle2D.Double();
		geni.getInitialRange(vsblRange);
		jpgImg = new JImageDisplay(x, x);

	}

	class ActiveLisner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("ùùùùù")) {
				jpgImg.clearImage();
				geni.getInitialRange(vsblRange);
				drawFractal();
			} else if (cmd.equals("comboBoxChanged")) {
				JComboBox<FractalGenerator> combo = (JComboBox<FractalGenerator>) e.getSource();
				geni = (FractalGenerator) combo.getSelectedItem();
				geni.getInitialRange(vsblRange);
				drawFractal();
			} else if (cmd.equals("ùùùùù")) {
				JFileChooser choozer = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				choozer.setFileFilter(filter);
				choozer.setAcceptAllFileFilterUsed(false);
				int a = choozer.showSaveDialog(choozer.getParent());
				System.out.println(e);
				if (a == JFileChooser.APPROVE_OPTION) {
					String png = "png";
					try {
						ImageIO.write((RenderedImage) jpgImg.getImg(), png, choozer.getSelectedFile());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(choozer.getParent(), ex.getMessage(), "ùù ùù ùù ùùù ùùùù",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		}
	}

	class MouseClass extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (remainingLines == 0) {
				System.out.println(e);
				double xCoord = FractalGenerator.getCoord(vsblRange.x, vsblRange.x + vsblRange.width, side, e.getX());
				double yCoord = FractalGenerator.getCoord(vsblRange.y, vsblRange.y + vsblRange.height, side, e.getY());
				geni.recenterAndZoomRange(vsblRange, xCoord, yCoord, 0.3);
				drawFractal();
			}
		}
	}

	private void createAndShowGUI() {
		JFrame f = new JFrame("ùùùùùùù ùùùùùùùùù");
		JComboBox<FractalGenerator> combo = new JComboBox<FractalGenerator>();

		f.setLayout(new BorderLayout());
		f.add(jpgImg, BorderLayout.CENTER);
		JButton b = new JButton("ùùùùù");
		JPanel p2 = new JPanel();
		JButton b2 = new JButton("ùùùùù");
		f.add(b, BorderLayout.SOUTH);
		p2.add(b2);
		p2.add(b);
		f.add(p2, BorderLayout.SOUTH);

		b.addActionListener(new ActiveLisner());
		jpgImg.addMouseListener(new MouseClass());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		combo.addItem(new BurningShip());
		combo.addItem(new Tricorn());
		combo.addItem(new Mandelbrot());
		JPanel p = new JPanel();
		p.add(new JLabel("ùùùùùùù "));
		p.add(combo);
		f.add(p, BorderLayout.NORTH);

		combo.addActionListener(new ActiveLisner());
		b2.addActionListener(new ActiveLisner());

		f.pack();
		f.setVisible(true);
		f.setResizable(false);
	}

	private void drawFractal() {
		remainingLines = side;
		for (int i = 0; i < side; i++) {
			new FractalWorker(i).execute();
		}
	}

	private void enableUI(boolean val) {
		
	}

	private class FractalWorker extends SwingWorker<Object, Object> {

		int y;
		int[] colors;

		public FractalWorker(int y) {
			this.y = y;
		}

		protected Object doInBackground() throws Exception {
			colors = new int[side];
			int status;

			double yCoord = FractalGenerator.getCoord(vsblRange.y, vsblRange.y + vsblRange.height, side, y);
			for (int x = 0; x < side; x++) {
				double xCoord = FractalGenerator.getCoord(vsblRange.x, vsblRange.x + vsblRange.width, side, x);
				status = geni.numIterations(xCoord, yCoord);
				float hue = 0.7f + (float) status / 200f;
				int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
				colors[x] = (status == -1) ? 0 : rgbColor;
			}

			return null;
		}

		protected void done() {
			enableUI(false);
			for (int x = 0; x < side; x++) {
				jpgImg.drawPixel(x, y, colors[x]);
			}
			jpgImg.repaint(0, 0, y, side, 1);

			remainingLines--;
			if (remainingLines == 0) {
				enableUI(true);
			}

		}

	}

	public static void main(String[] args) {
		FractalExplorer frac = new FractalExplorer(800);
		frac.createAndShowGUI();
		frac.drawFractal();
	}
}
