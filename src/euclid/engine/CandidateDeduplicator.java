package euclid.engine;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;

public class CandidateDeduplicator<B> implements CandidatePreFilter<B> {

	private final Collection<B> cache = ConcurrentHashMap.newKeySet();

	private final AtomicInteger dupeCount = new AtomicInteger();

	
	@Override
	public boolean accept(final B b) {
		final boolean isNew = cache.add(b);
		if(!isNew) {
			dupeCount.incrementAndGet();
		}
		return isNew;
	}
	
	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("total", cache.size());
		collector.add("dupes", dupeCount.get());
	}

}
