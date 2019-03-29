package euclid.model;

public class Circle implements Curve {
	
	final Point center;
	final Constructable radiusSquare;

	Circle(final Point center, final Constructable radiusSquare) {
		this.center = center;
		this.radiusSquare = radiusSquare;
	}
	
	@Override
	public boolean isLine() {
		return false;
	}
	
	@Override
	public Circle asCircle() {
		return this;
	}
	
	@Override
	public boolean near(final Curve other) {
		if(other.isCircle()) {
			final Circle circle = other.asCircle();
			return circle.center.near(center) && circle.radiusSquare.near(radiusSquare);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 83 * center.hashCode() + 263 * radiusSquare.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		final Curve other = (Curve) obj;
		if(other.isCircle()) {
			final Circle circle = other.asCircle();
			return circle.center.equals(center) && circle.radiusSquare.equals(radiusSquare);
		}
		return false;
	}

	@Override
	public String toString() {
		return "circle ( p - " + center + " )^2 = " + radiusSquare;
	}

}
