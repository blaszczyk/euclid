package euclid.alg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import euclid.model.*;

public class CurveBasedSearch extends BoardSearch{
	
	public CurveBasedSearch(final Board initial, final Collection<Board> required, final Algebra algebra) {
		super(initial.identifyByCurves(), required, algebra);
	}

	@Override
	public Board digest(final Board board) {
		final Set<Point> points = createAllIntersections(board.curves());
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
		final Set<Curve> successors = createAllCurves(board.points());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			if(curves.containsNot(successor)) {
				final Board next = Board.withPoints(board.points()).andCurves(curves.adjoin(successor)).identifyByCurves();
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}
}
