import java.awt.geom.Rectangle2D.Double;

public class Tricorn extends FractalGenerator {

	public static final int MAX_ITERATIONS = 1000;

	@Override
	public void getInitialRange(Double range) {
		range.x = -2;
		range.y = -2;
		range.width = 4;
		range.height = 4;
	}

	@Override
	public int numIterations(double x, double y) {
		double xn = 0, yn = 0;
		double zk = 0, tmp = 0;
		int i;
		for (i = 0; i < MAX_ITERATIONS; i++) {
			tmp = xn * xn - yn * yn + x;
			yn = - 2 * xn * yn + y;
			xn = tmp;
			zk = xn * xn + yn * yn;
			if (zk > 4) return i;
		}

		return (i == MAX_ITERATIONS) ? -1 : i;
	}

	@Override
	public String toString() {
		return "Треугольник";
	}
}
