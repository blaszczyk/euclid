package euclid.algorithm.priority;

import euclid.sets.Board;

public class CoarsePrioritizer extends Prioritizer {

	@Override
	public int maxPriority() {
		return problem.required().points().size() + problem.required().curves().size();
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return pointMisses + curveMisses;
	}

}
