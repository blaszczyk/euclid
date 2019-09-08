package euclid.algorithm;

import java.util.List;

import euclid.geometry.*;
import euclid.sets.*;
import static euclid.algorithm.ListHelper.*;

public class CurveBasedDedupingSearch extends BasicCurveBasedSearch {

	@Override
	void addSuccessors(final Board board, final List<Point> points, final CurveSet successors) {
		if(board.hasParent()) {
			final Board parent = board.parent();
			final Curve last = board.curve();

			if(data.required().curves().contains(last))
			{
				super.addSuccessors(board, points, successors);
				return;
			}

			@SuppressWarnings("serial")
			final CurveSet dedupeSuccessors = new CurveSet(curveComparator) {
				public boolean add(final Curve curve) {
					if(curve.greater(last)) {
						return super.add(curve);
					}
					return false;
				};
			};

			final List<Point> oldPoints = parent.pointList();
			final List<Point> newPoints = board.newPoints();

			forEachDistinctPair(oldPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, dedupeSuccessors));
			forEachPair(oldPoints, newPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));
			forEachDistinctPair(newPoints, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));

			if(constructor.isAdvanced()) {
				final List<Line> oldLines = board.parent().lineList();
				
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
			super.addSuccessors(board, points, successors);
		}
	}

}
