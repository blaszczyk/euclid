package euclid.algorithm;

import static euclid.algorithm.ListHelper.forEachDistinctPair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import euclid.algebra.Algebra;
import euclid.algorithm.constructor.Constructor;
import euclid.algorithm.priority.Prioritizer;
import euclid.geometry.Curve;
import euclid.geometry.Line;
import euclid.geometry.Point;
import euclid.problem.Problem;
import euclid.sets.Board;
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
		final PointSet initialPoints = problem.initial().points();
		final PointSet requiredPoints = problem.required().points();
		final List<Curve> requiredCurves = problem.required().curveList();

		forEachDistinctPair(problem.initial().curveList(), (c1,c2) -> {
			initialPoints.addAll(Algebra.intersect(c1, c2));
		});
		for(final Line line : problem.initial().lineList()) {
			if(line.isRay()) {
				initialPoints.add(Algebra.endPoint(line.asRay()));
			}
			else if(line.isSegment()) {
				initialPoints.addAll(Algebra.endPoints(line.asSegment()));
			}
		}

		for(final Curve curve : problem.required().curveList()) {
			if(curve.isCircle()) {
				final Point center = curve.asCircle().center();
				if(initialPoints.containsNot(center) && !requiredPoints.contains(center)) {
					requiredPoints.add(center);
				}
			}
			else {
				final Line line = curve.asLine();
				if(line.hasEnds()) {
					if(line.isRay()) {
						requiredPoints.add(Algebra.endPoint(line.asRay()));
					}
					else if(line.isSegment()) {
						requiredPoints.addAll(Algebra.endPoints(line.asSegment()));
					}
					final Line trueLine = new Line(line.normal(), line.offset());
					requiredCurves.replaceAll(c -> c.near(line) ? trueLine : c);
				}
			}
		}

		final Board initial = new RootBoard(initialPoints, problem.initial().curves());
		return new AlgorithmData(initial, new ArrayList<>(requiredPoints), requiredCurves, problem.maxDepth());
	}

}
