package euclid.alg;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import euclid.model.*;

public class PointBasedSearch extends BoardSearch<PointSet> {
	
	public PointBasedSearch(final Board initial, final Collection<Board> required) {
		super(initial, required);
	}

	@Override
	public PointSet first() {
		return initial.points();
	}

	@Override
	public Board digest(final PointSet points) {
		final Set<Curve> curves = points.curves();
		initial.curves().forEach(curves::add);
		return Board.withPoints(points).andCurves(curves);
	}

	@Override
	public int depth(final Board board) {
		return board.points().size() - initial.points().size();
	}

	@Override
	public Collection<PointSet> generateNext(final Board board) {
		final PointSet points = board.points();
		final Set<Point> successors = board.curves().intersections();
		return successors.stream()
				.filter(p -> !points.contains(p))
				.map(points::adjoin)
				.collect(Collectors.toList());
	}
	
}
