package euclid.model;

import java.util.Arrays;
import java.util.Collection;

public class PointSet extends ElementSet<Point, PointSet> {
	
	private static final PointSet EMPTY = new PointSet();
	
	public static PointSet of(final Point... points) {
		return of(Arrays.asList(points));
	}

	public static PointSet of(final Collection<? extends Point> points) {
		return new PointSet(points);
	}

	public static PointSet empty() {
		return EMPTY;
	}
	
	private PointSet() {
		super(PointSet::new);
	}
	
	private PointSet(final Collection<? extends Point> points) {
		super(points, PointSet::new);
	}

}
