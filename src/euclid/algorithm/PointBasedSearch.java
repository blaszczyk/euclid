package euclid.algorithm;

import java.util.ArrayList;
import java.util.List;

import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class PointBasedSearch extends BoardSearch<Board> {
	
	public PointBasedSearch(final Problem problem, final Prioritizer prioritizer) {
		super(problem, prioritizer);
	}
	
	@Override
	public Board decorateFirst(final Board first) {
		final CurveSet curves = first.curves().copy();
		addAllCurves(first.points(), curves);
		return new Board(first.points(), curves);
	}

	@Override
	public List<Board> nextGeneration(final Board board) {
		final PointSet points = board.points();
		final PointSet successors = addAllIntersections(board.curves(), new PointSet());
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Point successor : successors) {
			if(points.containsNot(successor)) {
				final CurveSet curves = board.curves().copy();
				for(final Point point : points) {
					addAllCurves(point, successor, curves);
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
