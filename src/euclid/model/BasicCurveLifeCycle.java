package euclid.model;

import java.util.concurrent.atomic.AtomicInteger;

import euclid.kpi.KpiCollector;

public class BasicCurveLifeCycle implements CurveLifeCycle {
	
	private final AtomicInteger lineCount = new AtomicInteger();
	
	private final AtomicInteger circleCount = new AtomicInteger();

	@Override
	public Line line(final Point normal, final Constructable offset) {
		lineCount.incrementAndGet();
		return new Line(normal, offset) {
			@Override
			public boolean equals(final Object obj) {
				final Curve other = (Curve) obj;
				return near(other);
			}
		};
	}

	@Override
	public Circle curve(final Point center, final Constructable radiusSquare) {
		circleCount.incrementAndGet();
		return new Circle(center, radiusSquare) {
			@Override
			public boolean equals(final Object obj) {
				final Curve other = (Curve) obj;
				return near(other);
			}
		};
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("total-lines", lineCount.get());
		collector.add("total-circles", circleCount.get());
	}
	
}
