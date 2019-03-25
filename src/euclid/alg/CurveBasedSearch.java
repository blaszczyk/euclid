package euclid.alg;

import java.util.Collection;
import java.util.Set;
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
		final Set<Point> points = curves.intersections();
		initial.points().forEach(points::add);
		return Board.withPoints(points).andCurves(curves);
	}

	@Override
	int depth(final Board board) {
		return board.curves().size() - initial.curves().size();
	}

	@Override
	Collection<CurveSet> generateNext(final Board board) {
		final CurveSet curves = board.curves();
		final Set<Curve> successors = board.points().curves();
		return successors.stream()
				.filter(c -> !curves.contains(c))
				.map(curves::adjoin)
				.collect(Collectors.toList());
	}
}
