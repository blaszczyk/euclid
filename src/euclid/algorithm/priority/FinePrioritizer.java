package euclid.algorithm.priority;

import euclid.algebra.Algebra;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinePrioritizer extends CoarsePrioritizer {

	@Override
	public int maxPriority() {
		return 2 * super.maxPriority();
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves) {
		final int nearPointMisses = nearPointMisses(curves);
		final int nearCurveMisses = nearCurveMisses(points);
		return super.priotiry(points, curves) + nearPointMisses + nearCurveMisses;
	}
	
	private int nearPointMisses(final CurveSet curves) {
		int nearMisses = 0;
		for(final Point point : points) {
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
		for(final Curve curve : curves) {
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
		}
		return true;
	}

}
