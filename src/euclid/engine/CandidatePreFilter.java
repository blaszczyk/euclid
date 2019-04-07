package euclid.engine;

import euclid.kpi.KpiReporter;

public interface CandidatePreFilter<B> extends KpiReporter {
	
	public boolean accept(final B b);
}
