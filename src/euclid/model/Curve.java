package euclid.model;

public interface Curve {
	
	public PointSet intersect(final Curve other);
	
	public boolean contains(final Point point);
	
	public boolean isEqual(final Curve other);

}
