package euclid.algorithm.priority;

import euclid.sets.Board;

public class FinePlusDepth extends FinePrioritizer {

	@Override
	public int maxPriority() {
		return super.maxPriority() + problem.maxDepth();
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return super.priotiry(b, pointMisses, curveMisses) + b.depth();
	}

}
