package euclid.algorithm.priority;

import euclid.algebra.Algebra;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinePrioritizer extends Prioritizer {

	@Override
	public int maxPriority() {
		return 2 * (data.requiredPoints().size() + data.requiredCurves().size());
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves, final int pointMisses, final int curveMisses) {
		final int nearPointMisses = nearPointMisses(curves);
		final int nearCurveMisses = nearCurveMisses(points);
		return pointMisses + nearPointMisses + curveMisses + nearCurveMisses;
	}
	
	private int nearPointMisses(final CurveSet curves) {
		int nearMisses = 0;
		for(final Point point : data.requiredPoints()) {
			if(containsNot(point, curves)) {
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
	
	private int nearCurveMisses(final PointSet points) {
		int nearMisses = 0;
		for(final Curve curve : data.requiredCurves()) {
			if(containsNot(curve, points)) {
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
