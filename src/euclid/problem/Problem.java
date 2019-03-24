package euclid.problem;

import euclid.model.Board;

public class Problem {
	
	public enum Algorithm {
		CURVE_BASED,
		POINT_BASED;
	}
	
	private final Board initial;
	private final Board required;
	private final int maxDepth;
	private final boolean findAll;
	private final Algorithm algorithm;
	
	public Problem(final Board initial, final Board required, final int maxDepth, final boolean findAll, final Algorithm algorithm) {
		this.initial = initial;
		this.required = required;
		this.maxDepth = maxDepth;
		this.findAll = findAll;
		this.algorithm = algorithm;
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

	public final boolean findAll() {
		return findAll;
	}

	public final Algorithm algorithm() {
		return algorithm;
	}
	
}
