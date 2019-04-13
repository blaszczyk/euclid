package euclid.algorithm;

import euclid.problem.Problem;
import euclid.sets.Board;

public enum AlgorithmType {

	CURVE_BASED(BasicCurveBasedSearch::new),
	POINT_BASED(PointBasedSearch::new),
	CURVE_ADVANCED(AdvancedCurveBasedSearch::new),
	CURVE_DEDUPE(CurveBasedDedupingSearch::new);

	@FunctionalInterface
	interface AlgorithmCreator {
		public Algorithm<? extends Board> create(final Problem problem);
	}

	private final AlgorithmCreator creator;

	private AlgorithmType(final AlgorithmCreator creator) {
		this.creator = creator;
	}

	public Algorithm<? extends Board> create(final Problem problem) {
		return creator.create(problem);
	}
	
}
