package euclid.problem;

import euclid.model.Board;

public class Problem {
	
	public enum AlgorithmType {
		CURVE_BASED,
		POINT_BASED;
	}
	
	private final Board initial;
	private final Board required;
	private final int maxDepth;
	private final boolean depthFirst;
	private final int maxSolutions;
	private final AlgorithmType algorithmType;
	
	public Problem(final Board initial, final Board required, final int maxDepth, final boolean depthFirst, final int maxSolutions, final AlgorithmType algorithmType) {
		this.initial = initial;
		this.required = required;
		this.maxDepth = maxDepth;
		this.depthFirst = depthFirst;
		this.maxSolutions = maxSolutions;
		this.algorithmType = algorithmType;
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

	public final AlgorithmType algorithmType() {
		return algorithmType;
	}
	
}
