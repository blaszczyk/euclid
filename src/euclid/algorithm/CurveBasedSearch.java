package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import euclid.model.*;
import euclid.problem.Problem;

public class CurveBasedSearch extends BoardSearch{
	
	public CurveBasedSearch(final Problem problem, final Algebra algebra) {
		super(problem, algebra);
	}

	@Override
	public Board digest(final Board board) {
		final Set<Point> points = createAllIntersections(board.curves());
		board.points().forEach(points::add);
		return Board.withPoints(points).andCurves(board.curves()).parent(board);
	}

	@Override
	public int depth(final Board board) {
		return board.curves().size() - problem.initial().curves().size();
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final CurveSet curves = board.curves();
		final Set<Curve> successors = createAllCurves(board.points());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			if(curves.containsNot(successor)) {
				final Board next = Board.withPoints(board.points()).andCurves(curves.adjoin(successor)).identifyByCurves().parent(board);
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}
}
