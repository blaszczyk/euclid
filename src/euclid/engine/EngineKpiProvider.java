package euclid.engine;

import java.util.concurrent.atomic.AtomicLong;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class EngineKpiProvider implements KpiReporter {

	private final AtomicLong totalCount = new AtomicLong();

	private final AtomicLong processedCount = new AtomicLong();

	private final AtomicLong bunchCount = new AtomicLong();

	private final AtomicLong cumulatedDepth = new AtomicLong();

	private final AtomicLong deadEndCount = new AtomicLong();

	@Override
	public void fetchReport(final KpiCollector collector) {
		final long processed = processedCount.get();
		final long deadEnds = deadEndCount.get();
		final long bunches = bunchCount.get();
		final int depth = Math.round(1000f * cumulatedDepth.get() / bunches);
		final long total = totalCount.get();
		collector.add("total", total);
		collector.add("processed", processed);
		collector.add("bunches", bunches);
		collector.add("depth", depth);
		collector.add("next-gen-size", (total + deadEnds) / processed);
		collector.add("dead-ends", deadEnds);
	}

	public void report(final int depth, final int processed, final PrioritizedGeneration<?> generation) {
		bunchCount.incrementAndGet();
		processedCount.addAndGet(processed);
		cumulatedDepth.addAndGet(depth);
		totalCount.addAndGet(generation.totalSize());
		deadEndCount.addAndGet(generation.deadEnds());
	}

}
