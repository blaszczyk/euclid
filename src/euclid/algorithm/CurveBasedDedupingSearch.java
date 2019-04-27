package euclid.algorithm;

import java.util.List;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.sets.Board;
import euclid.sets.CurveBuiltBoard;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class CurveBasedDedupingSearch extends CurveBasedSearch<CurveBuiltBoard> {
	
	@Override
	public CurveBuiltBoard first() {
		final Board initial = problem.initial();
		final PointSet points = initial.points().copy();
		forEachDistinctPair(initial.curves().asList(), (c1,c2) -> {
			points.addAll(Algebra.intersect(c1, c2));
		});
		return new CurveBuiltBoard(points, initial.curves());
	}

	@Override
	CurveBuiltBoard next(final CurveBuiltBoard parent, final Curve successor) {
		final PointSet points = parent.points().copy();
		for(final Curve curve : parent.curves()) {
			points.addAll(Algebra.intersect(successor, curve));
		}
		return new CurveBuiltBoard(points, parent.curves().copy(), successor, parent);
	}

	@Override
	void addSuccessors(final CurveBuiltBoard board, final CurveSet successors) {
		final Board parent = board.parent();
		if(parent != null) {
			final Curve last = board.last();
			@SuppressWarnings("serial")
			final CurveSet dedupeSuccessors = new CurveSet(curveComparator) {
				public boolean add(final Curve curve) {
					if(curve.greater(last)) {
						return super.add(curve);
					}
					return false;
				};
			};

			final List<Point> oldPoints = parent.points().asList();
			final List<Point> newPoints = board.points().asList();
			newPoints.removeAll(oldPoints);

			forEachDistinctPair(oldPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, dedupeSuccessors));
			forEachPair(oldPoints, newPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));
			forEachDistinctPair(newPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));

			if(constructor.isAdvanced()) {
				final List<Line> oldLines = pickLines(board);
				oldLines.remove(board.last());
				
				forEachDistinctTriple(oldPoints, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, dedupeSuccessors));
				forEachDistinctPairAndSingle(oldPoints, newPoints, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));
				forEachDistinctPairAndSingle(newPoints, oldPoints, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));
				forEachDistinctTriple(newPoints, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));

				forEachPair(oldPoints, oldLines, (p,l) -> constructor.constructFromPointAndLine(p, l, dedupeSuccessors));
				forEachPair(newPoints, oldLines, (p,l) -> constructor.constructFromPointAndLine(p, l, successors));
				forEachDistinctPair(oldLines, (l1,l2) -> constructor.constructFromDistinctLines(l1, l2, dedupeSuccessors));

				if(last.isLine()) {
					final Line l = last.asLine();
					oldPoints.forEach(p -> constructor.constructFromPointAndLine(p, l, successors));
					newPoints.forEach(p -> constructor.constructFromPointAndLine(p, l, successors));
					oldLines.forEach(l2 -> constructor.constructFromDistinctLines(l, l2, successors));
				}
			}
			successors.addAll(dedupeSuccessors);
		}
		else
		{
			final List<Point> ps = board.points().asList();
			forEachDistinctPair(ps, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));
			if(constructor.isAdvanced()) {
				final List<Line> lines = pickLines(board);
				forEachPair(ps, lines, (p,l) -> constructor.constructFromPointAndLine(p, l, successors));
				forEachDistinctTriple(ps, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));
				forEachDistinctPair(lines, (l1,l2) -> constructor.constructFromDistinctLines(l1, l2, successors));
			}
		}
	}

}
