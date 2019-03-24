package euclid.alg;

import euclid.model.*;

abstract class BoardSearch<T> extends ThreadedSearch<T, Board> {
	
	final Board initial;
	final Board required;
	
	BoardSearch(final Board initial, final Board required, final int maxDepth) {
		super(maxDepth);
		this.initial = initial;
		this.required = required;
	}
	
	@Override
	boolean solves(final Board board) {
		return board.curves().containsAll(required.curves())
				&& board.points().containsAll(required.points());
	}

}
