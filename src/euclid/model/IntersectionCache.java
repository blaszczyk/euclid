package euclid.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class IntersectionCache implements Curve {

	private final Map<Curve, PointSet> cache = new ConcurrentHashMap<>();
	
	@Override
	public PointSet intersect(final Curve other) {
		final PointSet cached = cache.get(other);
		if(cached != null) {
			return cached;
		}
		final PointSet intersection = doIntersect(other);
		cache.put(other, intersection);
		((IntersectionCache)other).cache.put(this, intersection);
		return intersection;
	}
	
	abstract PointSet doIntersect(final Curve other);
	
}
