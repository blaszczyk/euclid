package euclid.alg;

import java.util.Collection;
import java.util.Optional;

import euclid.model.*;

public interface Search {

	public Collection<Board> findAll(final Board initial, final Board required, final int depth);

	public Optional<Board> findFirst(final Board initial, final Board required, final int depth);

}
