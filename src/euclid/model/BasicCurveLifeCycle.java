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
			public int hashCode() {
				return 13 * normal.hashCode() + 37 * offset.hashCode();
			}
			@Override
			public boolean equals(final Object obj) {
				final Curve other = (Curve) obj;
				return isEqual(other);
			}
		};
	}

	@Override
	public Circle curve(final Point center, final Constructable radiusSquare) {
		circleCount.incrementAndGet();
		return new Circle(center, radiusSquare) {
			@Override
			public int hashCode() {
				return 196 * center.hashCode() + 883 * radiusSquare.hashCode();
			}
			@Override
			public boolean equals(final Object obj) {
				final Curve other = (Curve) obj;
				return isEqual(other);
			}
		};
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		collector.add("total-lines", lineCount.get());
		collector.add("total-circles", circleCount.get());
	}
	
}
