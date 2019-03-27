package euclid.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedIntersectionAlgebra extends Algebra {

	public CachedIntersectionAlgebra(final CurveLifeCycle lifeCycle) {
		super(lifeCycle);
	}

	private final Map<Pair, PointSet> cache = new ConcurrentHashMap<>();

	@Override
	public PointSet intersect(final Curve c1, final Curve c2) {
		final Pair pair = new Pair(c1, c2);
		final PointSet cached = cache.get(pair);
		if(cached != null) {
			return cached;
		}
		final PointSet intersection = super.intersect(c1,c2);
		cache.put(pair, intersection);
		return intersection;
	}
	
	private static class Pair {
		private final Curve c1;
		private final Curve c2;
		
		public Pair(Curve c1, Curve c2) {
			this.c1 = c1;
			this.c2 = c2;
		}

		@Override
		public int hashCode() {
			return c1.hashCode() + c2.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			Pair other = (Pair) obj;
			return ( c1.equals(other.c1) && c2.equals(other.c2) ) 
					|| ( c1.equals(other.c2) && c2.equals(other.c1) );
		}

	}

}
