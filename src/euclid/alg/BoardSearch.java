package euclid.alg;

import java.util.Collection;

import euclid.model.*;

abstract class BoardSearch<T> implements Algorithm <T, Board> {
	
	final Board initial;
	final Collection<Board> required;
	
	BoardSearch(final Board initial, final Collection<Board> required) {
		this.initial = initial;
		this.required = required;
	}
	
	@Override
	public boolean solves(final Board candidate) {
		for(final Board possibility : required) {
			final boolean found = candidate.curves().contains(possibility.curves())
					&& candidate.points().contains(possibility.points());
			if(found) {
				return true;
			}
		}
		return false;
	}

}
