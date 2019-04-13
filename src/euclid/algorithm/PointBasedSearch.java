package euclid.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class PointBasedSearch extends BoardSearch {
	
	public PointBasedSearch(final Problem problem) {
		super(problem);
	}
	
	@Override
	public Board first() {
		final Board preFirst = super.first();
		final CurveSet curves = preFirst.curves().copy();
		addAllCurves(preFirst.points(), curves);
		return new Board(preFirst.points(), curves);
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final PointSet points = board.points();
		final PointSet successors = addAllIntersections(board.curves(), new PointSet());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Point successor : successors) {
			if(points.containsNot(successor)) {
				final CurveSet curves = board.curves().copy();
				for(final Point point : points) {
					curves.add(Algebra.line(successor, point));
					curves.add(Algebra.circle(successor, point));
					curves.add(Algebra.circle(point, successor));
				}
				final Board next = new Board(points.adjoin(successor), curves, board).identifyByPoints();
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
