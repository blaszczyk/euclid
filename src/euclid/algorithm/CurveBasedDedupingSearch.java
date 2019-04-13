package euclid.algorithm;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveBuiltBoard;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class CurveBasedDedupingSearch extends CurveBasedSearch<CurveBuiltBoard> {
	
	public CurveBasedDedupingSearch(final Problem problem) {
		super(problem);
	}
	
	@Override
	public CurveBuiltBoard decorateFirst(final Board first) {
		final PointSet points = first.points().copy();
		addAllIntersections(first.curves(), points);
		return new CurveBuiltBoard(points, first.curves());
	}

	@Override
	CurveBuiltBoard next(final CurveBuiltBoard parent, final Curve successor) {
		final PointSet points = allIntersections(parent, successor);
		return new CurveBuiltBoard(points, parent.curves().copy(), successor, parent);
	}
	
	CurveSet successors(final CurveBuiltBoard board) {
		final CurveSet successors = new CurveSet();
		final PointSet lastPoints = board.points().copy();
		final Board parent = board.parent();
		if(parent != null) {
			final Curve last = board.last();
			final PointSet oldPoints = parent.points();
			lastPoints.removeAll(oldPoints);
			forEachDistinctPair(oldPoints.asList(), (p1,p2) -> {
				addIfGreater(Algebra.line(p1, p2), last, successors);
				addIfGreater(Algebra.circle(p1, p2), last, successors);
				addIfGreater(Algebra.circle(p2, p1), last, successors);
			});
			for(final Point oldPoint : oldPoints) {
				for(final Point lastPoint : lastPoints) {
					addAllCurves(oldPoint, lastPoint, successors);
				}
			}
		}
		forEachDistinctPair(lastPoints.asList(), (p1,p2) -> {
			addAllCurves(p1, p2, successors);
		});
		return successors;
	}

	private static void addIfGreater(final Curve newCurve, final Curve lastCurve, final CurveSet curves) {
		if(newCurve.greater(lastCurve)) {
			curves.add(newCurve);
		}
	}

}
