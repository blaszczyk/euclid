package euclid.algorithm;

import static euclid.algorithm.ListHelper.*;

import java.util.ArrayList;
import java.util.Comparator;

import euclid.algebra.Algebra;
import euclid.algorithm.constructor.Constructor;
import euclid.algorithm.priority.Prioritizer;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;
import euclid.sets.RootBoard;

public class AlgorithmFactory {

	public static Algorithm<Board> create(final Problem problem) {
		final AlgorithmData data = createAlgorithmData(problem);
		final Constructor constructor = problem.constructor().create();

		final Prioritizer prioritizer = problem.priority().create();
		prioritizer.init(data);
		
		final Comparator<Curve> curveComparator = problem.curveIdentification().curveComparator();
		
		final CurveBasedSearch algorithm = problem.algorithm().create();
		algorithm.init(data, constructor, prioritizer, curveComparator);
		return algorithm;
	}

	private static AlgorithmData createAlgorithmData(final Problem problem) {
		final Board initial = enhance(problem.initial());
		final Board required = transform(problem.required(), initial);
		final Board assist = transform(problem.assist(), initial, required);
		return new AlgorithmData(initial, required, assist, problem.maxDepth());
	}

	private static RootBoard enhance(final Board board) {
		final PointSet points = board.points();
		forEachDistinctPair(board.curveList(), (c1,c2) -> {
			points.addAll(Algebra.intersect(c1, c2));
		});
		for(final Line line : board.lineList()) {
			if(line.isRay()) {
				points.add(Algebra.endPoint(line.asRay()));
			}
			else if(line.isSegment()) {
				points.addAll(Algebra.endPoints(line.asSegment()));
			}
		}
		return new RootBoard(points, board.curves());
	}
	
	private static Board transform(final Board board, final Board ... excludors) {
		final CurveSet curves = board.curves();
		final PointSet points = board.points();
		for(final Curve curve : new ArrayList<>(curves)) {
			if(curve.isCircle()) {
				points.add(curve.asCircle().center());
			}
			else {
				final Line line = curve.asLine();
				if(line.hasEnds()) {
					if(line.isRay()) {
						points.add(Algebra.endPoint(line.asRay()));
					}
					else if(line.isSegment()) {
						points.addAll(Algebra.endPoints(line.asSegment()));
					}
					curves.remove(line);
					curves.add(new Line(line.normal(), line.offset()));
				}
			}
		}
		for(final Board excludor : excludors) {
			points.removeAll(excludor.pointList());
			curves.removeAll(excludor.curveList());
		}
		return new RootBoard(points, curves);
	}

}
