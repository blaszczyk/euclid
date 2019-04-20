package euclid.algorithm.priority;

import euclid.sets.Board;

public class FinestPlusDepth extends FinestPrioritizer {

	@Override
	public int maxPriority() {
		return super.maxPriority() + problem.maxDepth();
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return super.priotiry(b, pointMisses, curveMisses) + b.depth();
	}

}
