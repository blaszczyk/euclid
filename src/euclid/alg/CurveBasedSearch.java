package euclid.alg;

import java.util.Collection;
import java.util.stream.Collectors;

import euclid.model.*;

public class CurveBasedSearch extends BoardSearch<CurveSet>{
	
	public CurveBasedSearch(final Board initial, final Board required, final int maxDepth) {
		super(initial, required, maxDepth);
	}

	@Override
	CurveSet first() {
		return initial.curves();
	}

	@Override
	Board digest(final CurveSet curves) {
		final PointSet points = curves.intersections().adjoin(initial.points());
		return Board.withPoints(points).andCurves(curves);
	}

	@Override
	int depth(final Board board) {
		return board.curves().size() - initial.curves().size();
	}

	@Override
	Collection<CurveSet> generateNext(final Board board) {
		final CurveSet curves = board.curves();
		final CurveSet successors = board.points().curves();
		successors.removeAll(curves);
		return successors.stream().map(curves::adjoin).collect(Collectors.toList());
	}
}
