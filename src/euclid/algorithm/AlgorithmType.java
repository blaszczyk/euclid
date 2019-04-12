package euclid.algorithm;

import euclid.problem.Problem;

public enum AlgorithmType {
	
	CURVE_BASED() {
		public BoardSearch create(final Problem problem) {
			return new CurveBasedSearch(problem);
		};
	},
	POINT_BASED() {
		public BoardSearch create(final Problem problem) {
			return new PointBasedSearch(problem);
		};
	},
	CURVE_ADVANCED() {
		public BoardSearch create(final Problem problem) {
			return new AdvancedCurveBasedSearch(problem);
		};
	},;
	
	abstract public BoardSearch create(final Problem problem);
	
}