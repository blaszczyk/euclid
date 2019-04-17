package euclid.algorithm;

import java.util.Comparator;
import java.util.function.Supplier;

import euclid.geometry.Curve;
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

		final Supplier<Prioritizer> constructor;
		private PriorityType(final Supplier<Prioritizer> constructor) {
			this.constructor = constructor;
		}

	};
	
	public enum CurveIdentification {
		SEGMENT_IS_NOT_LINE((c1,c2) -> c1.compareTo(c2)),
		SEGMENT_IS_LINE((c1,c2) -> {
			if(c1.isLine() && c2.isLine()) {
				return c1.asLine().compareAsLine(c2.asLine());
			}
			return c1.compareTo(c2);
		});

		private final Comparator<Curve> curveComparator;

		private CurveIdentification(Comparator<Curve> curveComparator) {
			this.curveComparator = curveComparator;
		}

	};

	private final Supplier<BoardSearch<? extends Board>> constructor;

	private AlgorithmType(final Supplier<BoardSearch<? extends Board>> constructor) {
		this.constructor = constructor;
	}

	public Algorithm<? extends Board> create(final Problem problem) {
		final Prioritizer prioritizer = problem.priority().constructor.get();
		prioritizer.init(problem);
		
		final Comparator<Curve> curveComparator = problem.curveIdentification().curveComparator;
		
		final BoardSearch<? extends Board> algorithm = constructor.get();
		algorithm.init(problem, prioritizer, curveComparator);
		return algorithm;
	}
	
}
