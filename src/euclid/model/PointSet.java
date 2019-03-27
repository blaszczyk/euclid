package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

public class PointSet extends ElementSet<Point> {
	
	private static final PointSet EMPTY = new PointSet();
	
	public static PointSet of(final Point... points) {
		return of(Arrays.asList(points));
	}

	public static PointSet of(final Collection<Point> points) {
		final PointSet result = new PointSet(points);
		result.computeHash();
		return result;
	}

	public static PointSet empty() {
		return EMPTY;
	}
	
	private PointSet() {
		super(new TreeSet<>());
	}
	
	private PointSet(final Collection<Point> points) {
		super(new TreeSet<>(points));
	}

	public PointSet adjoin(final Point p) {
		final PointSet result = new PointSet(set);
		result.set.add(p);
		result.computeHash();
		return result;
	}

	public PointSet adjoin(final PointSet other) {
		final PointSet result = new PointSet(set);
		result.set.addAll(other.set);
		result.computeHash();
		return result;
	}

}
