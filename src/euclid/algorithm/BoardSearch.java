package euclid.algorithm;

import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.Problem;

abstract class BoardSearch implements Algorithm <Board> {
	
	final Problem problem;
	final Algebra algebra;
	
	BoardSearch(final Problem problem, final Algebra algebra) {
		this.problem = problem;
		this.algebra = algebra;
	}

	@Override
	public Board first() {
		return problem.initial();
	}

	@Override
	public int depth(final Board board) {
		return board.depth();
	}

	@Override
	public int maxPriority() {
		return problem.required().points().size() + problem.required().curves().size();
	}
	
	@Override
	public int priority(final Board b) {
		final int depth = depth(b);
		final int curveMisses = curveMisses(b);
		final int pointMisses = pointMisses(b);
		if(missingDepth(pointMisses, curveMisses) > problem.maxDepth() - depth) {
			return -1;
		}
		return pointMisses + curveMisses;
	}

	abstract int missingDepth(final int pointMisses, final int curveMisses);

	int curveMisses(final Board b) {
		int count = 0;
		final CurveSet curves = b.curves();
		for(final Curve c : problem.required().curves()) {
			if(curves.containsNot(c)) {
				count++;
			}
		}
		return count;
	}

	int pointMisses(final Board b) {
		int count = 0;
		final PointSet points = b.points();
		for(final Point p : problem.required().points()) {
			if(points.containsNot(p)) {
				count++;
			}
		}
		return count;
	}
	
	Set<Curve> createAllCurves(final PointSet set) {
		final Set<Curve> curves = new TreeSet<>();
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
				if(c1.compareTo(c2) < 0) {
					algebra.intersect(c1,c2).forEach(points::add);
				}
		return points;
	}

}
