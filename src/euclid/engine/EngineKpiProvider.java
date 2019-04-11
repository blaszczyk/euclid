package euclid.engine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

import euclid.kpi.KpiCollector;
import euclid.kpi.KpiReporter;

public class EngineKpiProvider implements KpiReporter {

	private final AtomicInteger processedCount = new AtomicInteger();

	private final AtomicInteger cumulatedDepth = new AtomicInteger();

	private final AtomicInteger cumulatedNextGenerationSize = new AtomicInteger();

	private final AtomicInteger deadEndCount = new AtomicInteger();

	private final IntSupplier solutionsCount;

	public EngineKpiProvider(final IntSupplier solutionsCount) {
		this.solutionsCount = solutionsCount;
	}

	@Override
	public void fetchReport(final KpiCollector collector) {
		final int processed = processedCount.get();
		final int depth = Math.round(1000f * cumulatedDepth.get() / processed);
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
		if(priority < 0) {
			deadEndCount.incrementAndGet();
		}
	}

}
