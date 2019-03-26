package euclid.alg;

import java.util.Collection;

import euclid.model.*;

abstract class BoardSearch implements Algorithm <Board> {
	
	final Board initial;
	final Collection<Board> required;
	private final int maxMisses;
	
	BoardSearch(final Board initial, final Collection<Board> required) {
		this.initial = initial;
		this.required = required;
		maxMisses = required.stream()
				.mapToInt(b -> b.points().size() + b.curves().size())
				.min()
				.orElse(0);
	}

	@Override
	public Board first() {
		return initial;
	}
	
	@Override
	public int misses(final Board candidate) {
		int misses = maxMisses;
		for(final Board possibility : required) {
			final long pointMisses = possibility.points().stream()
					.filter(candidate.points()::containsNot)
					.count();
			final long curveMisses = possibility.curves().stream()
					.filter(candidate.curves()::containsNot)
					.count();
			misses = Math.min(misses, (int)(pointMisses + curveMisses));
		}
		return misses;
	}
	
	@Override
	public int maxMisses() {
		return maxMisses;
	}

}
