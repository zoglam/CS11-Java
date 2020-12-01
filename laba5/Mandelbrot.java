import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

	public static final int MAX_ITERATIONS = 2000;

	public void getInitialRange(Rectangle2D.Double range) {
		range.x = -2;
		range.y = -1.5;
		range.width = 3;
		range.height = 3;
	}

	public int numIterations(double x, double y) {

		double xn = 0;
		double yn = 0;
		int i;
		for (i = 0; i < MAX_ITERATIONS; i++) {
			double xn_temp = xn * xn - yn * yn + x;
			yn = 2 * xn * yn + y;
			xn = xn_temp;

			if (xn * xn + yn * yn > 4) {
				return i;
			}
		}
		return i == 2000 ? -1 : i;
	}
	
	@Override
	public String toString() {
		return "Mandelbrot";
	}
}