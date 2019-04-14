package euclid.algorithm;

import euclid.algebra.Algebra;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.problem.Problem;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinestPrioritizer<B extends Board> implements Prioritizer {
	
	private final Problem problem;

	FinestPrioritizer(Problem problem) {
		this.problem = problem;
	}

	@Override
	public int maxPriority() {
		return 3 * (problem.required().points().size() + problem.required().curves().size());
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		final int nearPointMisses = nearPointMisses(b);
		final int nearCurveMisses = nearCurveMisses(b);
		return pointMisses + nearPointMisses + curveMisses + nearCurveMisses;
	}
	
	private int nearPointMisses(final Board b) {
		int nearMisses = 0;
		for(final Point point : problem.required().points()) {
			nearMisses += nearMisses(point, b.curves());
		}
		return nearMisses;
	}
	
	private static int nearMisses(final Point point, final CurveSet curves) {
		int hits = 0;
		for(final Curve curve : curves) {
			if(Algebra.contains(point, curve)) {
				hits++;
			}
		}
		return 2 - Math.min(hits, 2);
	}
	
	private int nearCurveMisses(final Board b) {
		int nearMisses = 0;
		for(final Curve curve : problem.required().curves()) {
			nearMisses += nearMisses(curve, b.points());
		}
		return nearMisses;
	}

	private static int nearMisses(final Curve curve, final PointSet points) {
		int hits = 0;
		boolean hasCenter = false;
		for(final Point point : points) {
			if(Algebra.contains(point, curve)) {
				hits++;
			}
			if(curve.isCircle() && curve.asCircle().center().near(point)) {
				hasCenter = true;
			}
		}
		if(curve.isCircle()) {
			return ( hasCenter ? 0 : 1 ) + ( hits > 0 ? 0 : 1 );
		}
		else {
			return 2 - Math.min(hits, 2);
		}
	}

}
