package euclid.alg.engine;

import java.util.Collection;
import java.util.Optional;

public interface SearchEngine<B> {

	public Collection<B> findAll();

	public Optional<B> findFirst();

}
