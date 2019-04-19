package euclid.geometry;

public class Ray extends Line {

	final Number end;
	
	final boolean orientation;

	public Ray(final Point normal, final Number offset, final Number end, final boolean orientation) {
		super(normal, offset);
		final Number norm = norm(normal, offset);
		this.end = end.div(norm);
		this.orientation = orientation ^ norm.less(Number.ZERO);
	}
	
	public Number end() {
		return end;
	}
	
	public boolean orientation() {
		return orientation;
	}
	
	@Override
	public boolean isRay() {
		return true;
	}
	
	@Override
	public Ray asRay() {
		return this;
	}
	
	@Override
	public boolean near(final Curve other) {
		if(other.isLine()) {
			final Line line = other.asLine();
			if(line.isRay()) {
				final Ray ray = line.asRay();
				return isNear(line) && end.near(ray.end()) && orientation == ray.orientation();
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 571 * super.hashCode() + 277 * end.hashCode() + (orientation ? 929 : 0);
	}

	@Override
	public String toString() {
		return "ray " + normal() + " * p = " + offset() + ";  t " + (orientation ? ">= " : "<= ") + end;
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
		if(line.isRay()) {
			final int compLine = compareAsLine(line);
			if(compLine!= 0) {
				return compLine;
			}
			final Ray ray = line.asRay();
			final int compEnd = end.compareTo(ray.end);
			return compEnd != 0 ? compEnd : ( orientation == ray.orientation() ? 0 : ( orientation ? 1 : -1) );
		}
		return 1;
	}

}
