package euclid.model;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;

public class CachedCurveLifeCycle implements CurveLifeCycle {

	private final Collection<Line> lines = ConcurrentHashMap.newKeySet();
	
	private final Collection<Circle> circles = ConcurrentHashMap.newKeySet();
	
	private final AtomicInteger dupeLines = new AtomicInteger();
	
	private final AtomicInteger dupeCircles = new AtomicInteger();


	@Override
	public Line line(final Point normal, final Constructable offset) {
		return cached(new Line(normal, offset), lines, dupeLines);
	}

	@Override
	public Circle curve(final Point center, final Constructable radiusSquare) {
		return cached(new Circle(center, radiusSquare), circles, dupeCircles);
	}

	private static <E extends Curve> E cached(final E newE, final Collection<E> es, final AtomicInteger dupeCounter) {
		for(final E e : es) {
			if(e.isEqual(newE)) {
				dupeCounter.incrementAndGet();
				return e;
			}
		}
		es.add(newE);
		return newE;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("cached-lines", lines.size());
		collector.add("dupe-lines", dupeLines.get());
		collector.add("cached-circles", circles.size());
		collector.add("dupe-circles", dupeCircles.get());
	}
	
}
