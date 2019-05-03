package euclid.algorithm.priority;

import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class CoarsePrioritizer extends Prioritizer {

	@Override
	public int maxPriority() {
		return data.requiredPoints().size() + data.requiredCurves().size();
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves, final int pointMisses, final int curveMisses) {
		return pointMisses + curveMisses;
	}

}
