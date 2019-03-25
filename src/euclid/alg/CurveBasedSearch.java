package euclid.alg;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import euclid.model.*;

public class CurveBasedSearch extends BoardSearch<CurveSet>{
	
	public CurveBasedSearch(final Board initial, final Collection<Board> required) {
		super(initial, required);
	}

	@Override
	public CurveSet first() {
		return initial.curves();
	}

	@Override
	public Board digest(final CurveSet curves) {
		final Set<Point> points = curves.intersections();
		initial.points().forEach(points::add);
		return Board.withPoints(points).andCurves(curves);
	}

	@Override
	public int depth(final Board board) {
		return board.curves().size() - initial.curves().size();
	}

	@Override
	public Collection<CurveSet> generateNext(final Board board) {
		final CurveSet curves = board.curves();
		final Set<Curve> successors = board.points().curves();
		return successors.stream()
				.filter(c -> !curves.contains(c))
				.map(curves::adjoin)
				.collect(Collectors.toList());
	}
}
