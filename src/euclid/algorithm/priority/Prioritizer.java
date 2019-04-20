package euclid.algorithm.priority;

import euclid.problem.Problem;
import euclid.sets.Board;

public abstract class Prioritizer {

	Problem problem;
	
	public void init(final Problem problem) {
		this.problem = problem;
	}
	
	public abstract int priotiry(final Board b, final int pointMisses, final int curveMisses);
	
	public abstract int maxPriority();

}
