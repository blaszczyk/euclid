package euclid.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class CachedIntersectionAlgebra extends Algebra implements KpiReporter {

	public CachedIntersectionAlgebra(final CurveLifeCycle lifeCycle) {
		super(lifeCycle);
	}

	private final Map<Pair, PointSet> cache = new ConcurrentHashMap<>();
	
	private final AtomicInteger dupeCount = new AtomicInteger();

	@Override
	public PointSet intersect(final Curve c1, final Curve c2) {
		final Pair pair = new Pair(c1, c2);
		final PointSet cached = cache.get(pair);
		if(cached != null) {
			dupeCount.incrementAndGet();
			return cached;
		}
		final PointSet intersection = super.intersect(c1,c2);
		cache.put(pair, intersection);
		return intersection;
	}
	
	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("cached-intersections", cache.size());
		collector.add("dupe-intersections", dupeCount.get());
	}
	
	private static class Pair {
		private final Curve c1;
		private final Curve c2;
		
		public Pair(final Curve c1, final Curve c2) {
			this.c1 = c1;
			this.c2 = c2;
		}

		@Override
		public int hashCode() {
			return c1.hashCode() + c2.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			final Pair other = (Pair) obj;
			return ( c1.equals(other.c1) && c2.equals(other.c2) ) 
					|| ( c1.equals(other.c2) && c2.equals(other.c1) );
		}
	}

}
