package euclid.algorithm;

import euclid.sets.Board;

class CoarsePrioritizer extends Prioritizer {

	@Override
	int maxPriority() {
		return problem.required().points().size() + problem.required().curves().size();
	}

	@Override
	int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return pointMisses + curveMisses;
	}

}
