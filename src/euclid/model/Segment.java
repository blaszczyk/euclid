package euclid.model;

public class Segment extends Line {

	final Constructable from;
	final Constructable to;

	Segment(final Point normal, final Constructable offset, final Constructable end1, final Constructable end2) {
		super(normal, offset);
		final Constructable norm = norm(normal, offset);
		final Constructable normEnd1 = end1.div(norm);
		final Constructable normEnd2 = end2.div(norm);
		this.from = normEnd1.min(normEnd2);
		this.to = normEnd1.max(normEnd2);
	}
	
	public Constructable from() {
		return from;
	}
	
	public Constructable to() {
		return to;
	}
	
	@Override
	public boolean isSegment() {
		return true;
	}
	
	@Override
	public Segment asSegment() {
		return this;
	}
	
	@Override
	public boolean near(final Curve other) {
		if(other.isLine()) {
			final Line line = other.asLine();
			if(line.isSegment()) {
				final Segment segment = line.asSegment();
				return offset.near(line.offset) && normal.near(line.normal) 
						&& from.near(segment.from) && to.near(segment.to);
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 421 * super.hashCode() + 787 * from.hashCode() + 239 * to.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if(this == obj) {
			return true;
		}
		final Curve other = (Curve) obj;
		if(other.isLine()) {
			final Line line = other.asLine();
			if(line.isSegment()) {
				final Segment segment = line.asSegment();
				return offset.equals(line.offset) && normal.equals(line.normal)
						&& from.equals(segment.from) && to.equals(segment.to);
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "segment " + normal + " * p = " + offset + "; " + from + " <= t <= " + to;
	}

	@Override
	public int compareTo(final Curve other) {
		if(other.isCircle()) {
			return -1;
		};
		final Line line = other.asLine();
		if(line.isSegment()) {
			final int compNormal = normal.compareTo(line.normal);
			if(compNormal!= 0) {
				return compNormal;
			}
			final int compOffset = offset.compareTo(line.offset);
			if(compOffset!= 0) {
				return compOffset;
			}
			final Segment segment = line.asSegment();
			final int compFrom = from.compareTo(segment.from);
			return compFrom!= 0 ? compFrom : to.compareTo(segment.to);
		}
		return 1;
	}

}
