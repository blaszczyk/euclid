package euclid.alg;

import euclid.model.*;

abstract class BoardSearch<T> extends ThreadedSearch<T, Board> {
	
	final Board initial;
	final Board required;
	final int depth;
	
	public BoardSearch(final Board initial, final Board required, final int depth) {
		this.initial = initial;
		this.required = required;
		this.depth = depth;
	}
	
	@Override
	boolean solves(final Board board) {
		return board.curves().containsAll(required.curves()) && board.points().containsAll(required.points());
	}

}
