package euclid.geometry;

public class Point extends Element<Point> {
	
	public static final Point ORIGIN = new Point(Number.ZERO, Number.ZERO);
	
	private final Number x;
	private final Number y;
	
	public Point(Number x, Number y) {
		this.x = x;
		this.y = y;
	}

	public final Number x() {
		return x;
	}

	public final Number y() {
		return y;
	}
	
	public Point add(final Point other) {
		return new Point(x.add(other.x), y.add(other.y));
	}
	
	public Point sub(final Point other) {
		return new Point(x.sub(other.x), y.sub(other.y));
	}
	
	public Point mul(final Number factor) {
		return new Point(x.mul(factor), y.mul(factor));
	}

	public Point div(final Number factor) {
		return new Point(x.div(factor), y.div(factor));
	}
	
	public Number mul(final Point other) {
		return x.mul(other.x).add(y.mul(other.y));
	}
	
	public Number square() {
		return mul(this);
	}
	
	public Number cross(final Point other) {
		return x.mul(other.y).sub(y.mul(other.x));
	}

	public Point orth() {
		return new Point(y.negate(), x);
	}

	public Point negate() {
		return new Point(x.negate(), y.negate());
	}
	
	public boolean colinear(final Point other) {
		return x.mul(other.y).equals(y.mul(other.x));
	}
	
	@Override
	public boolean near(final Point other) {
		return x.near(other.x) && y.near(other.y);
	}
	
	@Override
	public int hashCode() {
		return 59 * x.hashCode() + 31 * y.hashCode();
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public int compareTo(Point other) {
		int cx = x.compareTo(other.x);
		return cx!=0 ? cx : y.compareTo(other.y);
	}
	
}
