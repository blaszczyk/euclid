package euclid.problem;

import euclid.alg.*;
import euclid.model.Board;

public class Problem {
	
	public enum AlgorithmType {
		CURVE_BASED,
		POINT_BASED;
	}
	
	private final Board initial;
	private final Board required;
	private final int maxDepth;
	private final boolean findAll;
	private final AlgorithmType algorithmType;
	
	public Problem(final Board initial, final Board required, final int maxDepth, final boolean findAll, final AlgorithmType algorithmType) {
		this.initial = initial;
		this.required = required;
		this.maxDepth = maxDepth;
		this.findAll = findAll;
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

	public final boolean findAll() {
		return findAll;
	}

	public final AlgorithmType algorithmType() {
		return algorithmType;
	}

	public Algorithm<?, Board> createAlgorithm() {
		switch (algorithmType) {
		case CURVE_BASED:
			return new CurveBasedSearch(initial, required);
		case POINT_BASED:
			return new PointBasedSearch(initial, required);
		}
		return null;
	}
	
}
