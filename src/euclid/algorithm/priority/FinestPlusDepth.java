package euclid.algorithm.priority;

import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public class FinestPlusDepth extends FinestPrioritizer {

	@Override
	public int maxPriority() {
		return super.maxPriority() + data.maxDepth();
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves) {
		return super.priotiry(points, curves) + curves.size() - data.initial().curveCount();
	}

}
