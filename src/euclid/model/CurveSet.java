package euclid.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CurveSet extends ElementSet<Curve> {
	
	private static final CurveSet EMPTY = new CurveSet(0);
	
	public static CurveSet of(final Curve... curves) {
		return of(Arrays.asList(curves));
	}
	
	public static CurveSet of(final Collection<Curve> curves) {
		final CurveSet result = new CurveSet(curves);
		result.computeHash();
		return result;
	}

	public static CurveSet empty() {
		return EMPTY;
	}
	
	private final HashSet<Curve> set;
	
	private CurveSet(final int capacity) {
		set = new HashSet<>(capacity);
	}
	
	private CurveSet(final Collection<Curve> init) {
		set = new HashSet<>(init);
	}

	public Set<Point> intersections() {
		final Set<Point> points = new TreeSet<>();
		for(final Curve c1 : set)
			for(final Curve c2 : set)
				points.addAll(c1.intersect(c2).set());
		return points;
	}

	public CurveSet adjoin(final Curve c) {
		final CurveSet result = new CurveSet(set);
		result.set.add(c);
		result.computeHash();
		return result;
	}

	public CurveSet adjoin(final CurveSet curves) {
		final CurveSet result = new CurveSet(set);
		result.set.addAll(curves.set);
		result.computeHash();
		return result;
	}

	@Override
	Collection<Curve> set() {
		return set;
	}

}
