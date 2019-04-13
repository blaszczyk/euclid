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

public class CurveBasedSearch extends BoardSearch {
	
	public CurveBasedSearch(final Problem problem) {
		super(problem);
	}
	
	@Override
	public Board first() {
		final Board preFirst = super.first();
		final PointSet points = preFirst.points().copy();
		addAllIntersections(preFirst.curves(), points);
		return new Board(points, preFirst.curves());
	}

	@Override
	public Collection<Board> nextGeneration(final Board board) {
		final CurveSet curves = board.curves();
		final CurveSet successors = successors(board);
		final List<Board> nextGeneration = new ArrayList<>(successors.size());
		for(final Curve successor : successors) {
			if(curves.containsNot(successor)) {
				final PointSet points = board.points().copy();
				for(final Curve curve : curves) {
					points.addAll(Algebra.intersect(successor, curve));
				}
				final Board next = new Board(points, curves.adjoin(successor), board).identifyByCurves();
				nextGeneration.add(next);
			}
		}
		return nextGeneration;
	}

	@Override
	int missingDepth(final int pointMisses, final int curveMisses) {
		return Math.max(curveMisses, pointMisses > 0 ? 1 : 0);
	}
	
	CurveSet successors(final Board board) {
		return addAllCurves(board.points(), new CurveSet());
	}
	
}
