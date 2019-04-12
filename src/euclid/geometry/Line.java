package euclid.geometry;

public class Line extends AbstractElement<Curve> implements Curve {
	
	private final Point normal;
	private final Number offset;

	public Line(final Point normal, final Number offset) {
		final Number norm = norm(normal, offset);
		this.normal = normal.div(norm);
		this.offset = offset.div(norm);
	}
	
	public Point normal() {
		return normal;
	}
	
	public Number offset() {
		return offset;
	}

	static Number norm(final Point normal, final Number offset) {
		final Number norm = normal.square().root();
		final boolean negate;
		final int signO = offset.sign();
		if(signO == 0) {
			final int signX = normal.x().sign();
			if(signX == 0) {
				negate = normal.y().sign() < 0;
			}
			else {
				negate = signX < 0;
			}
		}
		else {
			negate = signO < 0;
		}
		return negate ? norm.negate() : norm;
	}

	@Override
	public boolean isLine() {
		return true;
	}
	
	@Override
	public Line asLine() {
		return this;
	}

	public boolean isSegment() {
		return false;
	}

	public Segment asSegment() {
		throw new ClassCastException(this + " is not a segment");
	}
	
	@Override
	public boolean near(final Curve other) {
		if(other.isLine()) {
			final Line line = other.asLine();
			if(line.isSegment()) {
				return false;
			}
			return isNear(line);
		}
		return false;
	}
	
	boolean isNear(final Line other) {
		return offset.near(other.offset) && normal.near(other.normal);
	}
	
	@Override
	public int hashCode() {
		return 137 * normal.hashCode() + 353 * offset.hashCode();
	}

	@Override
	public String toString() {
		return "line " + normal + " * p = " + offset;
	}

	@Override
	public int compareTo(final Curve other) {
		if(other.isCircle()) {
			return -1;
		};
		final Line line = other.asLine();
		if(line.isSegment()) {
			return -1;
		}
		return doCompareTo(line);
	}
	
	int doCompareTo(final Line other) {
		final int compNormal = normal.compareTo(other.normal);
		return compNormal != 0 ? compNormal : offset.compareTo(other.offset);
	}

}
