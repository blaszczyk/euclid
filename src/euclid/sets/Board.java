package euclid.sets;

public class Board {
	
	public static final Board EMPTY = new Board(PointSet.EMPTY, CurveSet.EMPTY);

	private final PointSet points;
	private final CurveSet curves;
	private final Board parent;
	private final int depth;
	
	public Board(final PointSet points, final CurveSet curves, final Board parent) {
		this.points = points;
		this.curves = curves;
		this.parent = parent;
		this.depth = parent == null ? 0 : parent.depth + 1;
	}
	
	public Board(final PointSet points, final CurveSet curves) {
		this(points, curves, null);
	}

	public final PointSet points() {
		return points;
	}

	public final CurveSet curves() {
		return curves;
	}
	
	public Board parent() {
		return parent;
	}
	
	public int depth() {
		return depth;
	}

	public boolean hasParent() {
		return parent != null;
	}
	
	@Override
	public int hashCode() {
		return points.hashCode() + curves.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		final Board other = (Board) obj;
		return points.equals(other.points) && curves.equals(other.curves);
	}
	
	@Override
	public String toString() {
		return String.format("points: %s, curves: %s", points, curves);
	}
}
