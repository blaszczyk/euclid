package euclid.algorithm;

import euclid.sets.Board;

public class AlgorithmData {

	private final Board initial;
	private final Board required;
	private final Board assist;
	private final int maxDepth;

	AlgorithmData(final Board initial, final Board required, final Board assist, final int maxDepth) {
		this.initial = initial;
		this.required = required;
		this.assist = assist;
		this.maxDepth = maxDepth;
	}

	public Board initial() {
		return initial;
	}

	public Board required() {
		return required;
	}
	
	public Board assist() {
		return assist;
	}

	public int maxDepth() {
		return maxDepth;
	}

}
