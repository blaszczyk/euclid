package euclid.engine;

import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;

public class CandidateCounter<B> implements CandidatePreFilter<B> {

	private final AtomicInteger count = new AtomicInteger();

	
	@Override
	public boolean accept(final B b) {
		count.incrementAndGet();
		return true;
	}
	
	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("total", count.get());
	}

}
