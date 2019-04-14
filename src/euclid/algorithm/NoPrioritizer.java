package euclid.algorithm;

import euclid.problem.Problem;
import euclid.sets.Board;

public class NoPrioritizer<B extends Board> implements Prioritizer {

	NoPrioritizer(Problem problem) {
	}

	@Override
	public int maxPriority() {
		return 1;
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return Math.min(pointMisses + curveMisses, 1);
	}
}
