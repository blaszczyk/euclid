package euclid.algorithm.priority;

import euclid.algebra.Algebra;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.Board;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinePrioritizer extends Prioritizer {

	@Override
	public int maxPriority() {
		return 2 * (problem.required().points().size() + problem.required().curves().size());
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
			if(containsNot(point, b.curves())) {
				nearMisses++;
			}
		}
		return nearMisses;
	}
	
	private static boolean containsNot(final Point point, final CurveSet curves) {
		for(final Curve curve : curves) {
			if(Algebra.contains(point, curve)) {
				return false;
			}
		}
		return true;
	}
	
	private int nearCurveMisses(final Board b) {
		int nearMisses = 0;
		for(final Curve curve : problem.required().curves()) {
			if(containsNot(curve, b.points())) {
				nearMisses++;
			}
		}
		return nearMisses;
	}

	private static boolean containsNot(final Curve curve, final PointSet points) {
		for(final Point point : points) {
			if(Algebra.contains(point, curve)) {
				return false;
			}
			if(curve.isCircle() && curve.asCircle().center().near(point)) {
				return false;
			}
		}
		return true;
	}

}
