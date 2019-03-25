package euclid.alg.engine;

import java.util.Collection;
import java.util.Optional;

import euclid.kpi.KpiReporter;

public interface SearchEngine<B> extends KpiReporter {

	public Collection<B> findAll();

	public Optional<B> findFirst();

}
