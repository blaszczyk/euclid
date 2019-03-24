package euclid.problem;

import euclid.alg.*;
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

	public Search<Board> createSearch() {
		switch (algorithm) {
		case CURVE_BASED:
			return new CurveBasedSearch(initial, required, maxDepth);
		case POINT_BASED:
			return new PointBasedSearch(initial, required, maxDepth);
		}
		return null;
	}
	
}
