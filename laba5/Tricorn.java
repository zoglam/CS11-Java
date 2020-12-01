import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

	public static final int MAX_ITERATIONS = 2000;

	public void getInitialRange(Rectangle2D.Double range) {
		range.x = -2;
		range.y = -2;
		range.width = 4;
		range.height = 4;
	}

	public int numIterations(double x, double y) {

		double xn = 0;
		double yn = 0;
		int i;
		for (i = 0; i < MAX_ITERATIONS; i++) {
			double xn_temp = xn * xn - yn * yn + x;
			yn = -2 * xn * yn + y;
			xn = xn_temp;

			if (xn * xn + yn * yn > 4) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return "Tricorn";
	}
}