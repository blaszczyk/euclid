package euclid.problem;

import euclid.algorithm.AlgorithmType;
import euclid.algorithm.AlgorithmType.ConstructorType;
import euclid.algorithm.AlgorithmType.CurveIdentification;
import euclid.algorithm.AlgorithmType.PriorityType;
import euclid.sets.Board;

public class Problem {
	
	private final Board initial;
	private final Board required;
	private final Board assist;
	private final int maxDepth;
	private final boolean depthFirst;
	private final boolean shuffle;
	private final int maxSolutions;
	private final AlgorithmType algorithm;
	private final ConstructorType constructor;
	private final PriorityType priority;
	private final CurveIdentification curveIdentification;
	
	public Problem(final Board initial, final Board required, final Board assist, final int maxDepth, final boolean depthFirst, 
			final boolean shuffle, final int maxSolutions, final AlgorithmType algorithm, final ConstructorType constructor, 
			final PriorityType priority, final CurveIdentification curveIdentification) {
		this.initial = initial;
		this.required = required;
		this.assist = assist;
		this.maxDepth = maxDepth;
		this.depthFirst = depthFirst;
		this.shuffle = shuffle;
		this.maxSolutions = maxSolutions;
		this.algorithm = algorithm;
		this.constructor = constructor;
		this.priority = priority;
		this.curveIdentification = curveIdentification;
	}	

	public final Board initial() {
		return initial;
	}

	public final Board required() {
		return required;
	}

	public final Board assist() {
		return assist;
	}

	public final int maxDepth() {
		return maxDepth;
	}
	
	public final boolean depthFirst() {
		return depthFirst;
	}
	
	public final boolean shuffle() {
		return shuffle;
	}

	public final int maxSolutions() {
		return maxSolutions;
	}

	public final AlgorithmType algorithm() {
		return algorithm;
	}

	public ConstructorType constructor() {
		return constructor;
	}
	
	public final PriorityType priority() {
		return priority;
	}

	public final CurveIdentification curveIdentification() {
		return curveIdentification;
	}
}
