package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import euclid.model.*;

public class PointBasedSearch extends BoardSearch {
	
	public PointBasedSearch(final Board initial, final Collection<Board> required, final Algebra algebra) {
		super(initial.identifyByPoints(), required, algebra);
	}

	@Override
	public Board digest(final Board board) {
		final Set<Curve> curves = createAllCurves(board.points());
		board.curves().forEach(curves::add);
		return Board.withPoints(board.points()).andCurves(curves);
	}

	@Override
	public int depth(final Board board) {
		return board.points().size() - initial.points().size();
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final PointSet points = board.points();
		final Set<Point> successors = createAllIntersections(board.curves());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Point successor : successors) {
			if(points.containsNot(successor)) {
				final Board next = Board.withPoints(points.adjoin(successor)).andCurves(board.curves()).identifyByPoints();
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}
	
}
