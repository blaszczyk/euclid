package euclid.algorithm.priority;

import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class NoPrioritizer extends Prioritizer {
	@Override
	public int maxPriority() {
		return 1;
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves, final int pointMisses, final int curveMisses) {
		return 1;
	}
}
