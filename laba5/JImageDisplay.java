import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class JImageDisplay extends JComponent {

	private BufferedImage image;

	public JImageDisplay(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void clearImage() {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setRGB(i, j, 0);
			}
		}
	}

	public void drawPixel(int x, int y, int rgbColor) {
		image.setRGB(x, y, rgbColor);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
