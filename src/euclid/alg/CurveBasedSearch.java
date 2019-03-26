package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import euclid.model.*;

public class CurveBasedSearch extends BoardSearch{
	
	public CurveBasedSearch(final Board initial, final Collection<Board> required) {
		super(initial, required);
	}

	@Override
	public Board digest(final Board board) {
		final Set<Point> points = board.curves().intersections();
		board.points().forEach(points::add);
		return Board.withPoints(points).andCurves(board.curves());
	}

	@Override
	public int depth(final Board board) {
		return board.curves().size() - initial.curves().size();
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final CurveSet curves = board.curves();
		final Set<Curve> successors = board.points().curves();
		final List<Board> next = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			if(curves.containsNot(successor)) {
				next.add(Board.withPoints(board.points()).andCurves(curves.adjoin(successor)));
			}
		}
		return next;
	}
}
