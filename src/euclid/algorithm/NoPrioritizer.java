package euclid.algorithm;

import euclid.sets.Board;

class NoPrioritizer extends Prioritizer {
	@Override
	int maxPriority() {
		return 1;
	}

	@Override
	int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return Math.min(pointMisses + curveMisses, 1);
	}
}
