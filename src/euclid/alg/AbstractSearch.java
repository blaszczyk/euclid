package euclid.alg;

import java.util.Collection;
import java.util.Optional;

import euclid.model.*;

abstract class AbstractSearch implements Search {

	@Override
	public Collection<Board> findAll(final Board initial, final Board required, final int depth) {
		return find(initial, required, depth, false);
	}

	@Override
	public Optional<Board> findFirst(final Board initial, final Board required, final int depth) {
		final Collection<Board> sol = find(initial, required, depth, true);
		return sol.isEmpty() ? Optional.empty() : Optional.of(sol.iterator().next());
	}
	
	abstract Collection<Board> find(final Board initial, final Board required, final int depth, final boolean first);

}
