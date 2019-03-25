package euclid.alg;

import java.util.Collection;
import java.util.Optional;

import euclid.kpi.KpiReporter;

public interface Search<B> extends KpiReporter {

	public Collection<B> findAll();

	public Optional<B> findFirst();

}
