package euclid.model;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;

public class CachedCurveLifeCycle implements CurveLifeCycle {

	private final Collection<Line> lines = ConcurrentHashMap.newKeySet();

	private final Collection<Segment> segments = ConcurrentHashMap.newKeySet();
	
	private final Collection<Circle> circles = ConcurrentHashMap.newKeySet();
	
	private final AtomicInteger dupeLines = new AtomicInteger();
	
	private final AtomicInteger dupeSegments = new AtomicInteger();
	
	private final AtomicInteger dupeCircles = new AtomicInteger();


	@Override
	public Line line(final Point normal, final Constructable offset) {
		final Line line = new Line(normal, offset) {
			@Override
			public int hashCode() {
				return System.identityHashCode(this);
			}
			
			@Override
			public boolean equals(final Object obj) {
				return this == obj;
			}
		};
		return cached(line, lines, dupeLines);
	}

	@Override
	public Circle curve(final Point center, final Constructable radiusSquare) {
		final Circle circle = new Circle(center, radiusSquare) {
			@Override
			public int hashCode() {
				return System.identityHashCode(this);
			}
			
			@Override
			public boolean equals(final Object obj) {
				return this == obj;
			}
		};
		return cached(circle, circles, dupeCircles);
	}

	@Override
	public Segment segment(final Point normal, final Constructable offset, final Constructable end1, final Constructable end2) {
		final Segment segment = new Segment(normal, offset, end1, end2) {
			@Override
			public int hashCode() {
				return System.identityHashCode(this);
			}
			
			@Override
			public boolean equals(final Object obj) {
				return this == obj;
			}
		};
		return cached(segment, segments, dupeSegments);
	}
	
	private static <C extends Curve> C cached(final C newCurve, final Collection<C> cache, final AtomicInteger dupeCounter) {
		
		for(final C curve : cache) {
			if(curve.near(newCurve)) {
				dupeCounter.incrementAndGet();
				return curve;
			}
		}
		cache.add(newCurve);
		return newCurve;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("cached-lines", lines.size());
		collector.add("dupe-lines", dupeLines.get());
		collector.add("cached-circles", circles.size());
		collector.add("dupe-circles", dupeCircles.get());
		collector.add("cached-segments", segments.size());
		collector.add("dupe-segments", dupeSegments.get());
	}
	
}
