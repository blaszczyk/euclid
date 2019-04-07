package euclid.model;

import static euclid.model.Sugar.*;

public class Point implements Comparable<Point>, Element<Point> {
	
	final Constructable x;
	final Constructable y;
	
	Point(Constructable x, Constructable y) {
		this.x = x;
		this.y = y;
	}

	public final Constructable x() {
		return x;
	}

	public final Constructable y() {
		return y;
	}
	
	public Point add(final Point other) {
		return point(x.add(other.x), y.add(other.y));
	}
	
	public Point sub(final Point other) {
		return point(x.sub(other.x), y.sub(other.y));
	}
	
	public Point mul(final Constructable factor) {
		return point(x.mul(factor), y.mul(factor));
	}

	public Point div(final Constructable factor) {
		return point(x.div(factor), y.div(factor));
	}
	
	public Constructable mul(final Point other) {
		return x.mul(other.x).add(y.mul(other.y));
	}
	
	public Constructable square() {
		return mul(this);
	}
	
	public Constructable cross(final Point other) {
		return x.mul(other.y).sub(y.mul(other.x));
	}

	public Point orth() {
		return point(y.negate(), x);
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
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		final Point other = (Point) obj;
		return x.equals(other.x) && y.equals(other.y);
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
