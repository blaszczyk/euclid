package euclid.model;

public interface Curve extends Element<Curve> {
	
	public PointSet intersect(final Curve other);
	
	public boolean contains(final Point point);

}
