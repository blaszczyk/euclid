package euclid.geometry;

public class Segment extends Line {

	final Number from;
	final Number to;

	public Segment(final Point normal, final Number offset, final Number end1, final Number end2) {
		super(normal, offset);
		final Number norm = norm(normal, offset);
		final Number normEnd1 = end1.div(norm);
		final Number normEnd2 = end2.div(norm);
		this.from = normEnd1.min(normEnd2);
		this.to = normEnd1.max(normEnd2);
	}
	
	public Number from() {
		return from;
	}
	
	public Number to() {
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
				return isNear(line) && from.near(segment.from()) && to.near(segment.to());
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 421 * super.hashCode() + 787 * from.hashCode() + 239 * to.hashCode();
	}

	@Override
	public String toString() {
		return "segment " + normal() + " * p = " + offset() + "; " + from + " <= t <= " + to;
	}

	@Override
	public int compareTo(final Curve other) {
		if(other.isCircle()) {
			return -1;
		};
		final Line line = other.asLine();
		if(line.isSegment()) {
			final int compLine = compareAsLine(line);
			if(compLine!= 0) {
				return compLine;
			}
			final Segment segment = line.asSegment();
			final int compFrom = from.compareTo(segment.from);
			return compFrom!= 0 ? compFrom : to.compareTo(segment.to);
		}
		return 1;
	}

}
