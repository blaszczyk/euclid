package euclid.alg;

import euclid.model.*;

abstract class BoardSearch<T> implements Algorithm <T, Board> {
	
	final Board initial;
	final Board required;
	
	BoardSearch(final Board initial, final Board required) {
		this.initial = initial;
		this.required = required;
	}
	
	@Override
	public boolean solves(final Board board) {
		return board.curves().contains(required.curves())
				&& board.points().contains(required.points());
	}

}
