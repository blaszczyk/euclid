package euclid.algorithm;

import java.util.List;
import java.util.function.BiConsumer;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.*;

abstract class BoardSearch<B extends Board> implements Algorithm <B> {
	
	final Problem problem;
	
	private final Prioritizer prioritizer;
	
	BoardSearch(final Problem problem, final Prioritizer prioritizer) {
		this.problem = problem;
		this.prioritizer = prioritizer;
	}

	@Override
	public B first() {
		return decorateFirst(problem.initial());
	}
	
	abstract B decorateFirst(final Board b);

	@Override
	public int depth(final B board) {
		return board.depth();
	}

	@Override
	public int maxPriority() {
		return prioritizer.maxPriority();
	}
	
	@Override
	public int priority(final B b) {
		final int depth = depth(b);
		final int curveMisses = curveMisses(b);
		final int pointMisses = pointMisses(b);
		if(missingDepth(pointMisses, curveMisses) > problem.maxDepth() - depth) {
			return -1;
		}
		return prioritizer.priotiry(b, pointMisses, curveMisses);
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

	CurveSet addAllCurves(final PointSet set, final CurveSet curves) {
		forEachDistinctPair(set.asList(), (p1,p2) -> {
			addAllCurves(p1, p2, curves);
		});
		return curves;
	}

	PointSet addAllIntersections(final CurveSet set, final PointSet points) {
		forEachDistinctPair(set.asList(), (c1,c2) -> {
			Algebra.intersect(c1, c2).forEach(points::add);
		});
		return points;
	}
	
	<E> void forEachDistinctPair(final List<E> es, final BiConsumer<E, E> consumer ) {
		for(int i = 1; i < es.size(); i++) {
			for(int j = 0; j < i; j++) {
				final E e1 = es.get(i);
				final E e2 = es.get(j);
				consumer.accept(e1, e2);
			}
		}
	};
	
	void addAllCurves(final Point p1, final Point p2, final CurveSet curves) {
		curves.add(Algebra.line(p1,p2));
		curves.add(Algebra.circle(p1,p2));
		curves.add(Algebra.circle(p2,p1));
	}

}
