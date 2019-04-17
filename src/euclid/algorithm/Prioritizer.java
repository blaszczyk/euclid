package euclid.algorithm;

import euclid.problem.Problem;
import euclid.sets.Board;

abstract class Prioritizer {

	Problem problem;
	
	void init(final Problem problem) {
		this.problem = problem;
	}
	
	abstract int priotiry(final Board b, final int pointMisses, final int curveMisses);
	
	abstract int maxPriority();

}
