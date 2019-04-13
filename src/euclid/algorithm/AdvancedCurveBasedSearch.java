package euclid.algorithm;

import java.util.ArrayList;
import java.util.List;

import euclid.algebra.AdvancedAlgebra;
import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;

public class AdvancedCurveBasedSearch extends BasicCurveBasedSearch {
	
	public AdvancedCurveBasedSearch(final Problem problem) {
		super(problem);
	}

	@Override
	CurveSet successors(final Board board) {
		final CurveSet successors = super.successors(board);
		final List<Line> lines = pickLines(board.curves());
		final List<Point> points = board.points().asList();
		
		forEachDistinctPair(points, (p1,p2) -> {
			successors.add(AdvancedAlgebra.bisector(p1,p2));
		});
		forEachDistinctPair(lines, (l1,l2) -> {
			successors.addAll(AdvancedAlgebra.angleBisector(l1, l2));
		});
		for(final Point p : points) {
			for(final Line l : lines) {
				successors.add(AdvancedAlgebra.perpendicular(p, l));
				successors.addNonNull(AdvancedAlgebra.parallel(p, l));
			}
		}
		
		return successors;
	}

	private static List<Line> pickLines(final CurveSet curves) {
		final List<Line> lines = new ArrayList<>(curves.size());
		for(final Curve c : curves) {
			if(c.isLine()) {
				lines.add(c.asLine());
			}
		}
		return lines;
	}

}
