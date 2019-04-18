package euclid.geometry;

public class Circle extends Curve {
	
	private final Point center;
	private final Number radiusSquare;

	public Circle(final Point center, final Number radiusSquare) {
		if(radiusSquare.less(Number.ZERO)) {
			throw new IllegalArgumentException("radius square must be positive");
		}
		this.center = center;
		this.radiusSquare = radiusSquare;
	}
	
	public Point center() {
		return center;
	}
	
	public Number radiusSquare() {
		return radiusSquare;
	}
	
	@Override
	public boolean isCircle() {
		return true;
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
	public String toString() {
		return "circle ( p - " + center + " )^2 = " + radiusSquare;
	}

	@Override
	public int compareTo(final Curve other) {
		if(other.isLine()) {
			return 1;
		};
		final Circle circle = other.asCircle();
		final int compCenter = center.compareTo(circle.center);
		return compCenter != 0 ? compCenter : radiusSquare.compareTo(circle.radiusSquare);
	}

}
