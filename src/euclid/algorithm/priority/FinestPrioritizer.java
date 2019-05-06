package euclid.algorithm.priority;

import euclid.algebra.Algebra;
import euclid.geometry.Curve;
import euclid.geometry.Point;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinestPrioritizer extends CoarsePrioritizer {

	@Override
	public int maxPriority() {
		return 3 * super.maxPriority();
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
			nearMisses += nearMisses(point, curves);
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
	
	private int nearCurveMisses(final PointSet points) {
		int nearMisses = 0;
		for(final Curve curve : curves) {
			nearMisses += nearMisses(curve, points);
		}
		return nearMisses;
	}

	private static int nearMisses(final Curve curve, final PointSet points) {
		int hits = 0;
		for(final Point point : points) {
			if(Algebra.contains(point, curve)) {
				hits++;
			}
		}
		return 2 - Math.min(hits, curve.isCircle() ? 1 : 2);
	}

}
