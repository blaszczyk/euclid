package euclid.problem;

import euclid.algorithm.AlgorithmType;
import euclid.algorithm.AlgorithmType.PriorityType;
import euclid.sets.Board;

public class Problem {
	
	private final Board initial;
	private final Board required;
	private final int maxDepth;
	private final boolean depthFirst;
	private final int maxSolutions;
	private final AlgorithmType algorithm;
	private final PriorityType priority;
	
	public Problem(final Board initial, final Board required, final int maxDepth, final boolean depthFirst, 
			final int maxSolutions, final AlgorithmType algorithm, final PriorityType priority) {
		this.initial = initial;
		this.required = required;
		this.maxDepth = maxDepth;
		this.depthFirst = depthFirst;
		this.maxSolutions = maxSolutions;
		this.algorithm = algorithm;
		this.priority = priority;
	}	

	public final Board initial() {
		return initial;
	}

	public final Board required() {
		return required;
	}

	public final int maxDepth() {
		return maxDepth;
	}
	
	public final boolean depthFirst() {
		return depthFirst;
	}

	public final int maxSolutions() {
		return maxSolutions;
	}

	public final AlgorithmType algorithm() {
		return algorithm;
	}
	
	public final PriorityType priority() {
		return priority;
	}
}
