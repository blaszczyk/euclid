package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

public class PointSet extends ElementSet<Point, PointSet> {
	
	private static final PointSet EMPTY = new PointSet();
	
	public static PointSet of(final Point... points) {
		return of(Arrays.asList(points));
	}

	public static PointSet of(final Collection<? extends Point> points) {
		final PointSet result = new PointSet(points);
		return result;
	}

	public static PointSet empty() {
		return EMPTY;
	}
	
	private PointSet() {
		super(new TreeSet<>(), PointSet::new);
	}
	
	private PointSet(final Collection<? extends Point> points) {
		super(new TreeSet<>(points), PointSet::new);
	}

}
