package euclid.model;

import static euclid.model.Sugar.*;

public class Point implements Comparable<Point>{
	
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
		return p(x.add(other.x), y.add(other.y));
	}
	
	public Point sub(final Point other) {
		return p(x.sub(other.x), y.sub(other.y));
	}
	
	public Point mul(final Constructable factor) {
		return p(x.mul(factor), y.mul(factor));
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
		return p(y, x.negate());
	}
	
	public boolean colinear(final Point other) {
		return x.mul(other.y).isEqual(y.mul(other.x));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
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
