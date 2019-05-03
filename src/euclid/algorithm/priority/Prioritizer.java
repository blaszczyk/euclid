package euclid.algorithm.priority;

import euclid.algorithm.AlgorithmData;
import euclid.sets.CurveSet;
import euclid.sets.PointSet;

public abstract class Prioritizer {

	AlgorithmData data;
	
	public void init(final AlgorithmData data) {
		this.data = data;
	}

	public abstract int priotiry(final PointSet points, final CurveSet curves, final int pointMisses, final int curveMisses);
	
	public abstract int maxPriority();

}
