package euclid.algorithm;

import euclid.model.AdvancedAlgebra;
import euclid.problem.Problem;

public enum AlgorithmType {
	
	CURVE_BASED() {
		public BoardSearch create(final Problem problem, final AdvancedAlgebra algebra) {
			return new CurveBasedSearch(problem, algebra);
		};
	},
	POINT_BASED() {
		public BoardSearch create(final Problem problem, final AdvancedAlgebra algebra) {
			return new PointBasedSearch(problem, algebra);
		};
	},
	CURVE_ADVANCED() {
		public BoardSearch create(final Problem problem, final AdvancedAlgebra algebra) {
			return new AdvancedCurveBasedSearch(problem, algebra);
		};
	},;
	
	abstract public BoardSearch create(final Problem problem, final AdvancedAlgebra algebra);
	
}