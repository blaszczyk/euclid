package euclid.model;

public class Line implements Curve {
	
	final Point normal;
	final Constructable offset;

	Line(final Point normal, final Constructable offset) {
		final Constructable normalization = normal.square().root();
		this.normal = normal.div(normalization);
		this.offset = offset.div(normalization);
	}

	@Override
	public boolean isEqual(final Curve other) {
		if(other.isLine()) {
			final Line line = other.asLine();
			return offset.isEqual(line.offset) && normal.isEqual(line.normal);
		}
		return false;
	}
	
	@Override
	public boolean isLine() {
		return true;
	}
	
	@Override
	public Line asLine() {
		return this;
	}

	@Override
	public String toString() {
		return "line " + normal + " * p = " + offset;
	}

}
