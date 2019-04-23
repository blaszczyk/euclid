package euclid.algorithm;

import euclid.algebra.Algebra;
import euclid.sets.Board;
import euclid.sets.CurveSet;

public class LinesOnlySearch extends BasicCurveBasedSearch {

	@Override
	void addSuccessors(final Board board, final CurveSet successors) {
		forEachDistinctPair(board.points().asList(), (p1,p2) -> {
			successors.add(Algebra.line(p1,p2));
		});
	}

}
