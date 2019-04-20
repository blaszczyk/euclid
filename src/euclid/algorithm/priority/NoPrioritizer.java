package euclid.algorithm.priority;

import euclid.sets.Board;

public class NoPrioritizer extends Prioritizer {
	@Override
	public int maxPriority() {
		return 1;
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return 1;
	}
}
