package euclid.engine;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class EngineKpiProvider implements KpiReporter {

	private final AtomicLong totalCount = new AtomicLong();

	private final AtomicLong processedCount = new AtomicLong();

	private final AtomicLong cumulatedDepth = new AtomicLong();

	private final AtomicLong cumulatedNextGenerationSize = new AtomicLong();

	private final AtomicLong deadEndCount = new AtomicLong();

	private final IntSupplier solutionsCount;

	public EngineKpiProvider(final IntSupplier solutionsCount) {
		this.solutionsCount = solutionsCount;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		final long processed = processedCount.get();
		final int depth = Math.round(1000f * cumulatedDepth.get() / processed);
		collector.add("total", totalCount.get());
		collector.add("processed", processed);
		collector.add("depth", depth);
		collector.add("next-gen-size", cumulatedNextGenerationSize.get() / processed);
		collector.add("dead-ends", deadEndCount.get());
		collector.add("solutions", solutionsCount.getAsInt());
	}
	
	public void reportProcessed(final int depth, final int nextGenerationSize) {
		processedCount.incrementAndGet();
		cumulatedDepth.addAndGet(depth);
		cumulatedNextGenerationSize.addAndGet(nextGenerationSize);
	}

	public void reportCandidate(final int priority) {
		totalCount.incrementAndGet();
		if(priority < 0) {
			deadEndCount.incrementAndGet();
		}
	}

}
