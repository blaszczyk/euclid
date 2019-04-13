package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

abstract class CurveBasedSearch<B extends Board> extends BoardSearch<B> {
	
	public CurveBasedSearch(final Problem problem) {
		super(problem);
	}

	@Override
	public Collection<B> nextGeneration(final B board) {
		final CurveSet successors = successors(board);
		successors.removeAll(board.curves());
		final List<B> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
				final B next = next(board, successor);
				nextGeneration.add(next);
		}
		return nextGeneration;
	}

	@Override
	int missingDepth(final int pointMisses, final int curveMisses) {
		return Math.max(curveMisses, pointMisses > 0 ? 1 : 0);
	}
	
	PointSet allIntersections(final Board parent, final Curve successor) {
		final PointSet points = parent.points().copy();
		final CurveSet curves = parent.curves();
		for(final Curve curve : curves) {
			points.addAll(Algebra.intersect(successor, curve));
		}
		return points;
	}

	abstract CurveSet successors(final B board);

	abstract B next(final B parent, final Curve successor);
	
}
