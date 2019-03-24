package euclid.alg;

import java.util.Collection;
import java.util.Optional;

public interface Search<B> {

	public Collection<B> findAll();

	public Optional<B> findFirst();

}
