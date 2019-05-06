package euclid.algorithm.priority;

import euclid.sets.CurveSet;
import euclid.sets.PointSet;

import static euclid.algorithm.ListHelper.*;

public class CoarsePrioritizer extends Prioritizer {

	@Override
	public int maxPriority() {
		return points.size() + curves.size();
	}

	@Override
	public int priotiry(final PointSet points, final CurveSet curves) {
		return misses(points, this.points) + misses(curves,  this.curves);
	}

}
