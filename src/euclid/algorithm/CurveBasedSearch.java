package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.Problem;

public class CurveBasedSearch extends BoardSearch {
	
	public CurveBasedSearch(final Problem problem, final Algebra algebra) {
		super(problem, algebra);
	}
	
	@Override
	public Board first() {
		final Board preFirst = super.first();
		final Set<Point> points = createAllIntersections(preFirst.curves());
		preFirst.points().forEach(points::add);
		return Board.withPoints(points).andCurves(preFirst.curves());
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final CurveSet curves = board.curves();
		final Set<Curve> successors = successors(board);
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			if(curves.containsNot(successor)) {
				final Set<Point> points = new TreeSet<>();
				board.points().forEach(points::add);
				for(final Curve curve : curves) {
					algebra.intersect(successor, curve).forEach(points::add);
				}
				final Board next = Board.withPoints(points).andCurves(curves.adjoin(successor)).identifyByCurves().parent(board);
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}

	@Override
	int missingDepth(final int pointMisses, final int curveMisses) {
		return Math.max(curveMisses, pointMisses > 0 ? 1 : 0);
	}
	
	Set<Curve> successors(final Board board) {
		return createAllCurves(board.points());
	}
	
}
