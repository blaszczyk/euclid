package euclid.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class IntersectionCache implements Curve {

	private final Map<Curve, PointSet> cache = new ConcurrentHashMap<>();
	
	@Override
	public PointSet intersect(final Curve other) {
		if(cache.containsKey(other)) {
			return cache.get(other);
		}
		final PointSet intersection = doIntersect(other);
		cache.put(other, intersection);
		((IntersectionCache)other).cache.put(this, intersection);
		return intersection;
	}
	
	abstract PointSet doIntersect(final Curve other);
	
}
