package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import euclid.model.*;
import euclid.problem.Problem;

public class PointBasedSearch extends BoardSearch {
	
	public PointBasedSearch(final Problem problem, final Algebra algebra) {
		super(problem, algebra);
	}
	
	@Override
	public Board first() {
		final Board preFirst = super.first();
		final Set<Curve> curves = createAllCurves(preFirst.points());
		preFirst.curves().forEach(curves::add);
		return Board.withPoints(preFirst.points()).andCurves(curves);
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final PointSet points = board.points();
		final Set<Point> successors = createAllIntersections(board.curves());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Point successor : successors) {
			if(points.containsNot(successor)) {
				final Set<Curve> curves = new TreeSet<>();
				board.curves().forEach(curves::add);
				for(final Point point : points) {
					curves.add(algebra.line(successor, point));
					curves.add(algebra.circle(successor, point));
					curves.add(algebra.circle(point, successor));
				}
				final Board next = Board.withPoints(points.adjoin(successor)).andCurves(curves).identifyByPoints().parent(board);
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}
	
	@Override
	int missingDepth(final int pointMisses, final int curveMisses) {
		return Math.max(pointMisses, curveMisses > 0 ? 1 : 0);
	}
	
}
