package euclid.algorithm;

import java.util.List;

import euclid.algebra.Algebra;
import euclid.geometry.*;
import euclid.sets.*;
import euclid.sets.CurveSet;
import static euclid.algorithm.ListHelper.*;

public class BasicCurveBasedSearch extends CurveBasedSearch<Board> {
	
	@Override
	public Board first() {
		final Board initial = problem.initial();
		final PointSet points = initial.points().copy();
		forEachDistinctPair(initial.curves().asList(), (c1,c2) -> {
			points.addAll(Algebra.intersect(c1, c2));
		});
		return new Board(points, initial.curves());
	}

	@Override
	Board next(final Board parent, final Curve successor) {
		final PointSet points = parent.points().copy();
		for(final Curve curve : parent.curves()) {
			points.addAll(Algebra.intersect(successor, curve));
		}
		return new Board(points, parent.curves().adjoin(successor), parent);
	}

	@Override
	void addSuccessors(final Board board, final CurveSet successors) {
		final List<Point> ps = board.points().asList();
		forEachDistinctPair(ps, (p1,p2) -> constructor.constructFromTwoDistinctPoints(p1, p2, successors));
		if(constructor.isAdvanced()) {
			final List<Line> lines = pickLines(board);
			forEachPair(ps, lines, (p,l) -> constructor.constructFromPointAndLine(p, l, successors));
			forEachDistinctTriple(ps, (p1,p2,p3) -> constructor.constructFromThreeDistinctPoints(p1, p2, p3, successors));
			forEachDistinctPair(lines, (l1,l2) -> constructor.constructFromDistinctLines(l1, l2, successors));
		}
	}

}
