package euclid.algorithm;

import euclid.geometry.*;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class BasicCurveBasedSearch extends CurveBasedSearch<Board> {
	
	public BasicCurveBasedSearch(final Problem problem, final Prioritizer prioritizer) {
		super(problem, prioritizer);
	}
	
	@Override
	public Board decorateFirst(final Board first) {
		final PointSet points = first.points().copy();
		addAllIntersections(first.curves(), points);
		return new Board(points, first.curves());
	}

	@Override
	CurveSet successors(final Board board) {
		return addAllCurves(board.points(), new CurveSet());
	}

	@Override
	Board next(final Board parent, final Curve successor) {
		final PointSet points = allIntersections(parent, successor);
		return new Board(points, parent.curves().adjoin(successor), parent) {
			@Override
			public int hashCode() {
				return curves().hashCode();
			}

			@Override
			public boolean equals(final Object obj) {
				final Board other = (Board) obj;
				return curves().equals(other.curves());
			}
		};
	}

}
