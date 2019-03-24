package euclid.alg;

import java.util.Collection;
import java.util.stream.Collectors;

import euclid.model.*;

public class PointBasedSearch extends BoardSearch<PointSet> {
	
	public PointBasedSearch(final Board initial, final Board required, final int maxDepth) {
		super(initial, required, maxDepth + initial.points().size());
	}

	@Override
	PointSet first() {
		return initial.points();
	}

	@Override
	Board digest(final PointSet points) {
		final CurveSet curves = points.curves().adjoin(initial.curves());
		return Board.withPoints(points).andCurves(curves);
	}

	@Override
	int depth(final Board board) {
		return board.points().size();
	}

	@Override
	Collection<PointSet> generateNext(final Board board) {
		final PointSet points = board.points();
		final PointSet successors = board.curves().intersections();
		successors.removeAll(points);
		return successors.stream().map(points::adjoin).collect(Collectors.toList());
	}
	
}
