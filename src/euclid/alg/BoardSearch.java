package euclid.alg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;

abstract class BoardSearch implements Algorithm <Board> {
	
	final Board initial;
	final Collection<Board> required;
	final Algebra algebra;
	private final int maxMisses;
	
	BoardSearch(final Board initial, final Collection<Board> required, final Algebra algebra) {
		this.initial = initial;
		this.required = required;
		this.algebra = algebra;
		maxMisses = required.stream()
				.mapToInt(b -> b.points().size() + b.curves().size())
				.min()
				.orElse(0);
	}

	@Override
	public Board first() {
		return initial;
	}
	
	@Override
	public int misses(final Board candidate) {
		int misses = maxMisses;
		for(final Board possibility : required) {
			final long pointMisses = possibility.points().stream()
					.filter(candidate.points()::containsNot)
					.count();
			final long curveMisses = possibility.curves().stream()
					.filter(candidate.curves()::containsNot)
					.count();
			misses = Math.min(misses, (int)(pointMisses + curveMisses));
		}
		return misses;
	}
	
	@Override
	public int maxMisses() {
		return maxMisses;
	}
	
	Set<Curve> createAllCurves(final PointSet set) {
		final Set<Curve> curves = new HashSet<>(3 * set.size() * set.size());
		for(final Point p1 : set)
			for(final Point p2 : set)
				if(p1.compareTo(p2) < 0)
				{
					curves.add(algebra.line(p1,p2));
					curves.add(algebra.circle(p1,p2));
					curves.add(algebra.circle(p2,p1));
				}
		return curves;
	}

	Set<Point> createAllIntersections(final CurveSet curves) {
		final Set<Point> points = new TreeSet<>();
		for(final Curve c1 : curves)
			for(final Curve c2 : curves)
				algebra.intersect(c1,c2).forEach(points::add);
		return points;
	}

}
