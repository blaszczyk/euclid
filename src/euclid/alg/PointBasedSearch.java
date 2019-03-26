package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import euclid.model.*;

public class PointBasedSearch extends BoardSearch {
	
	public PointBasedSearch(final Board initial, final Collection<Board> required) {
		super(initial, required);
	}

	@Override
	public Board digest(final Board board) {
		final Set<Curve> curves = board.points().curves();
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
		final Set<Point> successors = board.curves().intersections();
		final List<Board> next = new ArrayList<>(successors.size());
		for(final Point successor : successors) {
			if(points.containsNot(successor)) {
				next.add(Board.withPoints(points.adjoin(successor)).andCurves(board.curves()));
			}
		}
		return next;
	}
	
}
