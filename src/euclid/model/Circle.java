package euclid.model;

public class Circle implements Curve {
	
	final Point center;
	final Constructable radiusSquare;

	Circle(final Point center, final Constructable radiusSquare) {
		this.center = center;
		this.radiusSquare = radiusSquare;
	}
	
	@Override
	public boolean isEqual(final Curve other) {
		if(other.isCircle()) {
			final Circle circle = other.asCircle();
			return circle.center.isEqual(center) && circle.radiusSquare.isEqual(radiusSquare);
		}
		return false;
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
	public String toString() {
		return "circle ( p - " + center + " )^2 = " + radiusSquare;
	}

}
