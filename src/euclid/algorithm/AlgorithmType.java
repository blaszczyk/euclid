package euclid.algorithm;

import java.util.Comparator;
import java.util.function.Supplier;

import euclid.algorithm.constructor.*;
import euclid.algorithm.priority.*;
import euclid.geometry.Curve;

public enum AlgorithmType {
	CURVE_BASED(BasicCurveBasedSearch::new),
	CURVE_DEDUPE(CurveBasedDedupingSearch::new);

	private final Supplier<CurveBasedSearch> creator;
	private AlgorithmType(final Supplier<CurveBasedSearch> creator) {
		this.creator = creator;
	}
	public CurveBasedSearch create() {
		return creator.get();
	}
	
	public enum ConstructorType {
		BASIC(BasicConstructor::new),
		ADVANCED(AdvancedConstructor::new),
		STRAIGHTEDGE(StraightEdgeOnly::new),
		COMPASS(CompassOnly::new);
		
		private final Supplier<Constructor> creator;
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

		private final Supplier<Prioritizer> creator;
		private PriorityType(final Supplier<Prioritizer> creator) {
			this.creator = creator;
		}
		public Prioritizer create() {
			return creator.get();
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
		public Comparator<Curve> curveComparator() {
			return curveComparator;
		}

	};
	
}
