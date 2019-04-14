package euclid.algorithm;

import euclid.problem.Problem;
import euclid.sets.Board;

public enum AlgorithmType {

	CURVE_BASED(BasicCurveBasedSearch::new),
	POINT_BASED(PointBasedSearch::new),
	CURVE_ADVANCED(AdvancedCurveBasedSearch::new),
	CURVE_DEDUPE(CurveBasedDedupingSearch::new);

	public enum PriorityType {
		NO(NoPrioritizer::new),
		COARSE(CoarsePrioritizer::new),
		FINE(FinePrioritizer::new),
		FINEST(FinestPrioritizer::new);

		@FunctionalInterface
		interface PriorityCreator {
			public Prioritizer create(final Problem problem);
		}

		final PriorityCreator creator;

		private PriorityType(final PriorityCreator creator) {
			this.creator = creator;
		}

	}

	@FunctionalInterface
	interface AlgorithmCreator {
		public Algorithm<? extends Board> create(final Problem problem, final Prioritizer priorizer);
	}

	private final AlgorithmCreator creator;

	private AlgorithmType(final AlgorithmCreator creator) {
		this.creator = creator;
	}

	public Algorithm<? extends Board> create(final Problem problem) {
		final Prioritizer prioritizer = problem.priority().creator.create(problem);
		return creator.create(problem, prioritizer);
	}
	
}
