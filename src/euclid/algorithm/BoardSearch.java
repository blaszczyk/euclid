package euclid.algorithm;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.Problem;

abstract class BoardSearch implements Algorithm <Board> {
	
	final Problem problem;
	final Algebra algebra;
	private final int maxMisses;
	
	BoardSearch(final Problem problem, final Algebra algebra) {
		this.problem = problem;
		this.algebra = algebra;
		maxMisses = problem.required().points().size() + problem.required().curves().size();
	}

	@Override
	public Board first() {
		return problem.initial();
	}
	
	@Override
	public int misses(final Board candidate) {
		final long pointMisses = problem.required().points().stream()
				.filter(candidate.points()::containsNot)
				.count();
		final long curveMisses = problem.required().curves().stream()
				.filter(candidate.curves()::containsNot)
				.count();
		return (int)(pointMisses + curveMisses);
	}
	
	@Override
	public int maxMisses() {
		return maxMisses;
	}
	
	@Override
	public int maxDepth() {
		return problem.maxDepth();
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
