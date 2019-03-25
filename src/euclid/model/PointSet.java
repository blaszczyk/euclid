package euclid.model;

import static euclid.model.ElementLifeTimeManager.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
	
	private final TreeSet<Point> set;
	
	private PointSet() {
		set = new TreeSet<>();
	}
	
	private PointSet(final Collection<Point> points) {
		set = new TreeSet<>(points);
	}

	public Set<Curve> curves() {
		final Set<Curve> curves = new HashSet<>(size() * size());
		for(final Point p1 : set)
			for(final Point p2 : set)
				if(p1.compareTo(p2) < 0)
				{
					curves.add(l(p1,p2));
					curves.add(c(p1,p2));
					curves.add(c(p2,p1));
				}
		return curves;
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

	@Override
	Collection<Point> set() {
		return set;
	}

}
