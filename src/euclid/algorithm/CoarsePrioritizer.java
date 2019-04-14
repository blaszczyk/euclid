package euclid.algorithm;

import euclid.problem.Problem;
import euclid.sets.Board;

public class CoarsePrioritizer implements Prioritizer {
	
	private final Problem problem;

	CoarsePrioritizer(Problem problem) {
		this.problem = problem;
	}

	@Override
	public int maxPriority() {
		return problem.required().points().size() + problem.required().curves().size();
	}

	@Override
	public int priotiry(final Board b, final int pointMisses, final int curveMisses) {
		return pointMisses + curveMisses;
	}

}
