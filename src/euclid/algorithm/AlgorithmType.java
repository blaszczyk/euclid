package euclid.algorithm;

import java.util.Comparator;
import java.util.function.Supplier;

import euclid.algorithm.constructor.*;
import euclid.algorithm.priority.*;
import euclid.geometry.Curve;
import euclid.problem.Problem;
import euclid.sets.Board;

public enum AlgorithmType {
	CURVE_BASED(BasicCurveBasedSearch::new),
	CURVE_DEDUPE(CurveBasedDedupingSearch::new);
	
	public enum ConstructorType {
		BASIC(BasicConstructor::new),
		ADVANCED(AdvancedConstructor::new),
		STRAIGHTEDGE(StraightEdgeOnly::new),
		COMPASS(CompassOnly::new);
		
		final Supplier<Constructor> creator;
		private ConstructorType(final Supplier<Constructor> creator) {
			this.creator = creator;
		}
		public Constructor create() {
			return creator.get();
		}
	}

	public enum PriorityType {
		NO(NoPrioritizer::new),
		COARSE(CoarsePrioritizer::new),
		FINE(FinePrioritizer::new),
		FINEST(FinestPrioritizer::new),
		FINE_PLUS_DEPTH(FinePlusDepth::new),
		FINEST_PLUS_DEPTH(FinestPlusDepth::new);

		final Supplier<Prioritizer> creator;
		private PriorityType(final Supplier<Prioritizer> creator) {
			this.creator = creator;
		}

	};
	
	public enum CurveIdentification {
		LINE_TYPES_DISTINCT((c1,c2) -> c1.compareTo(c2)),
		LINE_TYPES_EQUAL((c1,c2) -> {
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

	private final Supplier<CurveBasedSearch<? extends Board>> creator;

	private AlgorithmType(final Supplier<CurveBasedSearch<? extends Board>> creator) {
		this.creator = creator;
	}

	public Algorithm<? extends Board> create(final Problem problem) {
		final Constructor constructor = problem.constructor().create();
		
		final Prioritizer prioritizer = problem.priority().creator.get();
		prioritizer.init(problem);
		
		final Comparator<Curve> curveComparator = problem.curveIdentification().curveComparator;
		
		final CurveBasedSearch<? extends Board> algorithm = creator.get();
		algorithm.init(problem, constructor, prioritizer, curveComparator);
		return algorithm;
	}
	
}
