package euclid.engine;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class Deduplicator<B> implements KpiReporter {

	private final AtomicInteger count = new AtomicInteger();

	private final Collection<B> cache = ConcurrentHashMap.newKeySet();

	private final AtomicInteger dupeCount = new AtomicInteger();
	
	private final int maxDedupeDepth;
	
	Deduplicator(final int maxDedupeDepth) {
		this.maxDedupeDepth = maxDedupeDepth;
	}

	public boolean checkDupe(final B b, final int depth) {
		final boolean isNew;
		if(depth < maxDedupeDepth) {
			isNew = cache.add(b);
			if(!isNew) {
				dupeCount.incrementAndGet();
			}
		}
		else {
			isNew = true;
		}
		if(isNew) {
			count.incrementAndGet();
		}
		return isNew;
	}
	
	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("total", count.get());
		collector.add("cached", cache.size());
		collector.add("dupes", dupeCount.get());
	}

}
