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
		if(other instanceof Line) {
			final Line line = (Line)other;
			return offset.equals(line.offset) && normal.equals(line.normal);
		}
		return false;
	}

	@Override
	public String toString() {
		return "line " + normal + " * p = " + offset;
	}

}
